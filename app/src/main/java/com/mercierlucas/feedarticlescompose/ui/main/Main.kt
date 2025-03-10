package com.mercierlucas.feedarticlescompose.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.mercierlucas.feedarticlescompose.ui.login.LoginView
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme

@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel) {

}


@Composable
fun MainView(){

}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    FeedArticlesComposeTheme{
        MainView()
    }
}