package com.night.ntcomposeui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.night.ntcomposeui.MainViewModel
import com.night.ntcomposeui.model.Demo

//Demo列表導覽
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainList(navHostController: NavHostController, viewModel: MainViewModel) {

    val basicDemoItemModifier = Modifier
        .fillMaxWidth()
        .border(
            border = BorderStroke(1.dp, Color.DarkGray),
            MaterialTheme.shapes.medium
        )
        .padding(8.dp, 12.dp)

    val demoList = viewModel.demoList
    val webDemoList = viewModel.webDemoList

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {

            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
                ) {
                    Text(text = "Android Native")
                }
                Divider()
            }
            itemsIndexed(demoList) { index: Int, item: Demo ->
                Row(
                    modifier =
                    basicDemoItemModifier
                        .pointerInput(Unit) {
                            detectTapGestures {
                                navHostController.navigate(item.routeName)
                            }

                        }) {
                    Text(text = "${index + 1}. ")
                    Text(text = item.title)
                }
            }


            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
                ) {
                    Text(text = "Web")
                }
                Divider()
            }

            itemsIndexed(webDemoList) { index: Int, item: Demo ->
                Row(
                    modifier =
                    basicDemoItemModifier
                        .pointerInput(Unit) {
                            detectTapGestures {
                                navHostController.navigate(item.routeName)
                            }

                        }) {
                    Text(text = "${index + 1}. ")
                    Text(text = item.title)
                }
            }


        })
}

