package com.mercierlucas.feedarticlescompose.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.local.MyPrefs
import com.mercierlucas.feedarticlescompose.data.model.ItemClicked
import com.mercierlucas.feedarticlescompose.data.network.dtos.ArticleDto
import com.mercierlucas.feedarticlescompose.data.network.api.ApiService
import com.mercierlucas.feedarticlescompose.utils.CATEGORY_ALL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val apiService: ApiService
): ViewModel() {

    private val _isSwipeDisplayedStateFlow = MutableStateFlow(false)
    val isSwipeDisplayedStateFlow = _isSwipeDisplayedStateFlow.asStateFlow()

    private val _isDeleteBackGroundVisibleStateFlow = MutableStateFlow(true)
    val isDeleteBackGroundVisibleStateFlow = _isDeleteBackGroundVisibleStateFlow.asStateFlow()

    private val _userIdStateFlow = MutableStateFlow(myPrefs.userId)
    val userIdStateFlow = _userIdStateFlow.asStateFlow()

    private val _articleListStateFlow = MutableStateFlow<List<ArticleDto>>(emptyList())
    val articleListStateFlow = _articleListStateFlow.asStateFlow()

    private val _articleListFilteredStateFlow = MutableStateFlow<List<ArticleDto>>(emptyList())
    val articleListFilteredStateFlow = _articleListFilteredStateFlow.asStateFlow()

    private val _currentCategoryStateFlow = MutableStateFlow(CATEGORY_ALL)
    val currentCategoryStateFlow = _currentCategoryStateFlow.asStateFlow()

    private val _itemClickedForDetailsStateFlow =
        MutableStateFlow(ItemClicked(0L, false))
    val itemClickedForDetailsStateFlow = _itemClickedForDetailsStateFlow.asStateFlow()

    private val _messageSharedFlow = MutableSharedFlow<Int>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _goToEditSharedFlow = MutableSharedFlow<Long>()
    val goToEditSharedFlow = _goToEditSharedFlow.asSharedFlow()


    fun setCurrentCategory(categoryChanged: Int) {
        _currentCategoryStateFlow.value = categoryChanged
        refreshFilteredArticles()
    }

    fun clearMySharedPreferences() {
        myPrefs.apply {
            userId = 0L
            token = null
        }
    }


    private fun refreshFilteredArticles() {
        val categoryToFilter = _currentCategoryStateFlow.value
        if (categoryToFilter != CATEGORY_ALL)
            _articleListFilteredStateFlow.value =
                _articleListStateFlow.value.filter { article ->
                    article.category == _currentCategoryStateFlow.value
                }
        else
            _articleListFilteredStateFlow.value = _articleListStateFlow.value
    }

    fun getAllArticlesAndRefreshFilter() {
        viewModelScope.launch {
            _isSwipeDisplayedStateFlow.value = true
            try {
                val responseGetAllArticles = withContext(Dispatchers.IO){
                    apiService.getAllArticles(myPrefs.token)
                }
                val body = responseGetAllArticles?.body()
                when{
                    responseGetAllArticles == null -> Log.e(ContentValues.TAG,"Pas de reponse du serveur")
                    responseGetAllArticles.isSuccessful && body !=null ->{
                        _articleListStateFlow.value = body.toList()
                        refreshFilteredArticles()
                    }
                    else -> {
                        when(responseGetAllArticles.code()){
                            400 -> displayToast(R.string.error_param)
                            401 -> displayToast(R.string.unauthorized)
                            503 -> Log.e(ContentValues.TAG,"MySQL error")
                            else -> responseGetAllArticles.errorBody()?.let {
                                Log.e(ContentValues.TAG, it.string())
                            }
                        }
                    }
                }
            } catch (error : ConnectException){
                displayToast(R.string.no_response_from_server)
            }
            _isSwipeDisplayedStateFlow.value = false
        }
    }


    private fun displayToast(stringId:Int){
        viewModelScope.launch{
            _messageSharedFlow.emit(stringId)
        }
    }

    fun checkDestinationForItemClicked(idArticle: Long, idUser: Long) {
        if (idUser == myPrefs.userId){
            resetIsDeleteBackgroundVisibility()
            goToEditArticle(idArticle)
            resetItemClicked()
        }
        else{
            displayDetailArticle(idArticle)
        }

    }

    fun resetItemClicked() {
        _itemClickedForDetailsStateFlow.value = ItemClicked(0L,false)
    }

    private fun displayDetailArticle(idArticle: Long) {

        if(!itemClickedForDetailsStateFlow.value.selected ||
            itemClickedForDetailsStateFlow.value.idItemClicked != idArticle){
            _itemClickedForDetailsStateFlow.value = ItemClicked(idArticle,true)

        }
        else{
            _itemClickedForDetailsStateFlow.value = ItemClicked(idArticle,false)
        }
    }

    private fun goToEditArticle(idArticle: Long) {
        viewModelScope.launch { 
            _goToEditSharedFlow.emit(idArticle)
        }
    }

    fun deleteArticle(idArticle: Long) {
        viewModelScope.launch{

            try {

                val responseDeleteArticle = withContext(Dispatchers.IO){
                    apiService.deleteArticleEdited(idArticle,myPrefs.token)
                }
                val body = responseDeleteArticle?.body()
                when{
                    responseDeleteArticle == null -> Log.e(ContentValues.TAG,"Pas de reponse du serveur")
                    responseDeleteArticle.isSuccessful && body != null ->{
                        displayToast(R.string.article_deleted)
                        _isDeleteBackGroundVisibleStateFlow.value = false
                        getAllArticlesAndRefreshFilter()
                        resetIsDeleteBackgroundVisibility()
                    }
                    else -> {
                        when(responseDeleteArticle.code()){
                            304 -> displayToast(R.string.article_not_deleted)
                            400 -> displayToast(R.string.error_param)
                            401 -> displayToast(R.string.unauthorized)
                            503 -> Log.e(ContentValues.TAG,"MySQL error")
                            else -> responseDeleteArticle.errorBody()?.let {
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

    private fun resetIsDeleteBackgroundVisibility(){
        _isDeleteBackGroundVisibleStateFlow.value = true
    }



}