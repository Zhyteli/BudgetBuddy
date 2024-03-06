package com.budget.buddy.presentation.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budget.buddy.data.api.mono.ApiService
import com.budget.buddy.domain.cash.usecase.AddCashTransactionUseCase
import com.budget.buddy.domain.cash.usecase.DeleteTransactionUseCase
import com.budget.buddy.domain.cash.usecase.DeleteTransactionsByIdsUseCase
import com.budget.buddy.domain.cash.usecase.GetAllTransactionsUseCase
import com.budget.buddy.domain.cash.usecase.GetTransactionByIdUseCase
import com.budget.buddy.domain.cash.usecase.UpdateTransactionUseCase
import com.budget.buddy.domain.cash.usecase.UpdateTransactionsUseCase
import com.budget.buddy.domain.mono.UsersBankDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addCashTransactionUseCase: AddCashTransactionUseCase,
    private val deleteTransactionsByIdsUseCase: DeleteTransactionsByIdsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionsUseCase: UpdateTransactionsUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,

    private val apiService: ApiService,
) : ViewModel() {

    private val _resultUserDataApi = MutableLiveData<List<UsersBankDetails>>()
    val resultUserDataApi:LiveData<List<UsersBankDetails>>
        get() = _resultUserDataApi

    fun testTime(): String {
        val calendarFrom = Calendar.getInstance()
        calendarFrom.set(Calendar.MONTH, Calendar.MARCH)
        calendarFrom.set(Calendar.DAY_OF_MONTH, 1)
        calendarFrom.set(Calendar.HOUR_OF_DAY, 0)
        calendarFrom.set(Calendar.MINUTE, 0)
        calendarFrom.set(Calendar.SECOND, 0)
        calendarFrom.set(Calendar.MILLISECOND, 0)
        return (calendarFrom.timeInMillis / 1000).toString()
    }

    val toUnixTime = (System.currentTimeMillis() / 1000).toString()

    fun getMonoData(
        callbackError: (String) -> Unit = {},
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getUserData(from = testTime(), to = toUnixTime)
                if (response.isSuccessful) {
                    val userData: Array<UsersBankDetails>? = response.body()
                    userData?.let {
                        _resultUserDataApi.postValue(it.toList())
                    }
                } else {
                    callbackError(response.errorBody()?.string().toString())
                }
            } catch (e: Exception) {
                callbackError(e.message.toString())
            }
        }
    }
}