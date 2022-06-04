package com.night.ntcomposeui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.mukesh.MarkDown
import com.night.ntcomposeui.modal.Dice
import com.night.ntcomposeui.modal.MyTabView


@Composable
fun DiceDemo() {
    val ctx = LocalContext.current

    MyTabsView(tabViews = arrayOf(
        MyTabView(
            viewTitle = "PreView",
            ContentView = { DiceView() }
        ),
        MyTabView(
            viewTitle = "Code",
            ContentView = { MarkDown(text = ctx.assets.open("diceMd.md").readBytes().decodeToString()) }
        )
    ))
}

@Composable
fun DiceView() {

    var inputCount by remember { mutableStateOf<String>("") }

    val diceList = remember { mutableStateListOf<Dice>(Dice(6)) }

    fun addDice() {
        val newDice = if( inputCount.isEmpty() || inputCount.toInt() == 0 ) Dice() else Dice(inputCount.toInt())
        diceList.add(newDice)
    }

    Column() {
        Column() {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputCount, onValueChange = { inputCount = it },
                label = { Text(text = "Dice side count") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { addDice() }
            ) {
                Text(text = "Add Dice")
            }
        }
        LazyColumn() {

            itemsIndexed(items = diceList.toTypedArray()) { index: Int, item: Dice ->

                var rollNumber by remember {
                    mutableStateOf<Int?>(null)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Dice(${item.numSides})")
                    Text(text = "result: (${rollNumber})")
                    Button(onClick = { rollNumber = item.roll() }) {
                        Text(text = "roll")
                    }
                }
            }
        }
    }
}

