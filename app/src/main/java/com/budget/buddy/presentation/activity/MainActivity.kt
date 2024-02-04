package com.budget.buddy.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.budget.buddy.domain.cash.CashTransaction
import com.budget.buddy.presentation.view.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
        lifecycleScope.launch {
            viewModel.addCashTransactionUseCase(
                CashTransaction(
                    0,
                    0.1,
                    0.6,
                    "USD",
                    "U",
                )
            )
            val sg = viewModel.getTransactionByIdUseCase(1)
            Log.d("TEST_", sg.toString())
        }
    }
}
