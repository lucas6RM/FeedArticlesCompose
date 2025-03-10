package com.mercierlucas.feedarticlescompose.ui.splash

import android.app.Activity
import android.provider.Settings.Global.getString
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.mercierlucas.feedarticlescompose.MyApp
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.ui.navigation.Screen
import com.mercierlucas.feedarticlescompose.ui.theme.BlueFeedArt
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme

@Composable
fun SplashScreen(navController: NavHostController, splashViewModel: SplashViewModel) {

    val statusBarLight = Color.Transparent
    val view = LocalView.current

    DisposableEffect(true) {
        val activity = view.context as Activity
        onDispose {
            activity.window.statusBarColor = statusBarLight.toArgb()
            WindowCompat.getInsetsController(activity.window, activity.window.decorView)
                .isAppearanceLightStatusBars = true
        }
    }
    
    SplashView()

    LaunchedEffect(true) {
        splashViewModel.isUserLoggedSharedFlow.collect{ yes ->
            if(yes)
                navController.navigate(Screen.Main.route){
                    popUpTo(Screen.Splash.route){
                        inclusive = true
                    }
                }
            else
                navController.navigate(Screen.Login.route){
                    popUpTo(Screen.Splash.route){
                        inclusive = true
                    }
                }
        }
    }
}


@Composable
fun SplashView(){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(BlueFeedArt)
    )
    {
        Image(
            painter = painterResource(id = R.drawable.feedarticles_logo),
            contentDescription = "splash logo",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = stringResource(R.string.feed_articles),
            style = MaterialTheme.typography.titleLarge
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SplashPreview(){
    FeedArticlesComposeTheme{
        SplashView()
    }
}