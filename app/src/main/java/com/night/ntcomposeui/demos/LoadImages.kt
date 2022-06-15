package com.night.ntcomposeui.demos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mukesh.MarkDown
import com.night.ntcomposeui.component.MyTabsView
import com.night.ntcomposeui.model.MyTabView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL


@Composable
fun LoadImagesDemo() {

    val ctx = LocalContext.current
    MyTabsView(tabViews = arrayOf(
        MyTabView(
            viewTitle = "PreView",
            ContentView = { LoadImagesView() }
        ),
        MyTabView(
            viewTitle = "Code",
            ContentView = {
                MarkDown(
                    text = ctx.assets.open("loadImages.md").readBytes().decodeToString()
                )
            }
        ),
    ))
}

//define data class for this demo
data class IItem(
    val id: String,
    val img_src: String
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoadImagesView() {

    val imagesUrl = "https://android-kotlin-fun-mars-server.appspot.com/photos"
    val imageList = remember { mutableStateListOf<IItem>() }
    val cScope = rememberCoroutineScope()
    var isLoadedUrlData by remember { mutableStateOf(false) }

    if (!isLoadedUrlData) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        cells = GridCells.Fixed(2),
        content = {

            itemsIndexed(items = imageList) { index: Int, item: IItem ->
                var isLoading by remember {
                    mutableStateOf(false)
                }

                Box(
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.LightGray))
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.img_src)
                            .crossfade(true)
                            .build(),
                        contentDescription = index.toString(),
                        onLoading = { isLoading = true },
                        onError = { isLoading = false },
                        onSuccess = { isLoading = false },
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop

                    )
                    if (isLoading)
                        CircularProgressIndicator()
                }
            }
        }
    )

    //Loading data form url
    LaunchedEffect(keys = arrayOf(1), block = {
        cScope.launch(Dispatchers.IO) {
            try {
                val json = URL(imagesUrl).readText()
                val tempList =
                    Gson().fromJson<List<IItem>>(json, object : TypeToken<List<IItem>>() {}.type)
                imageList.addAll(tempList)
            } catch (e: Error) {

            }
        }.invokeOnCompletion {
            isLoadedUrlData = true
        }
    })

}


