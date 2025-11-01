package com.example.playlist.ui.item

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist.R

@Composable
fun ThemeSwitchItem(
    title: String = stringResource(R.string.dark),
    trackWidth: Dp,
    trackHeight: Dp,
    thumbSize: Dp,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedColor: Color = Color(0xFFD1D5DB)
) {
    var isChecked by remember { mutableStateOf(false) }

    val maxOffset = trackWidth - thumbSize
    val thumbOffset by animateDpAsState(
        targetValue = if (isChecked) maxOffset else 0.dp,
    )
    val trackTint by animateColorAsState(
        targetValue = if (isChecked) checkedColor.copy(alpha = 0.7f) else uncheckedColor,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(horizontal = 16.dp)
            .clickable { isChecked = !isChecked },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(thumbSize),
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.track),
                contentDescription = null,
                modifier = Modifier
                    .width(trackWidth)
                    .height(trackHeight),
                colorFilter = ColorFilter.tint(trackTint)
            )
            Image(
                painter = painterResource(id = R.drawable.knob),
                contentDescription = null,
                modifier = Modifier
                    .offset(x = thumbOffset)
                    .size(thumbSize)
            )
        }
    }
}