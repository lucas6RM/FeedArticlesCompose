package com.mercierlucas.feedarticlescompose.ui.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.mercierlucas.feedarticlescompose.ui.edit.EditArticleView
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme

@Composable
fun CreateArticleScreen(
    navController: NavHostController,
    createArticleViewModel: CreateArticleViewModel
) {

}


@Composable
fun CreateArticleView(){

}

@Preview(showBackground = true)
@Composable
fun CreateArticlePreview(){

    FeedArticlesComposeTheme{
        CreateArticleView()
    }

}