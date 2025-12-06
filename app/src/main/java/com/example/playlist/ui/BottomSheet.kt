package com.example.playlist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetExample(modifier: Modifier = Modifier) {

    var isShowPanel by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Box(modifier = modifier.fillMaxSize()) {

        FloatingActionButton(
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.BottomEnd),
            onClick = { isShowPanel = true },
            containerColor = Color.Gray,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "fab")
        }

        if (isShowPanel) {
            ModalBottomSheet(
                onDismissRequest = { isShowPanel = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bottom sheet",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(top = 16.dp),
                        imageVector = Icons.Filled.Home,
                        contentDescription = "icon"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomExamplePreview() {
    BottomSheetExample()
}
