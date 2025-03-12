package com.mercierlucas.feedarticlescompose.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mercierlucas.feedarticlescompose.data.network.dtos.ArticleDto
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_OTHERS
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_SPORT
import com.mercierlucas.feedarticlescompose.utils.formatApiDate

@Composable
fun ItemNotDetailed(article: ArticleDto, modifierForMainRowOnly: Modifier = Modifier){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = with(modifierForMainRowOnly){
            fillMaxWidth()
        }
    )
    {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Black, CircleShape)
                    .background(Color.White),
                placeholder = painterResource(R.drawable.feedarticles_logo),
                error = painterResource(R.drawable.feedarticles_logo),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = article.title,
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun ItemNotDetailedPreview(){
    FeedArticlesComposeTheme(darkTheme = false,dynamicColor = false) {
        ItemNotDetailed(ArticleDto(2,
            "2023-12-05 07:54:24",
            stringResource(id = R.string.large_size_description),
            1,
            1,
            "un titre",
            "https://i.discogs.com/1FYJYr17AjQk6Mlk9FqagjH46qXI5EaFq5RQIB4ylw0/rs:fit/" +
                    "g:sm/q:40/h:300/w:300/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTQwNzQ2/" +
                    "LTEzNDMwMTgwNzAt/Njg2MC5qcGVn.jpeg"
        ))
    }
}