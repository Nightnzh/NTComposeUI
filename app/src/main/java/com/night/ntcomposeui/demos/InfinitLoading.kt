package com.night.ntcomposeui.demos

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mukesh.MarkDown
import com.night.ntcomposeui.component.MyTabsView
import com.night.ntcomposeui.modal.MyTabView
import com.night.ntcomposeui.ui.theme.Shapes
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.*


//https://manavtamboli.medium.com/infinite-list-paged-list-in-jetpack-compose-b10fc7e74768

@Composable
fun InfinityLoadingDemo() {

    val ctx = LocalContext.current
    MyTabsView(tabViews = arrayOf(
        MyTabView(
            viewTitle = "PreView",
            ContentView = { InfinityLoading() }
        ),
        MyTabView(
            viewTitle = "Code",
            ContentView = {
                MarkDown(
                    text = ctx.assets.open("infinityLoading.md").readBytes().decodeToString()
                )
            }
        ),
    ))
}





data class FakeLoadingDataModel(
    val name: String,
    val email: String
)

@Composable
fun InfinityLoading() {

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val listItems = remember { mutableStateListOf<FakeLoadingDataModel>() }

    var isLoadingMore by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }



    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {

        item() {

            listState.OnTopScrollToRefresh {
                Log.d("InfinityLoadingDemo", "OnTopScrollToRefresh")
                if(listState.isScrollInProgress){
                    isRefreshing = true
                    scope.launch(Dispatchers.IO) {
                        val fakeData = fakeLoadingData()
                        listItems.clear()
                        listItems.addAll(fakeData)
                    }.invokeOnCompletion { isRefreshing = false }
                }
            }

            if (isRefreshing) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "refreshing...")
                    Box(modifier = Modifier.width(20.dp))
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 4.dp)
                }
            }
        }

        itemsIndexed(items = listItems) { index, item ->
            Card(
                modifier = Modifier.fillMaxWidth().border(border = BorderStroke(1.dp,Color.LightGray)),
                shape = Shapes.medium
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Name: ${item.name}")
                    Text(text = "Email: ${item.email}")
                }
            }
        }

        item {

            if (isLoadingMore) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "loading...")
                    Box(modifier = Modifier.width(40.dp))
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                }
            }

            listState.OnBottomReached {
                isLoadingMore = true
                scope.launch(Dispatchers.IO) {
                    listItems.addAll(fakeLoadingData())
                }.invokeOnCompletion { isLoadingMore = false }
            }
        }
    }


}

@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    //以最後一個item是否可視為判斷是否到底
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    // Convert the state into a cold flow and collect
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
    }
}

@Composable
fun LazyListState.OnTopScrollToRefresh(
    refresh: () -> Unit
) {

    val shouldRefresh = remember {
        derivedStateOf {
            val firstVisibleItem = layoutInfo.visibleItemsInfo.firstOrNull()
                ?: return@derivedStateOf false

            firstVisibleItem.index == 0
        }
    }

    LaunchedEffect(shouldRefresh) {
        snapshotFlow { shouldRefresh.value }
            .collect {
                if (it) refresh()
            }
    }
}



suspend fun fakeLoadingData(dataSize: Int = 5): List<FakeLoadingDataModel> {
    delay((1000..3000).random().toLong())
    val faker = Faker()
    return (1..dataSize).map {
        val name = faker.name.name()
        FakeLoadingDataModel(
            name = name,
            email = faker.internet.email(name = name)
        )
    }
}