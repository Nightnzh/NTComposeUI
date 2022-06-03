package com.night.ntcomposeui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.night.ntcomposeui.modal.MyTabView


//@Composable
//fun PreViewAndCodeView(PreView :  @Composable () -> Unit, CodeView :  @Composable () -> Unit){
//
//    var tabIndex by remember { mutableStateOf(0) }
//    val tabTitles = listOf("PreView", "Code")
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        TabRow(selectedTabIndex = tabIndex, tabs = {
//            tabTitles.forEachIndexed { index , item ->
//                Tab(selected = tabIndex == index, onClick = { tabIndex = index}, text = { Text(text = item) })
//            }
//        })
//        Box(modifier = Modifier.fillMaxSize()) {
//            when(tabIndex){
//                0 -> PreView()
//                1 -> CodeView()
//            }
//        }
//    }
//}

@Composable
fun MyTabsView(vararg tabViews : MyTabView){

    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = tabViews.map{ it.viewTitle }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex, tabs = {
            tabTitles.forEachIndexed { index , item ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(text = item) }
                )
            }
        })
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            tabViews[tabIndex].ContentView()
        }
    }
}