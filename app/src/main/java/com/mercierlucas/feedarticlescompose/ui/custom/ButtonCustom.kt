package com.mercierlucas.feedarticlescompose.ui.custom

import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.ui.theme.BlueFeedArt

@Composable
fun ButtonCustom(
    label: String = stringResource(id = R.string.connect),
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
){
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}

@Preview
@Composable
fun ButtonCustomPreview(){
    ButtonCustom("Validate",Modifier,{})
}