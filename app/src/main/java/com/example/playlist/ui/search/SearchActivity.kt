package com.example.playlist.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.playlist.R
import com.example.playlist.ui.item.TrackListItem

@Composable
fun SearchScreen(modifier: Modifier, viewModel: SearchViewModel, onNavigateBack: () -> Unit) {
    val screenState by viewModel.searchScreenState.collectAsState()
    var query by remember { mutableStateOf("") }

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
                contentDescription = "Назад",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onNavigateBack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Поиск",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Поиск", color = Color(0xFFAEAFB4)) },
            singleLine = true,
            leadingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        viewModel.search(query)
                    },
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = Color(0xFFAEAFB4)
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Очистить",
                        tint = Color(0xFFAEAFB4),
                        modifier = Modifier.clickable { query = "" }
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF1A1B22),
                fontSize = 16.sp
            )
        )
        when (screenState) {
            is SearchState.Initial -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Воспользуйтесь поиском")
                }
            }

            is SearchState.Searching -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ищем...")
                }
            }

            is SearchState.Success -> {
                val tracks = (screenState as SearchState.Success).list
                if (tracks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
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
                                color = Color(0xFF7A7C81)
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
                            TrackListItem(track = tracks[index])
                            HorizontalDivider(thickness = 0.5.dp)
                        }
                    }
                }
            }

            is SearchState.Fail -> {
                val error = (screenState as SearchState.Fail).error
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ошибка: $error", color = Color.Red)
                }
            }
        }
    }
}




