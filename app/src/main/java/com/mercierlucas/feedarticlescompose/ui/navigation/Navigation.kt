package com.mercierlucas.feedarticlescompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mercierlucas.feedarticlescompose.ui.create.CreateArticleScreen
import com.mercierlucas.feedarticlescompose.ui.create.CreateArticleViewModel
import com.mercierlucas.feedarticlescompose.ui.edit.EditArticlePreview
import com.mercierlucas.feedarticlescompose.ui.edit.EditArticleViewModel
import com.mercierlucas.feedarticlescompose.ui.login.LoginScreen
import com.mercierlucas.feedarticlescompose.ui.login.LoginViewModel
import com.mercierlucas.feedarticlescompose.ui.main.MainScreen
import com.mercierlucas.feedarticlescompose.ui.main.MainViewModel
import com.mercierlucas.feedarticlescompose.ui.register.RegisterScreen
import com.mercierlucas.feedarticlescompose.ui.register.RegisterViewModel
import com.mercierlucas.feedarticlescompose.ui.splash.SplashScreen
import com.mercierlucas.feedarticlescompose.ui.splash.SplashViewModel


sealed class Screen(val route : String){
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object CreateArticle : Screen("create_article")
    object EditArticle : Screen("edit_article")
}

@Composable
fun MyNavigation(navController: NavHostController = rememberNavController()){

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ){
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, splashViewModel)
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }

        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController, registerViewModel)
        }

        composable(Screen.Main.route) {
            val mainViewModel: MainViewModel = hiltViewModel()
            MainScreen(navController, mainViewModel)
        }

        composable(Screen.CreateArticle.route) {
            val createArticleViewModel: CreateArticleViewModel = hiltViewModel()
            CreateArticleScreen(navController, createArticleViewModel)
        }

        composable(Screen.EditArticle.route) {
            val editArticleViewModel: EditArticleViewModel = hiltViewModel()
            EditArticlePreview(navController, editArticleViewModel)
        }

    }
}