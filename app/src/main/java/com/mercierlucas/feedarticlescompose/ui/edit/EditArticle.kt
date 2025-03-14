package com.mercierlucas.feedarticlescompose.ui.edit

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.network.dtos.ArticleDto
import com.mercierlucas.feedarticlescompose.data.network.dtos.UpdateArticleDto
import com.mercierlucas.feedarticlescompose.ui.custom.ButtonCustom
import com.mercierlucas.feedarticlescompose.ui.custom.RadioButtonSingleSelectionHorizontal
import com.mercierlucas.feedarticlescompose.ui.custom.TextFieldCustom
import com.mercierlucas.feedarticlescompose.ui.theme.FeedArticlesComposeTheme
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_MANGA
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_OTHERS
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_SPORT
import com.mercierlucas.feedarticlescompose.utils.showToast

@Composable
fun EditArticleScreen(
    navController: NavHostController,
    editArticleViewModel: EditArticleViewModel,
    idArticleToEdit: Long
) {

    editArticleViewModel.setArticleToEditInfos(idArticleToEdit)

    val articleInfos by editArticleViewModel.articleToEdit.collectAsState()
    val isProgressBarActive by editArticleViewModel.isProgressBarDisplayedStateFlow.collectAsState()

    val context = LocalContext.current

    articleInfos?.let{ articleToEdit ->
        EditArticleView(
            idArticleToEdit,
            articleToEdit,
            isProgressBarActive,
            onClickConfirmButton = {updateArticleDto ->
                editArticleViewModel.validateInputs(updateArticleDto)
            }
        )
    }

    with(editArticleViewModel){
        LaunchedEffect(true) {
            messageSharedFlow.collect { messageId ->
                with(context){
                    showToast(getString(messageId))
                }
            }
        }

        LaunchedEffect(true) {
            goToMainSharedFlow.collect { yes ->
                if(yes)
                    navController.popBackStack()
            }
        }
    }
}


@Composable
fun EditArticleView(
    idArticleToEdit: Long,
    articleInfos: ArticleDto,
    isProgressBarActive: Boolean,
    onClickConfirmButton: (UpdateArticleDto) -> Unit
) {

    var title    by remember { mutableStateOf    (articleInfos.title)       }
    var desc     by remember { mutableStateOf    (articleInfos.description) }
    var imageUrl by remember { mutableStateOf    (articleInfos.urlImage)    }
    var category by remember { mutableIntStateOf (articleInfos.category)    }

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(Modifier.padding(vertical = 20.dp)) {
            Text(
                text = stringResource(R.string.edit_article),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column (modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ){
            TextFieldCustom(
                value = title,
                onValueChange = { if(it.length <= 80) title = it  },
                labelText = stringResource(id = R.string.title),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            TextFieldCustom(
                value = desc,
                onValueChange = { desc = it },
                labelText = stringResource(id = R.string.content),
                minLines = 8,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            TextFieldCustom(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                labelText = stringResource(id = R.string.image_url),
                maxLines = 2,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(5.dp),
                placeholder = painterResource(R.drawable.feedarticles_logo),
                error = painterResource(R.drawable.feedarticles_logo),
            )

            Row (Modifier.padding(vertical = 20.dp)){
                RadioButtonSingleSelectionHorizontal(
                    currentCategory = category,
                    radioOptions = listOf(CATEGORY_SPORT, CATEGORY_MANGA, CATEGORY_OTHERS),
                    callbackRBSelected = {categorySelected ->
                        category = categorySelected
                    }
                )
            }
        }

        if (isProgressBarActive)
            CircularProgressIndicator()

        Row (Modifier.padding(bottom = 40.dp)){
            Column {
                ButtonCustom(label = stringResource(id = R.string.save),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)) {
                    onClickConfirmButton.invoke(UpdateArticleDto(
                        title = title,
                        desc = desc,
                        image = imageUrl,
                        cat = category,
                        id = idArticleToEdit
                    ))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditArticlePreview() {

    FeedArticlesComposeTheme(darkTheme = false,dynamicColor = false){
        //EditArticleView(null,true,{})
    }

}