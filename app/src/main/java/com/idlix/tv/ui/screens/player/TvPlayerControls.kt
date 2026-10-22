package com.idlix.tv.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.ClosedCaptionDisabled
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.idlix.tv.R
import com.idlix.tv.ui.theme.NetflixDarkCard
import com.idlix.tv.ui.theme.NetflixLightGray
import com.idlix.tv.ui.theme.NetflixRed
import com.idlix.tv.ui.theme.NetflixWhite

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvPlayerControls(
    title: String,
    isVisible: Boolean,
    isPlaying: Boolean,
    currentPosMs: Long,
    durationMs: Long,
    isSubtitleEnabled: Boolean,
    selectedSubtitleLabel: String? = null,
    onPlayPauseClick: () -> Unit,
    onSeekBackward: () -> Unit,
    onSeekForward: () -> Unit,
    onToggleSubtitle: () -> Unit,
    onOpenSubtitlePicker: () -> Unit,
    modifier: Modifier = Modifier,
    playButtonModifier: Modifier = Modifier
) {
    val subtitleFocusRequester = remember { FocusRequester() }
    val internalPlayFocusRequester = remember { FocusRequester() }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xD9000000),
                            Color(0x33000000),
                            Color(0xD9000000)
                        )
                    )
                )
        ) {
            // Top Bar: Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .padding(horizontal = 36.dp, vertical = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = NetflixWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Center Controls: Rewind 10s, Play/Pause, Forward 10s
            Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rewind 10s Button
                Button(
                    onClick = onSeekBackward,
                    colors = ButtonDefaults.colors(
                        containerColor = NetflixDarkCard.copy(alpha = 0.85f),
                        focusedContainerColor = NetflixRed,
                        contentColor = NetflixWhite,
                        focusedContentColor = NetflixWhite
                    ),
                    shape = ButtonDefaults.shape(shape = CircleShape),
                    scale = ButtonDefaults.scale(focusedScale = 1.12f),
                    border = ButtonDefaults.border(
                        focusedBorder = Border(BorderStroke(2.dp, NetflixWhite))
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .size(56.dp)
                        .focusProperties { down = subtitleFocusRequester }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = stringResource(R.string.rewind_10s),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Play / Pause Button
                Button(
                    onClick = onPlayPauseClick,
                    colors = ButtonDefaults.colors(
                        containerColor = NetflixWhite,
                        contentColor = Color.Black,
                        focusedContainerColor = NetflixRed,
                        focusedContentColor = NetflixWhite
                    ),
                    shape = ButtonDefaults.shape(shape = CircleShape),
                    scale = ButtonDefaults.scale(focusedScale = 1.12f),
                    border = ButtonDefaults.border(
                        focusedBorder = Border(BorderStroke(2.dp, NetflixWhite))
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = playButtonModifier
                        .size(72.dp)
                        .focusRequester(internalPlayFocusRequester)
                        .focusProperties { down = subtitleFocusRequester }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            modifier = Modifier
                                .size(36.dp)
                                .then(if (!isPlaying) Modifier.offset(x = 2.dp) else Modifier)
                        )
                    }
                }

                // Forward 10s Button
                Button(
                    onClick = onSeekForward,
                    colors = ButtonDefaults.colors(
                        containerColor = NetflixDarkCard.copy(alpha = 0.85f),
                        focusedContainerColor = NetflixRed,
                        contentColor = NetflixWhite,
                        focusedContentColor = NetflixWhite
                    ),
                    shape = ButtonDefaults.shape(shape = CircleShape),
                    scale = ButtonDefaults.scale(focusedScale = 1.12f),
                    border = ButtonDefaults.border(
                        focusedBorder = Border(BorderStroke(2.dp, NetflixWhite))
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .size(56.dp)
                        .focusProperties { down = subtitleFocusRequester }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = stringResource(R.string.forward_10s),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Bottom Bar: Progress & Subtitle toggle
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 36.dp, vertical = 28.dp)
            ) {
                // Progress Bar
                val progress = if (durationMs > 0) (currentPosMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color(0x4DFFFFFF), RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(6.dp)
                            .background(NetflixRed, RoundedCornerShape(3.dp))
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Timing Info
                    Text(
                        text = "${formatTime(currentPosMs)} / ${formatTime(durationMs)}",
                        color = NetflixLightGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Subtitle Picker Button
                    Button(
                        onClick = onOpenSubtitlePicker,
                        colors = ButtonDefaults.colors(
                            containerColor = if (isSubtitleEnabled) NetflixRed else NetflixDarkCard,
                            contentColor = NetflixWhite,
                            focusedContainerColor = if (isSubtitleEnabled) Color(0xFFB81D24) else NetflixWhite,
                            focusedContentColor = if (isSubtitleEnabled) NetflixWhite else Color.Black
                        ),
                        shape = ButtonDefaults.shape(shape = RoundedCornerShape(8.dp)),
                        scale = ButtonDefaults.scale(focusedScale = 1.08f),
                        border = ButtonDefaults.border(
                            focusedBorder = Border(BorderStroke(2.dp, NetflixWhite))
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier
                            .focusRequester(subtitleFocusRequester)
                            .focusProperties { up = internalPlayFocusRequester }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isSubtitleEnabled) Icons.Default.ClosedCaption else Icons.Default.ClosedCaptionDisabled,
                                contentDescription = stringResource(R.string.subtitles),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            val displayLabel = if (!isSubtitleEnabled) {
                                stringResource(R.string.subtitle_off)
                            } else {
                                selectedSubtitleLabel ?: stringResource(R.string.subtitle_on)
                            }
                            Text(
                                text = "Sub: $displayLabel",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}
