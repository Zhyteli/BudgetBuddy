package com.budget.buddy.presentation.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.budget.buddy.domain.mapper.MapperCategoriesItemForNumber
import com.budget.buddy.domain.mono.UsersBankDetails
import com.budget.buddy.domain.user.MainUserDataMouth
import com.budget.buddy.domain.user.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
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

    private val loadDataMainUserDataMouthUseCase: LoadDataMainUserDataMouthUseCase,

    private val time: WorkTime,
) : ViewModel() {

    private val _userData = MutableStateFlow<UserDataState>(UserDataState.Loading)
    val userData: StateFlow<UserDataState> = _userData.asStateFlow()

    private val _resultUserDataApiLive = MutableLiveData<MutableList<SpendingItem>>(mutableListOf())

    //    private val resultUserDataApi: MutableList<SpendingItem> = mutableListOf()
    val resultUserDataApiLive: LiveData<MutableList<SpendingItem>>
        get() = _resultUserDataApiLive

    private val _liveBalance = MutableLiveData<Double>()
    val liveBalance: LiveData<Double>
        get() = _liveBalance

    private val _spentAll = MutableLiveData<Double>()
    val spentAll: LiveData<Double>
        get() = _spentAll

    init {
        initialUserData()
    }

    private val _spendingItems = MutableStateFlow<MutableList<SpendingItem>>(mutableListOf())
    val spendingItems: StateFlow<List<SpendingItem>> = _spendingItems.asStateFlow()

    fun updateSpendingItems(value: MutableList<SpendingItem>?) {
        viewModelScope.launch {
            // Map UsersBankDetails to SpendingItem and post value
            _spendingItems.value = value ?: mutableListOf()
        }
    }

    fun addSpendingItems(item: SpendingItem) {
        viewModelScope.launch {
            val d = MapperCategoriesItemForNumber.mapOfNumber(item.imageResourceId)
            saveAddCashTransactionUseCase(
                CashTransaction(
                    id = item.id,
                    amount = item.sum,
                    date = item.time,
                    description = item.reason,
                    type = d,
                    descriptionFull = item.description,
                    transaction = item.transaction
                )
            )

            // Add the item to the beginning of the list and update the state
            val currentList = _spendingItems.value.toMutableList()
            currentList.add(0, item)
            _spendingItems.value = currentList

            // If _resultUserDataApiLive is needed to be updated
            _resultUserDataApiLive.value = currentList
            spendingCounter(loadDataMainUserDataMouthUseCase() ?: MainUserDataMouth())
        }
    }


    fun checkingAvailabilityDataFromBank() {
        viewModelScope.launch {
            getAllTransactionsUseCase()?.let { list ->
                val updatedList = list.map {
                    SpendingItem(
                        id = it.id,
                        imageResourceId = MapperCategoriesItemForNumber.mapOfCategoriesItem(it.type).icons,
                        reason = it.description ?: "null",
                        sum = it.amount,
                        time = it.date,
                        description = it.descriptionFull ?: "null",
                        transaction = it.amount > 0
                    )
                }.sortedByDescending { it.time }.toMutableList()

                // Log the size of the updated list
                Log.d("TEST_ViewModel", "Updated List Size: ${updatedList.size}")

                _spendingItems.value = updatedList
                _resultUserDataApiLive.value = updatedList
            } ?: run {
                _resultUserDataApiLive.postValue(mutableListOf())
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
        calendarFrom.set(Calendar.MONTH, Calendar.MAY)
        calendarFrom.set(Calendar.DAY_OF_MONTH, 1)
        calendarFrom.set(Calendar.HOUR_OF_DAY, 0)
        calendarFrom.set(Calendar.MINUTE, 0)
        calendarFrom.set(Calendar.SECOND, 0)
        calendarFrom.set(Calendar.MILLISECOND, 0)
        return (calendarFrom.timeInMillis / 1000).toString()
    }

    fun testTime2(): String {
        val calendarFrom = Calendar.getInstance()
        calendarFrom.set(Calendar.MONTH, Calendar.APRIL)
        calendarFrom.set(Calendar.DAY_OF_MONTH, 30)
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
                        val uniqueTransactions = array.distinctBy { it.id }

                        uniqueTransactions.forEach { bankTransaction ->
                            // Check if the transaction already exists in the database
                            val existingTransaction = getTransactionByIdUseCase(bankTransaction.id)

                            if (existingTransaction == null) {
                                // Transaction doesn't exist, save it
                                val transaction = CashTransaction(
                                    amount = bankTransaction.amount / 100.0,
                                    date = bankTransaction.time,
                                    description = bankTransaction.description,
                                    descriptionFull = bankTransaction.description,
                                    type = bankTransaction.mcc / 100,
                                    transaction = (bankTransaction.amount / 100.0) > 0
                                )
                                Log.d("TEST_FUL_TR", "Transaction: $bankTransaction")
                                saveAddCashTransactionUseCase(transaction)
//                                spendingCounter(loadDataMainUserDataMouthUseCase() ?: MainUserDataMouth())
                            }
                        }
                        checkingAvailabilityDataFromBank()
                    }
                } else {
                    val error = response.errorBody()?.string().toString()
                    val json = JSONObject(error)
                    if (json.has("errorDescription")) {
                        if (json.getString("errorDescription") != ErrorMono.ErrorDescription.error) {
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


    private suspend fun MainViewModel.saveAddCashTransactionUseCase(transaction: CashTransaction) {
        getAllTransactionsUseCase()?.let { all ->
            if (all.all { it.date != transaction.date && it.id != transaction.id }) {
                addCashTransactionUseCase(
                    transaction
                )
            } else {
                deleteTransactionUseCase(transaction)
            }
        }
    }

    fun spendingCounter(mainUserDataMouth: MainUserDataMouth) {
        viewModelScope.launch {
            delay(1500)
            val transactions = getAllTransactionsUseCase() ?: emptyList()
            // Calculate spent amount only on transactions with negative amounts and not marked as 'transaction'
            val totalSpent: Double =
                transactions.filter { !it.transaction }.sumOf { it.amount }
            Log.d("Total_Spent", totalSpent.toString())
            val totalIncome: Double =
                transactions.filter { it.transaction }.sumOf { it.amount }
            Log.d("Total_Income", totalIncome.toString())
            val total = totalIncome + totalSpent

            // Create a NumberFormat for your locale (e.g., Germany)
            val numberFormat = NumberFormat.getInstance(Locale.GERMANY)
            numberFormat.maximumFractionDigits = 2

            // Format values with commas as decimal separators
            val formattedTotalSpent = numberFormat.format(total)

            // Log formatted amounts for debugging
            Log.d("Formatted Total Spent", formattedTotalSpent)

            // Parse formatted values back to Double
            val parsedTotalSpent = numberFormat.parse(formattedTotalSpent)?.toDouble() ?: 0.0

            // Update live data values
            _spentAll.value = parsedTotalSpent
            val allLive = mainUserDataMouth.balance + parsedTotalSpent
            val formattedAllLive = numberFormat.format(allLive)
            val parsedAllLive = numberFormat.parse(formattedAllLive)?.toDouble() ?: 0.0
            _liveBalance.value = parsedAllLive
        }
    }

    fun deleteSpendingItem(it: SpendingItem) {
        viewModelScope.launch {
            deleteTransactionUseCase(
                CashTransaction(
                    id = it.id,
                    amount = it.sum,
                    date = it.time,
                    description = it.reason,
                    type = MapperCategoriesItemForNumber.mapOfNumber(it.imageResourceId),
                    descriptionFull = it.description,
                    transaction = it.transaction
                )
            )

            // Remove the item from the list and update the state
            val currentList = _spendingItems.value.toMutableList()
            currentList.remove(it)
            _spendingItems.value = currentList

            // If _resultUserDataApiLive is needed to be updated
            _resultUserDataApiLive.value = currentList
            spendingCounter(loadDataMainUserDataMouthUseCase() ?: MainUserDataMouth())
        }
    }

    fun editSpendingItem(it: SpendingItem) {
        viewModelScope.launch {
            // Update the item in the list and update the state
            getTransactionByIdUseCase(it.id.toString())?.let {
                updateTransactionUseCase(
                    CashTransaction(
                        id = it.id,
                        amount = it.amount,
                        date = it.date,
                        description = it.description,
                        type = it.type,
                        descriptionFull = it.descriptionFull,
                        transaction = it.transaction
                    )
                )
            }
            checkingAvailabilityDataFromBank()
            spendingCounter(loadDataMainUserDataMouthUseCase() ?: MainUserDataMouth())
        }
    }
}