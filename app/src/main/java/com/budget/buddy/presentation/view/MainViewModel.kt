package com.budget.buddy.presentation.view

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budget.buddy.R
import com.budget.buddy.data.api.mono.ApiService
import com.budget.buddy.data.api.mono.error.ErrorMono
import com.budget.buddy.data.database.getdata.UserDataState
import com.budget.buddy.data.impl.WorkTime
import com.budget.buddy.domain.cash.CashTransaction
import com.budget.buddy.domain.cash.usecase.cashtransaction.AddCashTransactionUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.DeleteTransactionUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.DeleteTransactionsByIdsUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.GetAllTransactionsUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.GetTransactionByIdUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.UpdateTransactionUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.UpdateTransactionsUseCase
import com.budget.buddy.domain.cash.usecase.maindatauser.LoadDataMainUserDataMouthUseCase
import com.budget.buddy.domain.items.SpendingItem
import com.budget.buddy.domain.mapper.MapperCashCardToUsersBank
import com.budget.buddy.domain.mono.UsersBankDetails
import com.budget.buddy.domain.user.MainUserDataMouth
import com.budget.buddy.domain.user.Time
import com.budget.buddy.presentation.ui.card.CardCashTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Calendar
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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

    private val loadDataMainUserDataMouthUseCase: LoadDataMainUserDataMouthUseCase,

    private val time: WorkTime,
) : ViewModel() {

    private val _userData = MutableStateFlow<UserDataState>(UserDataState.Loading)
    val userData: StateFlow<UserDataState> = _userData.asStateFlow()

    private val _resultUserDataApi = MutableLiveData<MutableList<UsersBankDetails>>()
    val resultUserDataApi: LiveData<MutableList<UsersBankDetails>>
        get() = _resultUserDataApi

    init {
        initialUserData()
    }

    private val _spendingItems = MutableStateFlow<List<SpendingItem>>(emptyList())
    val spendingItems: StateFlow<List<SpendingItem>> = _spendingItems.asStateFlow()

    fun updateSpendingItems(value: List<UsersBankDetails>?) {
        viewModelScope.launch {
            // Map UsersBankDetails to SpendingItem and post value
            _spendingItems.value = value?.map { user ->
                SpendingItem(
                    imageResourceId = R.drawable.ic_launcher_foreground, // You might need to handle resource ID differently
                    reason = user.description,
                    sum = user.amount,
                    time = user.time
                )
            } ?: listOf()
        }
    }

    fun checkingAvailabilityDataFromBank() {
        viewModelScope.launch {
            getAllTransactionsUseCase()?.let { list ->
                val result = list.map {
                    MapperCashCardToUsersBank.cashCardToUsersBank(it)
                }
                _resultUserDataApi.postValue(result.toMutableList())
            } ?: run {
                _resultUserDataApi.postValue(mutableListOf())
            }
        }
    }


    private fun initialUserData() {
        viewModelScope.launch {
            try {
                val data = loadDataMainUserDataMouthUseCase()
                _userData.value = UserDataState.Success(data)
            } catch (e: Exception) {
                _userData.value = UserDataState.Error(e.message ?: "Unknown Error")
            }
        }
    }


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

    fun toSaveTime(): String {
        time.getWorkTime()?.let {
            return it.time
        } ?: run {
            time.setWorkTime(Time(time = toUnixTime))
            return toUnixTime
        }
    }

    private val toUnixTime = (System.currentTimeMillis() / 1000).toString()

    fun getMonoData(
        callbackError: (String) -> Unit = {},
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getUserData(from = testTime(), to = toUnixTime)
                if (response.isSuccessful) {
                    val userData: Array<UsersBankDetails>? = response.body()
                    userData?.let { array ->
                        array.map {
                            addCashTransactionUseCase(
                                CashTransaction(
                                    amount = it.amount / 100.0,
                                    date = it.time,
                                    description = it.description,
                                    type = it.mcc
                                )
                            )
                        }
                        _resultUserDataApi.postValue(array.toMutableList())
                    }
                } else {
                    val error = response.errorBody()?.string().toString()
                    val json = JSONObject(error)
                    if (json.has("errorDescription")) {
                        if (json.get("errorDescription") != ErrorMono.ErrorDescription.error) {
                            callbackError(json.getString("error"))
                        }
                    } else {
                        callbackError(error)
                    }
                }
            } catch (e: Exception) {
                callbackError(e.message.toString())
            }
        }
    }
}