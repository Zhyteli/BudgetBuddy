package com.budget.buddy.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.budget.buddy.R
import com.budget.buddy.data.database.getdata.UserDataState
import com.budget.buddy.domain.items.SpendingItem
import com.budget.buddy.domain.mono.UsersBankDetails
import com.budget.buddy.presentation.ui.card.CardCashTransaction
import com.budget.buddy.presentation.ui.list.ItemsList
import com.budget.buddy.presentation.view.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonthlyCheck()
        }
    }

    @Composable
    private fun MainActivity.MonthlyCheck() {
        val resultInitialUserData by viewModel.userData.collectAsState()

        when (resultInitialUserData) {
            is UserDataState.Loading -> {}
            is UserDataState.Success -> {
                val mainUserDataMouth = (resultInitialUserData as UserDataState.Success).data
                if (mainUserDataMouth != null) {
                    val resultMono = viewModel.resultUserDataApi.observeAsState()
                    MainSkrin(resultMono.value, mainUserDataMouth.balance)
                    viewModel.getMonoData(callbackError = {
                        lifecycleScope.launch(Dispatchers.Main) {
                            Log.d("TEST_", "onCreate: ${it}")
                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                        }
                    })
                } else {
                    startActivity(Intent(this, StartActivity::class.java))
                    finish()
                }
            }

            is UserDataState.Error -> {
                val errorMessage = (resultInitialUserData as UserDataState.Error).message
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    @Preview
    @Composable
    private fun MainSkrin(
        value: List<UsersBankDetails>? = null,
        monthlyBudget: Double = 0.0,
        spent: Double = 0.0,
        balance: Double = 0.0,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                CardCashTransaction(
                    monthlyBudget = monthlyBudget, spent = spent, balance = balance
                )
                // Преобразование списка UsersBankDetails в список SpendingItem
                val spendingItems = value?.map { user ->
                    SpendingItem(
                        imageResourceId = R.drawable.ic_launcher_foreground,
                        reason = user.description,
                        sum = user.amount / 100.0
                    )
                } ?: listOf()

//                if (spendingItems.size == 0){
//                    Text(text = "No items")
//                }else{
                ItemsList(items = spendingItems)
//                }
            }
        }
    }
}
