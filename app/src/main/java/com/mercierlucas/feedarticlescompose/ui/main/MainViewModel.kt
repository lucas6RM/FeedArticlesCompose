package com.mercierlucas.feedarticlescompose.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.local.MyPrefs
import com.mercierlucas.feedarticlescompose.data.model.Article
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

    private val _articleListStateFlow = MutableStateFlow<List<Article>>(emptyList())
    val articleListStateFlow = _articleListStateFlow.asStateFlow()

    private val _articleListFilteredStateFlow = MutableStateFlow<List<Article>>(emptyList())
    val articleListFilteredStateFlow = _articleListFilteredStateFlow.asStateFlow()

    private val _currentCategoryStateFlow = MutableStateFlow(CATEGORY_ALL)
    val currentCategoryStateFlow = _currentCategoryStateFlow.asStateFlow()


    private val _messageSharedFlow = MutableSharedFlow<Int>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()


    init {
        getAllArticlesAndRefreshFilter()
    }

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
                    article.categorie == _currentCategoryStateFlow.value
                }
        else
            _articleListFilteredStateFlow.value = _articleListStateFlow.value
    }

    private fun getAllArticlesAndRefreshFilter() {
        viewModelScope.launch {
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
        }
    }


    private fun displayToast(stringId:Int){
        viewModelScope.launch{
            _messageSharedFlow.emit(stringId)
        }
    }



}