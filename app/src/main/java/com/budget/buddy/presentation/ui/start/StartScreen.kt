package com.budget.buddy.presentation.ui.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.budget.buddy.R
import com.budget.buddy.domain.user.MainUserDataMouth

@Preview
@Composable
fun StartScreen(saveDataUser: (MainUserDataMouth) -> Unit = {}) {
    var showDialog by remember { mutableStateOf(false) }
    val ukr = stringResource(R.string.ukrainian_hryvnia)
    val currency = remember { mutableStateOf(ukr) }
    val currencyList = listOf(
        stringResource(R.string.ukrainian_hryvnia),
        stringResource(R.string.us_dollar),
        stringResource(R.string.euro),
        stringResource(R.string.british_pound)
    )
    val moun = stringResource(R.string.monthly_balance)
    val name = remember { mutableStateOf(moun) }
    val textAmountOfMoney = remember { mutableStateOf("") }

    val color = remember {
        mutableStateOf(Color.White)
    }

    if (showDialog) {
        CurrencyDialog(
            currencyList = currencyList,
            currencyU = currency,
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.new_budget),
                    fontSize = 30.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Button(
                    onClick = {
                        if (textAmountOfMoney.value != "") {
                            saveDataUser(
                                MainUserDataMouth(
                                    name = name.value,
                                    currency = currency.value,
                                    balance = textAmountOfMoney.value.toDouble(),
                                )
                            )
                        } else {
                            color.value = Color.Red
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .padding(end = 10.dp)
                ) {
                    Text(text = stringResource(R.string.save_data))
                }
            }
            Text(
                text = stringResource(R.string.description_of_the_beginning),
                modifier = Modifier.padding(horizontal = 10.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextEdit(text = name)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.currency),
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = currency.value,
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier.clickable { showDialog = true }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    TextEdit(textAmountOfMoney, R.string.amount_of_money, color.value, true)
                }
            }
            Image(
                painter = painterResource(id = R.drawable.start_cop),
                contentDescription = "start pig",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
    }
}

@Preview
@Composable
fun CurrencyDialog(
    currencyList: List<String> = listOf(
        "Ukrainian hryvnia",
        "US Dollar",
        "Euro",
        "British Pound"
    ),
    currencyU: MutableState<String> = mutableStateOf("Ukrainian hryvnia"),
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Customize your dialog appearance
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                currencyList.forEach { currency ->
                    Text(
                        text = currency,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                currencyU.value = currency
                                onDismissRequest()
                            }
                            .padding(8.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TextEdit(
    text: MutableState<String> = mutableStateOf(""),
    textLabel: Int = R.string.name,
    color: Color = Color.DarkGray,
    numberBord: Boolean = false,
) {
    BasicTextField(
        value = text.value,
        onValueChange = { text.value = it },
        decorationBox = { innerTextField ->
            if (text.value.isEmpty()) {
                Text(
                    stringResource(textLabel),
                    color = color,
                    fontSize = 20.sp
                )
            }
            innerTextField()
        },
        singleLine = true,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (numberBord) KeyboardType.Number else KeyboardType.Text
        )

    )
}