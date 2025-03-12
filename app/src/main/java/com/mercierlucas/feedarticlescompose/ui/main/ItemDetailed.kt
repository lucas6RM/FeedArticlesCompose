package com.mercierlucas.feedarticlescompose.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.model.ItemClicked
import com.mercierlucas.feedarticlescompose.data.network.dtos.ArticleDto
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_OTHERS
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_SPORT
import com.mercierlucas.feedarticlescompose.utils.formatApiDate
import kotlinx.coroutines.delay

@Composable
fun ItemDetailed(
    article: ArticleDto,
    modifierForMainColumnOnly: Modifier = Modifier,
    itemClickedForDetails: ItemClicked
){
    var isExpended by remember{ mutableStateOf(false) }
    var isVisible by remember{ mutableStateOf(true) }
    val animatedAlpha by animateFloatAsState(
        targetValue = if(isVisible) 1f else 0f,
        animationSpec = tween(2000, 200, easing = LinearEasing), label = ""
    )
    var isBig by remember{ mutableStateOf(true) }
    val animatedSize by animateDpAsState(
        targetValue = if(isBig) 100.dp else 60.dp,
        animationSpec = tween(2000, 200, easing = LinearEasing), label = ""
    )

    LaunchedEffect(itemClickedForDetails) {
        delay(100)
        isExpended = true
    }

    Column (modifier = modifierForMainColumnOnly.clickable {
        isExpended = !isExpended
        isVisible = !isVisible
        isBig = !isBig
    }){

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.
                fillMaxWidth()

        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(article.urlImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(animatedSize)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                        .background(Color.White),
                    placeholder = painterResource(R.drawable.feedarticles_logo),
                    error = painterResource(R.drawable.feedarticles_logo),
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = article.title,
                        modifier = Modifier.padding(5.dp),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowUp,
                        contentDescription = stringResource(id = R.string.close_details),
                        modifier = Modifier
                            .size(20.dp)
                            .alpha(animatedAlpha)
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isExpended,
            enter =  expandVertically(animationSpec = tween(durationMillis = 2000)),
            exit =  shrinkVertically(animationSpec = tween(durationMillis = 2000))
        ){
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),

                ) {
                    Text(
                        text = stringResource(id = R.string.from, formatApiDate(article.createdAt)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(5.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    when (article.category) {
                        CATEGORY_SPORT -> Text(
                            text = stringResource(
                                id = R.string.category_s, stringResource(id = R.string.sport)
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(5.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        CATEGORY_MANGA -> Text(
                            text = stringResource(
                                id = R.string.category_s, stringResource(id = R.string.manga)
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(5.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        CATEGORY_OTHERS -> Text(
                            text = stringResource(
                                id = R.string.category_s, stringResource(id = R.string.others)
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(5.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = article.description, style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(top = 20.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ItemDetailedPreview(){
    FeedArticlesComposeTheme(darkTheme = false,dynamicColor = false) {
        /*ItemDetailed(
            ArticleDto(2,
                "2023-12-05 07:54:24",
                stringResource(id = R.string.large_size_description),
                1,
                1,
                "un titre",
                "https://i.discogs.com/1FYJYr17AjQk6Mlk9FqagjH46qXI5EaFq5RQIB4ylw0/rs:fit/" +
                        "g:sm/q:40/h:300/w:300/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTQwNzQ2/" +
                        "LTEzNDMwMTgwNzAt/Njg2MC5qcGVn.jpeg"
            ),
            itemClickedForDetails = itemClickedForDetails,
        )*/
    }
}