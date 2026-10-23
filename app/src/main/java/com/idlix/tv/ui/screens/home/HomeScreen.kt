package com.idlix.tv.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import com.idlix.tv.R
import com.idlix.tv.data.model.MovieItem
import com.idlix.tv.ui.components.TvContinueWatchingRow
import com.idlix.tv.ui.components.TvErrorWithRetry
import com.idlix.tv.ui.components.TvHeroBillboard
import com.idlix.tv.ui.components.TvLoadingSpinner
import com.idlix.tv.ui.components.TvRowSkeleton
import com.idlix.tv.ui.components.TvSwimlaneRow
import com.idlix.tv.ui.theme.TvBackground
import com.idlix.tv.ui.theme.TvBrand

@Composable
fun HomeScreen(
    onNavigateToDetail: (contentType: String, slug: String) -> Unit,
    onNavigateToPlayer: (contentType: String, contentId: String, title: String, poster: String?, backdrop: String?, slug: String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val heroFocusRequester = remember { FocusRequester() }

    // Refresh continue watching list on screen entrance / return
    LaunchedEffect(Unit) {
        viewModel.refreshContinueWatching()
    }

    // Direct initial focus to Hero billboard content as soon as loaded
    LaunchedEffect(uiState.isLoading, uiState.heroMovie) {
        if (!uiState.isLoading && uiState.heroMovie != null) {
            try {
                heroFocusRequester.requestFocus()
            } catch (_: Exception) {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackground)
    ) {
        if (uiState.isLoading) {
            TvRowSkeleton(modifier = Modifier.align(Alignment.Center))
            TvLoadingSpinner(
                color = TvBrand,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (uiState.error != null) {
            TvErrorWithRetry(
                message = uiState.error ?: "",
                onRetry = { viewModel.loadHomeData() }
            )
        } else {
            TvLazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // 1. Dynamic Hero Billboard
                item {
                    TvHeroBillboard(
                        movie = uiState.heroMovie,
                        playButtonModifier = Modifier.focusRequester(heroFocusRequester),
                        onPlayClick = {
                            uiState.heroMovie?.let {
                                if (it.contentType == "tv_series") {
                                    onNavigateToDetail(it.contentType, it.slug)
                                } else {
                                    onNavigateToPlayer(
                                        it.contentType,
                                        it.id,
                                        it.title,
                                        it.fullPosterUrl,
                                        it.fullBackdropUrl,
                                        it.slug
                                    )
                                }
                            }
                        },
                        onDetailsClick = {
                            uiState.heroMovie?.let {
                                onNavigateToDetail(it.contentType, it.slug)
                            }
                        }
                    )
                }

                // 2. Bar Paling Pertama: Lanjutkan Menonton (hanya muncul jika ada histori yang belum kelar)
                if (uiState.continueWatching.isNotEmpty()) {
                    item {
                        TvContinueWatchingRow(
                            title = stringResource(R.string.continue_watching),
                            items = uiState.continueWatching,
                            onItemClick = { item ->
                                onNavigateToPlayer(
                                    item.contentType,
                                    item.contentId,
                                    item.title,
                                    item.posterUrl,
                                    item.backdropUrl,
                                    item.slug
                                )
                            }
                        )
                    }
                }

                // 3. Swimlane: Sedang Tren Sekarang
                item {
                    TvSwimlaneRow(
                        title = stringResource(R.string.trending_now),
                        items = uiState.trending,
                        onItemClick = { onNavigateToDetail(it.contentType, it.slug) }
                    )
                }

                // 4. Swimlane: Film Terpopuler
                item {
                    TvSwimlaneRow(
                        title = stringResource(R.string.top_10_movies),
                        items = uiState.popularMovies,
                        onItemClick = { onNavigateToDetail(it.contentType, it.slug) }
                    )
                }

                // 5. Swimlane: Serial TV Pilihan
                item {
                    val seriesAsMovies = remember(uiState.popularSeries) {
                        uiState.popularSeries.map { s ->
                            MovieItem(
                                id = s.id,
                                title = s.title,
                                slug = s.slug,
                                posterPath = s.posterPath,
                                backdropPath = s.backdropPath,
                                voteAverage = s.voteAverage,
                                contentType = "tv_series"
                            )
                        }
                    }
                    TvSwimlaneRow(
                        title = stringResource(R.string.popular_series),
                        items = seriesAsMovies,
                        onItemClick = { onNavigateToDetail(it.contentType, it.slug) }
                    )
                }

                // 6. Swimlane: Film Aksi
                if (uiState.actionMovies.isNotEmpty()) {
                    item {
                        TvSwimlaneRow(
                            title = stringResource(R.string.movies_action),
                            items = uiState.actionMovies,
                            onItemClick = { onNavigateToDetail(it.contentType, it.slug) }
                        )
                    }
                }
            }
        }
    }
}
