package com.example.playlist.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist.R

@Composable
fun CreatePlaylistScreen(
    onNavigateBack: () -> Unit,
    onCreatePlaylist: (String, String) -> Unit,
) {
    var playlistName by remember { mutableStateOf("") }
    var playlistDescription by remember { mutableStateOf("") }

    val isFormValid = playlistName.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Назад",
                tint = Color(0xFF1A1B22),
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onNavigateBack() }
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = "Новый плейлист",
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 136.dp, bottom = 150.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.picha),
                contentDescription = "Обложка",
                tint = Color(0xFFAEAFB4),
                modifier = Modifier.size(180.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = playlistName,
                onValueChange = { playlistName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                label = { Text("Название*") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3772E7),
                    unfocusedBorderColor = Color(0xFFAEAFB4),
                    focusedTextColor = Color(0xFF1A1B22),
                    unfocusedTextColor = Color(0xFF1A1B22)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = playlistDescription,
                onValueChange = { playlistDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                label = { Text("Описание") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3772E7),
                    unfocusedBorderColor = Color(0xFFAEAFB4),
                    focusedTextColor = Color(0xFF1A1B22),
                    unfocusedTextColor = Color(0xFF1A1B22)
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 17.dp)
                .padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        color = if (isFormValid) Color(0xFF3772E7) else Color(0xFFAEAFB4),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        enabled = isFormValid,
                        onClick = {
                            onCreatePlaylist(playlistName, playlistDescription)
                            onNavigateBack()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "СОЗДАТЬ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}