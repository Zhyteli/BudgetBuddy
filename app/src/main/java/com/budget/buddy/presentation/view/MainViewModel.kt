package com.budget.buddy.presentation.view

import androidx.lifecycle.ViewModel
import com.budget.buddy.domain.cash.usecase.AddCashTransactionUseCase
import com.budget.buddy.domain.cash.usecase.DeleteTransactionUseCase
import com.budget.buddy.domain.cash.usecase.DeleteTransactionsByIdsUseCase
import com.budget.buddy.domain.cash.usecase.GetAllTransactionsUseCase
import com.budget.buddy.domain.cash.usecase.GetTransactionByIdUseCase
import com.budget.buddy.domain.cash.usecase.UpdateTransactionUseCase
import com.budget.buddy.domain.cash.usecase.UpdateTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val addCashTransactionUseCase: AddCashTransactionUseCase,
    val deleteTransactionsByIdsUseCase: DeleteTransactionsByIdsUseCase,
    val deleteTransactionUseCase: DeleteTransactionUseCase,
    val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    val updateTransactionsUseCase: UpdateTransactionsUseCase,
    val updateTransactionUseCase: UpdateTransactionUseCase
): ViewModel() {


}