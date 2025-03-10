package com.mercierlucas.feedarticlescompose.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.mercierlucas.feedarticlescompose.ui.splash.SplashView
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme

@Composable
fun LoginScreen(navController: NavHostController, loginViewModel: LoginViewModel) {

}


@Composable
fun LoginView(){

}

@Preview(showBackground = true)
@Composable
fun LoginPreview(){

    FeedArticlesComposeTheme{
        LoginView()
    }

}