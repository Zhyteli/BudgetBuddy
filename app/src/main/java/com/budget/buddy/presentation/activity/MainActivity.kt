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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.setValue
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
import com.budget.buddy.presentation.ui.additem.BottomSheetComponent
import com.budget.buddy.presentation.ui.additem.EditItem
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
            EditItem()
        }
    }

    @Composable
    private fun MainActivity.MonthlyCheck() {
        val resultInitialUserData by viewModel.userData.collectAsState()
        var dataUpdate by remember { mutableStateOf(true) }

        when (resultInitialUserData) {
            is UserDataState.Loading -> {}
            is UserDataState.Success -> {
                val mainUserDataMouth = (resultInitialUserData as UserDataState.Success).data

                if (mainUserDataMouth != null) {
                    if (dataUpdate) {
                        viewModel.checkingAvailabilityDataFromBank()
                        dataUpdate = false
                    }
                    val resultMono = viewModel.resultUserDataApiLive.observeAsState()

                    // Log the resultMono value directly
                    Log.d("TEST_Check", "Result Mono: ${resultMono.value}")

                    resultMono.value?.let {
                        if (it.isEmpty()) {
                            Log.d("TEST_", "Cash: ${it}")
                            getBankData()
                        }
                        MainSkrin(it, mainUserDataMouth.balance)
                    }
                    viewModel.spendingCounter(mainUserDataMouth)
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
        value: MutableList<SpendingItem>? = null,
        monthlyBudget: Double = 0.0,
        spent: Double = 0.0,
        balance: Double = 0.0,
    ) {
        var showBottomSheet = remember { mutableStateOf(false) }
        val spendingItems by viewModel.spendingItems.collectAsState()
        val spentAll by viewModel.spentAll.observeAsState()
        val balanceAll by viewModel.liveBalance.observeAsState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                CardCashTransaction(
                    monthlyBudget = monthlyBudget, spent = spentAll ?: spent, balance = balanceAll ?: balance
                )
                // Update ViewModel's spending items
                value?.let { viewModel.updateSpendingItems(it) }

                if (spendingItems.isNotEmpty()) {
                    Log.d("TEST_ItemsList", spendingItems.toString())
                    ItemsList(items = spendingItems, onDelete = {
                        viewModel.deleteSpendingItem(it)
                    }, onEdit = {
                        viewModel.editSpendingItem(it)
                    })
                }
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
                            showBottomSheet.value = !showBottomSheet.value
                        },
                    contentDescription = stringResource(R.string.add_new_transaction),
                )
            }
        }

        BottomSheetComponent(
            isVisible = showBottomSheet.value,
            onDismiss = { showBottomSheet.value = false },
            addNewItem = {
                // Use the ViewModel function only to add the new item
                Log.d("Total_", "MainSkrin: $it")
                viewModel.addSpendingItems(it)
                showBottomSheet.value = false
            }
        )

    }

}
