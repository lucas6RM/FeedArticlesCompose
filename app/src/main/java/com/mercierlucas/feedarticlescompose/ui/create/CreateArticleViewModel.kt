package com.mercierlucas.feedarticlescompose.ui.create

import androidx.lifecycle.ViewModel
import com.mercierlucas.feedarticlescompose.data.local.MyPrefs
import com.mercierlucas.feedarticlescompose.data.network.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateArticleViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val apiService: ApiService
): ViewModel() {


}