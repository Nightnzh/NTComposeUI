package com.night.ntcomposeui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.night.ntcomposeui.config.demoList
import com.night.ntcomposeui.model.DemoItem

//Demo列表導覽
@Composable
fun MainList(navHostController: NavHostController) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            itemsIndexed(demoList) { index: Int, item: DemoItem ->


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(1.dp, Color.DarkGray),
                            MaterialTheme.shapes.medium
                        )
                        .padding(8.dp, 12.dp)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                navHostController.navigate(item.routeName, navOptions {
                                    anim {
                                        this.popEnter
                                    }

                                })
                            }

                        }) {

                    Text(text = "${index + 1}. ")
                    Text(text = item.title)
                    Spacer(modifier = Modifier.weight(1f))

                }
            }
        })
}

