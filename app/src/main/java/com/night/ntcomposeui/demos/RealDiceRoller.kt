package com.night.ntcomposeui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mukesh.MarkDown
import com.night.ntcomposeui.R
import com.night.ntcomposeui.model.Dice
import com.night.ntcomposeui.model.MyTabView


@Composable
fun RealDiceRollerDemo(){
    val ctx = LocalContext.current

    MyTabsView(tabViews = arrayOf(
        MyTabView(
           viewTitle = "PreView",
           ContentView = { RealDiceRollerView() }
        ),
        MyTabView(
            viewTitle = "Code",
            ContentView = { MarkDown(text = ctx.assets.open("realDice.md").readBytes().decodeToString()) }
        )
    ))
}


@Composable
fun RealDiceRollerView(){

    val dice = Dice()
    var rolledNum by remember { mutableStateOf(1) }
    var diceImageId by remember { mutableStateOf(R.drawable.dice_1) }

    fun roll() {
        rolledNum = (1..dice.numSides).random()
        diceImageId = when(rolledNum){
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_1
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(modifier = Modifier.fillMaxWidth(0.6f),painter = painterResource(id = diceImageId), contentDescription = "dice image")
        Button(modifier = Modifier.width(80.dp),onClick = { roll() }) {
            Text(text = "roll")
        }
    }
}