package com.mercierlucas.feedarticlescompose.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp.Companion.Hairline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.model.Article
import com.mercierlucas.feedarticlescompose.ui.custom.RadioButtonSingleSelectionHorizontal
import com.mercierlucas.feedarticlescompose.ui.login.LoginView
import com.mercierlucas.feedarticlescompose.ui.navigation.Screen
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme
import com.mercierlucas.feedarticlescompose.ui.theme.MangaColor
import com.mercierlucas.feedarticlescompose.ui.theme.OthersColor
import com.mercierlucas.feedarticlescompose.ui.theme.SportColor
import com.mercierlucas.feedarticlescompose.ui.theme.UnknownCategoryColor
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_ALL
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_OTHERS
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_SPORT
import com.mercierlucas.feedarticlescompose.utils.showToast

@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel) {


    val articlesList by mainViewModel.articleListFilteredStateFlow.collectAsState()
    val currentCategory by mainViewModel.currentCategoryStateFlow.collectAsState()

    val context = LocalContext.current


    MainView(
        articlesList,
        onClickAddButton = {navController.navigate(Screen.CreateArticle.route)},
        onClickLogoutButton = {
            mainViewModel.clearMySharedPreferences()
            navController.navigate(Screen.Login.route){
            popUpTo(Screen.Main.route){
                inclusive = true
            }
        } },
        onCategoryChange = {categorySelected ->
            mainViewModel.setCurrentCategory(categorySelected)
        },
        currentCategory
    )

    LaunchedEffect(true) {
        mainViewModel.messageSharedFlow.collect { messageId ->
            with(context){
                showToast(getString(messageId))
            }
        }
    }
}


@Composable
fun MainView(
    articlesList: List<Article>,
    onClickAddButton: () -> Unit,
    onClickLogoutButton: () -> Unit,
    onCategoryChange : (Int) -> Unit,
    currentCategory: Int
){

    Column(Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(id = R.string.new_article),
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = { onClickAddButton.invoke() })
            )
            Icon(
                imageVector = Icons.Rounded.PowerSettingsNew,
                contentDescription = stringResource(id = R.string.new_article),
                modifier = Modifier
                    .size(30.dp)
                    .clickable(onClick = { onClickLogoutButton.invoke() })
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            LazyColumn {
                items(items = articlesList){
                    Card (modifier = Modifier.padding(5.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        elevation = CardDefaults.cardElevation( defaultElevation = 4.dp)
                    ){

                        when(it.categorie){
                            CATEGORY_SPORT  -> ItemArticle(article = it,
                                modifier = Modifier.background(SportColor))
                            CATEGORY_MANGA  -> ItemArticle(article = it,
                                modifier = Modifier.background(MangaColor))
                            CATEGORY_OTHERS -> ItemArticle(article = it,
                                modifier = Modifier.background(OthersColor))
                            else            -> ItemArticle(article = it,
                                modifier = Modifier.background(UnknownCategoryColor))
                        }
                    }
                }
            }
        }
        Row (Modifier.padding(vertical = 20.dp)){
            RadioButtonSingleSelectionHorizontal(
                currentCategory = currentCategory,
                radioOptions = listOf(
                    CATEGORY_ALL,
                    CATEGORY_SPORT,
                    CATEGORY_MANGA,
                    CATEGORY_OTHERS),
                callbackRBSelected = { categorySelected ->
                    onCategoryChange.invoke(categorySelected) }
            )
        }
    }
}

@Composable
fun ItemArticle(article: Article, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = with(modifier){
            fillMaxWidth()
        }
    )
    {

        Column(//modifier = Modifier.weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Black, CircleShape),
                placeholder = painterResource(R.drawable.feedarticles_logo),
            )
        }
        Column(//modifier = Modifier.weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = article.titre,
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    FeedArticlesComposeTheme(darkTheme = false,dynamicColor = false){
        MainView(listOf(Article(1,
            "2025/02/03",
            "description",
            1,
            1,
            "un titre",
            "https://i.discogs.com/1FYJYr17AjQk6Mlk9FqagjH46qXI5EaFq5RQIB4ylw0/rs:fit/" +
                    "g:sm/q:40/h:300/w:300/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTQwNzQ2/" +
                    "LTEzNDMwMTgwNzAt/Njg2MC5qcGVn.jpeg"
        ),
            Article(2,
                "2025/02/03",
                "description",
                2,
                2,
                "un titre 2",
                "https://i.discogs.com/1FYJYr17AjQk6Mlk9FqagjH46qXI5EaFq5RQIB4ylw0/rs:fit/" +
                        "g:sm/q:40/h:300/w:300/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTQwNzQ2/" +
                        "LTEzNDMwMTgwNzAt/Njg2MC5qcGVn.jpeg"
            )

            ),{},{},{},CATEGORY_ALL)
    }
}