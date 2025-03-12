package com.mercierlucas.feedarticlescompose.ui.edit

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.local.MyPrefs
import com.mercierlucas.feedarticlescompose.data.network.dtos.ArticleDto
import com.mercierlucas.feedarticlescompose.data.network.api.ApiService
import com.mercierlucas.feedarticlescompose.data.network.dtos.UpdateArticleDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class EditArticleViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val apiService: ApiService
): ViewModel(){


    private val _articleToEdit = MutableStateFlow<ArticleDto?>(null)
    val articleToEdit = _articleToEdit.asStateFlow()

    private val _isProgressBarDisplayedStateFlow = MutableStateFlow(false)
    val isProgressBarDisplayedStateFlow = _isProgressBarDisplayedStateFlow.asStateFlow()



    private val _messageSharedFlow = MutableSharedFlow<Int>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _goToMainSharedFlow = MutableSharedFlow<Boolean>()
    val goToMainSharedFlow = _goToMainSharedFlow.asSharedFlow()

    private fun displayToast(stringId:Int){
        viewModelScope.launch{
            _messageSharedFlow.emit(stringId)
        }
    }

    private fun goToMainScreen(){
        viewModelScope.launch {
            _goToMainSharedFlow.emit(true)
        }
    }

    fun setArticleToEditInfos(idArticleToEdit: Long){
        viewModelScope.launch{
            try {
                val responseGetArticle = withContext(Dispatchers.IO){
                    apiService.getOneArticle(myPrefs.token,idArticleToEdit)
                }
                val body = responseGetArticle?.body()
                when{
                    responseGetArticle == null -> Log.e(ContentValues.TAG,"Pas de reponse du serveur")
                    responseGetArticle.isSuccessful && body != null ->{
                        _articleToEdit.value = body

                        Log.i(TAG, "article obtenu : ${articleToEdit.value!!.title}")

                    }
                    else -> {
                        when(responseGetArticle.code()){
                            303 -> displayToast(R.string.article_not_found)
                            400 -> displayToast(R.string.error_param)
                            401 -> displayToast(R.string.unauthorized)
                            503 -> Log.e(ContentValues.TAG,"MySQL error")
                            else -> responseGetArticle.errorBody()?.let {
                                Log.e(ContentValues.TAG, it.string())
                            }
                        }
                    }
                }
            }catch (_: ConnectException){
                displayToast(R.string.no_response_from_server)
            }
        }
    }

    fun setIsProgressBarDisplayed(boolean: Boolean){
        _isProgressBarDisplayedStateFlow.value = boolean
    }


    fun validateInputs(updateArticleDto: UpdateArticleDto) {
        with(updateArticleDto){
            if (title.isNotEmpty() && desc.isNotEmpty()){
                updateArticle(updateArticleDto)
            }
            else
                displayToast(com.mercierlucas.feedarticlescompose.R.string.empty_required_fields)
        }
    }

    private fun updateArticle(updateArticleDto: UpdateArticleDto) {
        viewModelScope.launch {
            setIsProgressBarDisplayed(true)
            delay(1000)
            try {
                val responseCreateArticle = withContext(Dispatchers.IO) {
                    apiService.updateArticleEdited(
                        updateArticleDto.id,
                        myPrefs.token,
                        updateArticleDto
                    )
                }

                val body = responseCreateArticle?.body()
                when{
                    responseCreateArticle == null -> Log.e(ContentValues.TAG,"Pas de reponse du serveur")
                    responseCreateArticle.isSuccessful && body != null ->{
                        displayToast(R.string.article_modified)
                        goToMainScreen()
                    }
                    else -> {
                        when(responseCreateArticle.code()){
                            303 -> displayToast(R.string.ids_are_different)
                            304 -> displayToast(R.string.article_not_modified)
                            400 -> displayToast(R.string.error_param)
                            401 -> displayToast(R.string.unauthorized)
                            503 -> Log.e(ContentValues.TAG,"MySQL error")
                            else -> responseCreateArticle.errorBody()?.let {
                                Log.e(ContentValues.TAG, it.string())
                            }
                        }
                    }
                }
            } catch (_: ConnectException){
                displayToast(R.string.no_response_from_server)
            }
            setIsProgressBarDisplayed(false)
        }


    }

}