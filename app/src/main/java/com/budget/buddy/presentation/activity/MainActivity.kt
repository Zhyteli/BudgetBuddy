package com.budget.buddy.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.budget.buddy.R
import com.budget.buddy.data.database.getdata.UserDataState
import com.budget.buddy.data.impl.work.SumAllCategories
import com.budget.buddy.domain.items.SpendingItem
import com.budget.buddy.domain.user.MainUserDataMouth
import com.budget.buddy.presentation.ui.additem.BottomSheetComponent
import com.budget.buddy.presentation.ui.additem.EditItem
import com.budget.buddy.presentation.ui.card.CardCashTransaction
import com.budget.buddy.presentation.ui.list.ItemsList
import com.budget.buddy.presentation.ui.menu.analysis.AnalysisCard
import com.budget.buddy.presentation.ui.menu.history.HistoryCard
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(R.color.background))
            ) {
                MonthlyCheck()
            }
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
                        MainSkrin(it, mainUserDataMouth, resultMono = resultMono)
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

    @Composable
    private fun MainSkrin(
        value: MutableList<SpendingItem>? = null,
        monthlyBudget: MainUserDataMouth = MainUserDataMouth(),
        resultMono: State<MutableList<SpendingItem>?>,
        spent: Double = 0.0,
        balance: Double = 0.0,
    ) {
        var showBottomSheet = remember { mutableStateOf(false) }
        val spendingItems by viewModel.spendingItems.collectAsState()
        val spentAll by viewModel.spentAll.observeAsState()
        val balanceAll by viewModel.liveBalance.observeAsState()
        var editShow = remember { mutableStateOf(false) }
        var historyShow = remember { mutableStateOf(false) }
        var analysisShow = remember { mutableStateOf(false) }
        val newSpendingItem = remember { mutableStateOf(SpendingItem()) }
        var showMenu = remember { mutableStateOf(false) } // State to control menu visibility
        val analysisAi by viewModel.livePromt.observeAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(R.color.surface)),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), // Ensure the Row uses the full width
                        horizontalArrangement = Arrangement.SpaceBetween // Arrange children to start and end
                    ) {
                        Text(
                            text = monthlyBudget.name,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(10.dp),
                            color = Color.White,
                            fontFamily = FontFamily(
                                Font(R.font.open)
                            )
                        )
                        // IconButton used for triggering the menu
                        IconButton(
                            onClick = { showMenu.value = true }
                        ) {
                            Icon(
                                tint = Color.White,
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(10.dp)
                            )
                        }
                        // Adjusted DropdownMenu
                        DropdownMenu(
                            expanded = showMenu.value,
                            onDismissRequest = { showMenu.value = false },
                            offset = DpOffset(
                                (-85).dp,
                                0.dp
                            ),  // Adjust the X offset to align the menu to the right
                            modifier = Modifier.wrapContentSize(Alignment.TopEnd) // Align the menu contents to the end
                        ) {
                            DropdownMenuItem(
                                text = { Text("History and analytics") },
                                onClick = {
                                    showMenu.value = false
                                    historyShow.value = !historyShow.value
                                })
                            DropdownMenuItem(
                                text = { Text("Recommendations") },
                                onClick = {
                                    showMenu.value = false
                                    analysisShow.value = !analysisShow.value
                                    viewModel.ai(application = application)
                                }
                            )
                        }
                    }
                }
                CardCashTransaction(
                    monthlyBudget = monthlyBudget.balance,
                    spent = spentAll ?: spent,
                    balance = balanceAll ?: balance
                )
                // Update ViewModel's spending items
                value?.let { viewModel.updateSpendingItems(it) }

                if (spendingItems.isNotEmpty()) {
                    Log.d("TEST_ItemsList", spendingItems.toString())
                    ItemsList(items = spendingItems, onDelete = {
                        viewModel.deleteSpendingItem(it)
                    }, onEdit = {
                        newSpendingItem.value = it
                        editShow.value = !editShow.value
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
                Box(Modifier.background(Color.DarkGray)) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(10.dp)
                            .clickable {
                                showBottomSheet.value = !showBottomSheet.value
                            },
                        tint = Color.White
                    )

                }
            }
        }

        BottomSheetComponent(
            isVisible = showBottomSheet.value,
            onDismiss = { showBottomSheet.value = false },
            addNewItem = {
                // Use the ViewModel function only to add the new item
                viewModel.addSpendingItems(it)
                showBottomSheet.value = false
            }
        )
        EditItem(editShow.value, item = newSpendingItem, onDismiss = { editShow.value = false },
            addNewItem = {
                viewModel.editSpendingItem(it)
                editShow.value = false
            })

        resultMono.value?.let {
            HistoryCard(
                historyShow.value, onDismiss = { historyShow.value = false },
                it
            )
        }
        AnalysisCard(analysisShow.value, onDismiss = { analysisShow.value = false }, analysisAi)
    }

}
