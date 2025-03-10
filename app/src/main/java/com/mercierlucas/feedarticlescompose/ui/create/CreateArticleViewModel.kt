package com.mercierlucas.feedarticlescompose.ui.create

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.local.MyPrefs
import com.mercierlucas.feedarticlescompose.data.model.CreateArticleModel
import com.mercierlucas.feedarticlescompose.data.network.api.ApiService
import com.mercierlucas.feedarticlescompose.data.network.dtos.CreateArticleDto
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
class CreateArticleViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val apiService: ApiService
): ViewModel() {


    private val _isProgressBarDisplayedStateFlow = MutableStateFlow(false)
    val isProgressBarDisplayedStateFlow = _isProgressBarDisplayedStateFlow.asStateFlow()

    private val _messageSharedFlow = MutableSharedFlow<Int>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _goToMainSharedFlow = MutableSharedFlow<Boolean>()
    val goToMainSharedFlow = _goToMainSharedFlow.asSharedFlow()

    fun setIsProgressBarDisplayed(boolean: Boolean){
        _isProgressBarDisplayedStateFlow.value = boolean
    }


    private fun displayToast(stringId:Int){
        viewModelScope.launch{
            _messageSharedFlow.emit(stringId)
        }
    }

    fun goToMainScreen(isResponseCorrectFromServer: Boolean){
        viewModelScope.launch {
            _goToMainSharedFlow.emit(isResponseCorrectFromServer)
        }
    }

    fun validateInputs(createArticleModel: CreateArticleModel) {
        with(createArticleModel){
            if (title.isNotEmpty() && desc.isNotEmpty()){
                addNewArticle(CreateArticleDto(
                    title = title,
                    cat = cat,
                    desc = desc,
                    image = imageUrl,
                    idU = myPrefs.userId
                ))
            }
            else
                displayToast(R.string.empty_required_fields)
        }
    }

    private fun addNewArticle(createArticleDto: CreateArticleDto) {
        viewModelScope.launch {
            setIsProgressBarDisplayed(true)
            delay(1000)
            try {
                val responseCreateArticle = withContext(Dispatchers.IO) {
                    apiService.createNewArticle(myPrefs.token,createArticleDto)
                }

                val body = responseCreateArticle?.body()
                when{
                    responseCreateArticle == null -> Log.e(ContentValues.TAG,"Pas de reponse du serveur")
                    responseCreateArticle.isSuccessful && body != null ->{
                        displayToast(R.string.new_article_created)
                        goToMainScreen(true)
                    }
                    else -> {
                        when(responseCreateArticle.code()){
                            304 -> displayToast(R.string.article_not_created)
                            400 -> displayToast(R.string.error_param)
                            401 -> displayToast(R.string.unauthorized)
                            503 -> Log.e(ContentValues.TAG,"MySQL error")
                            else -> responseCreateArticle.errorBody()?.let {
                                Log.e(ContentValues.TAG, it.string())
                            }
                        }
                    }
                }
            } catch (error : ConnectException){
                displayToast(R.string.no_response_from_server)
            }
            setIsProgressBarDisplayed(false)
        }
    }

}