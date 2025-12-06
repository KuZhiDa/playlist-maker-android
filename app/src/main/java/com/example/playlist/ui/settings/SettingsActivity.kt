package com.example.playlist.ui.settings

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.playlist.R
import com.example.playlist.ui.item.SettingsItem
import com.example.playlist.ui.item.ThemeSwitchItem

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
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Назад",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onNavigateBack() }
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = stringResource(R.string.settings_title),
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            ThemeSwitchItem(trackWidth = 32.dp, trackHeight = 12.dp, thumbSize = 18.dp)

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
                val packageManager = context.packageManager
                if (emailIntent.resolveActivity(packageManager) != null) {
                    context.startActivity(Intent.createChooser(emailIntent, emailChooserTitle))
                } else {
                    Toast.makeText(context, "Нет приложения для отправки email", Toast.LENGTH_SHORT).show()
                }
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
