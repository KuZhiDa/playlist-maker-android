package com.example.playlist.ui.settings

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.playlist.R

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current

    val shareMessage = stringResource(R.string.share_message, context.packageName)
    val shareChooserTitle = stringResource(R.string.share_chooser_title)
    val devEmail = stringResource(R.string.dev_email)
    val emailSubject = stringResource(R.string.email_subject)
    val emailBody = stringResource(R.string.email_body)
    val emailChooserTitle = stringResource(R.string.email_chooser_title)
    val userAgreementLink = stringResource(R.string.user_agreement_link)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
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
                text = stringResource(R.string.settings_title),
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {

            SettingsItem(
                iconRes = R.drawable.share,
                text = stringResource(R.string.share_app),
                iconWidth = 16.dp,
                iconHeight = 18.dp
            ) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                }
                context.startActivity(Intent.createChooser(shareIntent, shareChooserTitle))
            }

            SettingsItem(
                iconRes = R.drawable.support,
                text = stringResource(R.string.write_to_devs),
                iconWidth = 20.dp,
                iconHeight = 18.dp
            ) {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(devEmail))
                    putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                    putExtra(Intent.EXTRA_TEXT, emailBody)
                }
                context.startActivity(Intent.createChooser(emailIntent, emailChooserTitle))
            }

            SettingsItem(
                iconRes = R.drawable.chevron_right,
                text = stringResource(R.string.user_agreement),
                iconWidth = 8.dp,
                iconHeight = 14.dp
            ) {
                val agreementIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = userAgreementLink.toUri()
                }
                context.startActivity(agreementIntent)
            }
        }
    }
}

@Composable
fun SettingsItem(
    iconRes: Int,
    text: String,
    iconWidth: Dp,
    iconHeight: Dp,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp)
            .padding(start = 16.dp)
            .height(61.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Box(Modifier
            .size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color(0xFFAEAFB4),
                modifier = Modifier
                    .width(iconWidth)
                    .height(iconHeight)
            )
        }
    }
}
