package com.mercierlucas.feedarticlescompose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun GetStringInViewModel(resId: Int){
    stringResource(id = resId)
}