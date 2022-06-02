package com.night.ntcomposeui.component
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController


@Composable
fun MainList(navHostController: NavHostController){

    LazyColumn(content = {
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { navHostController.navigate("/dice") }) {
                    Text(text = "Dice Demo")
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { navHostController.navigate("/real_dice_roller") }) {
                    Text(text = "real_dice_roller")
                }
            }
        }
    })
}

