package com.budget.buddy.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

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
                    viewModel.checkingAvailabilityDataFromBank()
                    val resultMono = viewModel.resultUserDataApi.observeAsState()
//                    if (resultMono.value != null && resultMono.value!!.isEmpty()) {
//                        getBankData()
//                    }
                    Log.d("TEST_", "Cash: ${resultMono.value}")
                    MainSkrin(resultMono.value, mainUserDataMouth.balance)
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

    @Composable
    private fun getBankData() {
        viewModel.getMonoData(callbackError = {
            lifecycleScope.launch(Dispatchers.Main) {
                Log.d("TEST_", "onCreate: ${it}")
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    @Preview
    @Composable
    private fun MainSkrin(
        value: MutableList<UsersBankDetails>? = null,
        monthlyBudget: Double = 0.0,
        spent: Double = 0.0,
        balance: Double = 0.0,
    ) {
        val spendingItems by viewModel.spendingItems.collectAsState()
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
                viewModel.updateSpendingItems(value)

//                if (spendingItems.size == 0){
//                    Text(text = "No items")
//                }else{
                Log.d("TEST_", spendingItems.toString())
                ItemsList(items = spendingItems)
//                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize(),
                shape = RoundedCornerShape(7.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(10.dp)
                        .clickable {
                            value?.add(UsersBankDetails(
                                description = "TEST",
                                amount = 1000.0,
                                time = 412565652
                            ))
                            viewModel.updateSpendingItems(value)
                        },
                    contentDescription = stringResource(R.string.add_new_transaction),
                )
            }
        }
    }
}
