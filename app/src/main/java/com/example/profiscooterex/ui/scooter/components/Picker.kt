package com.example.profiscooterex.ui.scooter.components

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.profiscooterex.ui.theme.DarkColor2
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Picker(
    items: List<String>,
    state: PickerState = rememberPickerState(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    startIndex: Int,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = DarkColor2,
    textMetric: String
) {

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemWidthPixels = remember { mutableIntStateOf(0) }

    val itemHeightDp = pixelsToDp(itemHeightPixels.intValue)
    val itemWidthDp = pixelsToDp(itemWidthPixels.intValue)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }



    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }
    LaunchedEffect(startIndex) {
        listState.scrollToItem(startIndex)
    }

    Box(modifier = modifier) {
    Row {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                val currentTextStyle = if (index != listState.firstVisibleItemIndex + visibleItemsMiddle) {
                    textStyle.copy(fontSize = 12.sp)
                } else {
                    textStyle
                }
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = currentTextStyle,
                    modifier = Modifier
                        .onSizeChanged { size ->
                            if (itemHeightPixels.intValue == 0) itemHeightPixels.intValue = size.height
                            if (itemWidthPixels.intValue == 0) itemWidthPixels.intValue = size.width
                        }
                        .offset(x = 4.dp)
                        .then(textModifier)
                )
            }
        }


        Text(
            text = textMetric,
            style = textStyle.copy(fontSize = MaterialTheme.typography.bodySmall.fontSize),
            modifier = Modifier.align(Alignment.CenterVertically).offset(x = itemWidthDp - 65.dp),
            textAlign = TextAlign.Start
        )
    }



        HorizontalDivider(
            modifier = Modifier
                .offset(y = itemHeightDp * visibleItemsMiddle)
                .height(IntrinsicSize.Min),
            color = dividerColor,
            thickness = 2.dp
        )

        HorizontalDivider(
            modifier = Modifier
                .offset(y = itemHeightDp * (visibleItemsMiddle + 1))
                .height(IntrinsicSize.Min),
            color = dividerColor,
            thickness = 2.dp
        )

    }
}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }