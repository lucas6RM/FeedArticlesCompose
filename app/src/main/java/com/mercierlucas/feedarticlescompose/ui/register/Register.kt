package com.mercierlucas.feedarticlescompose.ui.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.model.LoginModel
import com.mercierlucas.feedarticlescompose.data.model.RegisterModel
import com.mercierlucas.feedarticlescompose.ui.custom.ButtonCustom
import com.mercierlucas.feedarticlescompose.ui.custom.TextFieldCustom
import com.mercierlucas.feedarticlescompose.ui.navigation.Screen
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme
import com.mercierlucas.feedarticlescompose.utils.showToast

@Composable
fun RegisterScreen(navController: NavHostController, registerViewModel: RegisterViewModel) {

    val isProgressBarActive by registerViewModel.isProgressBarDisplayedStateFlow.collectAsState()

    val context = LocalContext.current

    RegisterView(isProgressBarActive,
        onClickConfirmButton = { registerModel ->
            registerViewModel.validateInputs(registerModel)
        }
    )

    LaunchedEffect(true) {
        registerViewModel.messageSharedFlow.collect { messageId ->
            with(context){
                showToast(getString(messageId))
            }
        }
    }

    LaunchedEffect(true) {
        registerViewModel.goToMainSharedFlow.collect { yes ->
            if(yes)
                navController.navigate(Screen.Main.route){
                    popUpTo(Screen.Login.route){
                        inclusive = true
                    }
                }
        }
    }


}


@Composable
fun RegisterView(
    isProgressBarActive: Boolean,
    onClickConfirmButton: (RegisterModel) -> Unit
){

    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmedPassword by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(Modifier.padding(top = 20.dp)) {
            Text(
                text = stringResource(R.string.new_account),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column (modifier = Modifier.weight(0.5f)){}

        Row {
            Column {
                TextFieldCustom(
                    value = login,
                    onValueChange = { login = it },
                    labelText = stringResource(id = R.string.login),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                TextFieldCustom(
                    value = password,
                    onValueChange = { password = it },
                    labelText = stringResource(id = R.string.password),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                TextFieldCustom(
                    value = confirmedPassword,
                    onValueChange = { confirmedPassword = it },
                    labelText = stringResource(id = R.string.confirm_password),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        }
        Column (modifier = Modifier.weight(0.5f)){}
        if (isProgressBarActive)
            CircularProgressIndicator()
        Column (modifier = Modifier.weight(0.5f)){}

        Row {
            Column {
                ButtonCustom(label = stringResource(id = R.string.subscribe)) {
                    onClickConfirmButton.invoke(RegisterModel(login, password, confirmedPassword))
                }
            }
        }
        Column (modifier = Modifier.weight(1f)){}
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterPreview(){
    FeedArticlesComposeTheme(darkTheme = false,dynamicColor = false){
        RegisterView(true, {})
    }
}