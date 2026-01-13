package com.example.playlist.ui.activity.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist.R
import com.example.playlist.ui.item.TrackListItem
import com.example.playlist.ui.viewModel.PlaylistViewModel
import com.example.playlist.ui.viewModel.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier,
    searchViewModel: SearchViewModel,
    playlistViewModel: PlaylistViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToTrackDetails: () -> Unit = {}
) {
    val screenState by searchViewModel.searchScreenState.collectAsState()
    val history by searchViewModel.history.collectAsState()
    val currentQuery by searchViewModel.currentQuery.collectAsState()

    var query by remember { mutableStateOf(currentQuery) }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(currentQuery) {
        query = currentQuery
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .size(16.dp)
                    .clickable {
                        searchViewModel.resetAll()
                        onNavigateBack()
                    }
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = stringResource(R.string.search_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val showHistory = query.isEmpty() && history.isNotEmpty() && isFocused

        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFE6E8EB),
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = if (showHistory) 0.dp else 8.dp,
                            bottomEnd = if (showHistory) 0.dp else 8.dp
                        )
                    )
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_title),
                        tint = Color(0xFFAEAFB4),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                searchViewModel.setCurrentQuery(query)
                                searchViewModel.search(query)
                            }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        BasicTextField(
                            value = query,
                            onValueChange = {
                                query = it
                                searchViewModel.setCurrentQuery(it)
                            },
                            singleLine = true,
                            cursorBrush = SolidColor(Color.Black),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                color = Color(0xFF1A1B22)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart)
                                .onFocusChanged { isFocused = it.isFocused }
                        ) { innerTextField ->
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (query.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.search_title),
                                        color = Color(0xFFAEAFB4),
                                        fontSize = 16.sp,
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }

                    if (query.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear),
                            tint = Color(0xFFAEAFB4),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    query = ""
                                    searchViewModel.setCurrentQuery("")
                                    searchViewModel.resetSearch()
                                }
                        )
                    }
                }
            }

            if (showHistory) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFE6E8EB),
                            shape = RoundedCornerShape(
                                bottomStart = 8.dp,
                                bottomEnd = 8.dp
                            )
                        )
                ) {
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFAEAFB4))
                    history.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    query = item
                                    searchViewModel.setCurrentQuery(item)
                                    searchViewModel.search(item)
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_clock),
                                contentDescription = null,
                                tint = Color(0xFFAEAFB4),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item,
                                fontSize = 16.sp,
                                color = Color(0xFF1A1B22)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (screenState) {
            is SearchState.Initial -> {
                if (query.isEmpty() && history.isEmpty()) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.initial_search_text),
                            color = Color(0xFF1A1B22)
                        )
                    }
                }
            }
            is SearchState.Searching -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.searching_text), color = Color(0xFF1A1B22))
                }
            }
            is SearchState.Success -> {
                val tracks = (screenState as SearchState.Success).list
                if (tracks.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.empty_search),
                                contentDescription = stringResource(R.string.title_empty),
                                modifier = Modifier.size(120.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.title_empty),
                                fontSize = 16.sp,
                                color = Color(0xFF1A1B22)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tracks.size) { index ->
                            TrackListItem(
                                track = tracks[index],
                                onClick = {
                                    playlistViewModel.setSelectedTrack(tracks[index])
                                    onNavigateToTrackDetails()
                                }
                            )
                        }
                    }
                }
            }
            is SearchState.Fail -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_internet),
                            contentDescription = stringResource(R.string.no_internet),
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(19.dp))
                        Text(
                            text = stringResource(R.string.no_internet),
                            color = Color(0xFF1A1B22)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.fail_load_text),
                            color = Color(0xFF1A1B22),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
