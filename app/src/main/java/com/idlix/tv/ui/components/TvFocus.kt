package com.idlix.tv.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.idlix.tv.ui.theme.TvOnSurface

/** Shared focused treatment: 1.1 scale, 2dp white border, default glow. */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun tvCardScale() = CardDefaults.scale(focusedScale = 1.1f)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun tvCardBorder() = CardDefaults.border(
    focusedBorder = Border(border = BorderStroke(2.dp, TvOnSurface))
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun tvCardGlow() = CardDefaults.glow()

