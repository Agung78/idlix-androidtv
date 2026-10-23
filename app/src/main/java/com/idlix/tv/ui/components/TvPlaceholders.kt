package com.idlix.tv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.idlix.tv.R
import com.idlix.tv.ui.theme.TvBrand
import com.idlix.tv.ui.theme.TvMuted
import com.idlix.tv.ui.theme.TvOnSurface
import com.idlix.tv.ui.theme.TvSurface

@Composable
fun TvSkeletonBox(
    modifier: Modifier = Modifier,
    width: Dp = 135.dp,
    height: Dp = 200.dp
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .background(TvSurface, RoundedCornerShape(8.dp))
    )
}

@Composable
fun TvRowSkeleton(
    modifier: Modifier = Modifier,
    count: Int = 6
) {
    Column(modifier = modifier.padding(vertical = 14.dp)) {
        Box(
            modifier = Modifier
                .padding(start = 48.dp, bottom = 10.dp)
                .width(180.dp)
                .height(20.dp)
                .background(TvSurface, RoundedCornerShape(4.dp))
        )
        androidx.tv.foundation.lazy.list.TvLazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 48.dp)
        ) {
            items(count) { TvSkeletonBox() }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvErrorWithRetry(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val retryRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        try { retryRequester.requestFocus() } catch (_: Exception) {}
    }
    Column(
        modifier = modifier.fillMaxSize().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = TvMuted,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier.focusRequester(retryRequester),
            colors = ButtonDefaults.colors(
                containerColor = TvBrand,
                contentColor = TvOnSurface,
                focusedContainerColor = TvBrand,
                focusedContentColor = TvOnSurface
            ),
            shape = ButtonDefaults.shape(shape = RoundedCornerShape(6.dp))
        ) {
            Text(text = stringResource(R.string.try_again), fontWeight = FontWeight.Bold)
        }
    }
}

