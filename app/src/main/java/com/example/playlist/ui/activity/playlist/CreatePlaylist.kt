package com.example.playlist.ui.activity.playlist

import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.playlist.R
import com.example.playlist.ui.utils.saveCoverToInternalStorage
import com.example.playlist.ui.viewModel.NewPlaylistViewModel

@Composable
fun CreatePlaylistScreen(
    viewModel: NewPlaylistViewModel,
    onNavigateBack: () -> Unit,
) {
    var playlistName by remember { mutableStateOf("") }
    var playlistDescription by remember { mutableStateOf("") }
    val isFormValid = playlistName.isNotBlank()

    val coverImageUri by viewModel.coverImageUri.collectAsState()
    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val savedPath = saveCoverToInternalStorage(context, it)
            viewModel.setCoverImageUri(savedPath)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) pickImageLauncher.launch("image/*") }

    fun pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickImageLauncher.launch("image/*")
        } else {
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                pickImageLauncher.launch("image/*")
            } else {
                permissionLauncher.launch(permission)
            }
        }
    }

    val hasCover = !coverImageUri.isNullOrEmpty()

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
                contentDescription = stringResource(R.string.back),
                modifier = Modifier.clickable {
                    onNavigateBack()
                    viewModel.clearCoverImage()
                }
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = stringResource(R.string.new_playlist_title),
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hasCover) {
                Spacer(modifier = Modifier.height(60.dp))
                Box(
                    modifier = Modifier
                        .size(312.dp)
                        .clickable { pickImage() },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = coverImageUri,
                        contentDescription = stringResource(R.string.playlist_cover),
                        modifier = Modifier.size(312.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(60.dp))
            } else {
                Spacer(modifier = Modifier.height(120.dp))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { pickImage() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.picha),
                        contentDescription = stringResource(R.string.playlist_cover),
                        tint = Color(0xFFAEAFB4),
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.height(120.dp))
            }

            OutlinedTextField(
                value = playlistName,
                onValueChange = { playlistName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                label = { Text(stringResource(R.string.playlist_name_label)) },
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
                label = { Text(stringResource(R.string.playlist_description_label)) },
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
                            viewModel.createNewPlaylist(playlistName, playlistDescription)
                            onNavigateBack()
                            viewModel.clearCoverImage()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.create_button),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}
