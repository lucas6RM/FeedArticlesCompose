package com.mercierlucas.feedarticlescompose.ui.register

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.mercierlucas.feedarticlescompose.ui.login.LoginView
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme

@Composable
fun RegisterScreen(navController: NavHostController, registerViewModel: RegisterViewModel) {

}


@Composable
fun RegisterView(){

}

@Preview(showBackground = true)
@Composable
fun RegisterPreview(){
    FeedArticlesComposeTheme{
        RegisterView()
    }
}