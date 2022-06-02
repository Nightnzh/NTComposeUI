package com.night.ntcomposeui.component
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.night.ntcomposeui.config.demoList
import com.night.ntcomposeui.modal.DemoItem


@Composable
fun MainList(navHostController: NavHostController){

    LazyColumn(content = {
        itemsIndexed(demoList){ index: Int, item: DemoItem ->
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { navHostController.navigate(item.routeName) }) {
                    Text(text = item.title)
                }
            }
        }
    })
}

