package com.mercierlucas.feedarticlescompose.ui.register

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlescompose.R
import com.mercierlucas.feedarticlescompose.data.local.MyPrefs
import com.mercierlucas.feedarticlescompose.data.model.RegisterModel
import com.mercierlucas.feedarticlescompose.data.network.api.ApiService
import com.mercierlucas.feedarticlescompose.data.network.dtos.RegisterDto
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
class RegisterViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val apiService: ApiService
): ViewModel() {


    private val _isProgressBarDisplayedStateFlow = MutableStateFlow(false)
    val isProgressBarDisplayedStateFlow = _isProgressBarDisplayedStateFlow.asStateFlow()


    private val _goToMainSharedFlow = MutableSharedFlow<Boolean>()
    val goToMainSharedFlow = _goToMainSharedFlow.asSharedFlow()

    private val _messageSharedFlow = MutableSharedFlow<Int>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()


    private fun setIsProgressBarDisplayed(boolean: Boolean){
        _isProgressBarDisplayedStateFlow.value = boolean
    }


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

    private fun registerIn(registerDto: RegisterDto) {
        viewModelScope.launch {
            setIsProgressBarDisplayed(true)
            delay(1000)
            try {
                val responseRegister = withContext(Dispatchers.IO) {
                    apiService.registerUserAndGetToken(registerDto)
                }

                val body = responseRegister?.body()
                when{
                    responseRegister == null -> Log.e(ContentValues.TAG,"No response from server")
                    responseRegister.isSuccessful && body != null ->{
                        myPrefs.apply {
                            token = body.token
                            userId = body.id
                        }
                        displayToast(R.string.user_logged)
                        goToMainScreen()
                    }
                    else -> {
                        when(responseRegister.code()){
                            303 -> displayToast(R.string.login_already_used)
                            304 -> displayToast(R.string.token_security_problem)
                            400 -> displayToast(R.string.error_param)
                            503 -> Log.e(ContentValues.TAG,"MySQL error")
                            else -> responseRegister.errorBody()?.let {
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


    fun validateInputs(registerModel: RegisterModel) {
        with(registerModel){
            if (password == confirmedPassword)
                if (login.trim().isNotEmpty() && password.trim().isNotEmpty())
                    registerIn(RegisterDto(login,password))
                else
                    displayToast(R.string.empty_required_fields)
            else
                displayToast(R.string.error_on_confirmed_password)
        }
    }


}