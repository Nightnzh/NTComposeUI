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

class Dice(val count: Int) {
    fun roll() = (1..count).random()
}

@Composable
fun DiceDemo(){
    val ctx = LocalContext.current
    PreViewAndCodeView(PreView = { DiceView() }, CodeView = {
        MarkDown(
            text = ctx.assets.open("diceMd.md").readBytes().decodeToString()
        )
    })
}

@Composable
fun DiceView() {

    var inputCount by remember { mutableStateOf<String>("") }

    val diceList = remember { mutableStateListOf<Dice>(Dice(4)) }

    fun addDice() {
        val newDice = Dice(inputCount.toInt())
        diceList.add(newDice)
    }

    Column() {
        Row(verticalAlignment = Alignment.CenterVertically,) {
            OutlinedTextField(
                value = inputCount, onValueChange = { inputCount = it },
                label = { Text(text = "Dice count") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(onClick = {
                addDice()

            }) {
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
                    Text(text = "Dice(${item.count})")
                    Text(text = "rolled: (${rollNumber})")
                    Button(onClick = { rollNumber = item.roll() }) {
                        Text(text = "roll")
                    }
                }
            }
        }
    }
}

