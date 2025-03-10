package com.mercierlucas.feedarticlescompose.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.mercierlucas.feedarticlescompose.ui.login.LoginView
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme

@Composable
fun EditArticleScreen(){

}


@Composable
fun EditArticleView(){

}

@Preview(showBackground = true)
@Composable
fun EditArticlePreview(
    navController: NavHostController,
    editArticleViewModel: EditArticleViewModel
) {

    FeedArticlesComposeTheme{
        EditArticleView()
    }

}