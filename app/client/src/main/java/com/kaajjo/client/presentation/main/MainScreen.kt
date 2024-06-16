package com.kaajjo.client.presentation.main

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaajjo.client.R
import com.kaajjo.client.presentation.main.dialog.ConfigDialog
import com.kaajjo.client.presentation.main.dialog.EmptyIpPortDialog
import com.kaajjo.client.presentation.main.dialog.EnableAccessibilityDialog
import com.kaajjo.client.system.accessibility.AccessibilityService
import com.kaajjo.client.system.accessibility.util.isAccessibilityServiceEnabled


@Composable
fun MainContent(
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var configDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var enableServiceDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var emptyIpPortDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val serverIpAddress by viewModel.serverIp.collectAsState(initial = "")
    val serverPort by viewModel.serverPort.collectAsState(initial = -1)
    var isServiceRunning by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                if (!viewModel.isValidIpAddress(serverIpAddress) || !viewModel.isPortValid(
                        serverPort
                    )
                ) {
                    emptyIpPortDialog = true
                    return@Button
                }
                if (!context.isAccessibilityServiceEnabled(AccessibilityService::class.java)) {
                    enableServiceDialog = true
                    return@Button
                }

                val serviceIntent = Intent(context, AccessibilityService::class.java)
                if (!isServiceRunning) {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lipsum.com/"))
                    context.startActivity(browserIntent)

                    context.startService(serviceIntent)
                    isServiceRunning = true
                } else {
                    context.startService(
                        serviceIntent.apply {
                            action = AccessibilityService.ACTION_DISABLE_SERVICE
                        }
                    )
                    isServiceRunning = false
                }
            }) {
                Text(
                    if (!isServiceRunning) {
                        stringResource(R.string.service_start)
                    } else {
                        stringResource(R.string.service_stop)
                    }
                )
            }

            Button(onClick = { configDialog = true }) {
                Text(stringResource(R.string.config_title))
            }
        }

        if (configDialog) {
            var ipAddress by rememberSaveable {
                mutableStateOf(serverIpAddress)
            }
            var port by rememberSaveable {
                mutableStateOf(if (serverPort >= 0) serverPort.toString() else "")
            }
            var isIpValid by rememberSaveable {
                mutableStateOf(true)
            }
            var isPortValid by rememberSaveable {
                mutableStateOf(true)
            }

            ConfigDialog(
                ipAddress = ipAddress,
                port = port,
                isIpValid = isIpValid,
                isPortValid = isPortValid,
                onIpChange = {
                    ipAddress = it
                    isIpValid = true
                },
                onPortChange = {
                    port = it
                    isPortValid = true
                },
                onConfirm = {
                    var portInt = -1
                    try {
                        portInt = port.toInt()
                    } catch (e: Exception) {
                        isPortValid = false
                    }
                    isPortValid = viewModel.isPortValid(portInt)
                    isIpValid = viewModel.isValidIpAddress(ipAddress)

                    if (isIpValid) viewModel.updateServerIp(ipAddress)
                    if (isPortValid) viewModel.updateServerPort(port.toInt())

                    configDialog = !(isIpValid && isPortValid)
                },
                onDismissRequest = { configDialog = false }
            )
        } else if (enableServiceDialog) {
            EnableAccessibilityDialog(
                onConfirm = {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    context.startActivity(intent)
                    enableServiceDialog = false
                },
                onDismissRequest = { enableServiceDialog = false })
        } else if (emptyIpPortDialog) {
            EmptyIpPortDialog(onDismissRequest = { emptyIpPortDialog = false })
        }
    }
}