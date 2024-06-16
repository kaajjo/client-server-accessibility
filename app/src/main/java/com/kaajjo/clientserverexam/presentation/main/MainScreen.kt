package com.kaajjo.clientserverexam.presentation.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kaajjo.clientserverexam.R

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val isServerRunning by viewModel.isRunning.collectAsState(initial = false)
    val serverPort by viewModel.serverPort.collectAsState(initial = -1)

    var configDialog by remember { mutableStateOf(false) }
    var ipAddress by remember { mutableStateOf("") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    if (!isServerRunning) {
                        viewModel.startServer(serverPort, context.applicationContext)
                    } else {
                        viewModel.stopServer()
                    }
                }) {
                    AnimatedContent(targetState = isServerRunning, label = "") { running ->
                        Text(
                            if (running) {
                                stringResource(R.string.server_turnoff)
                            } else {
                                stringResource(R.string.server_enable)
                            }
                        )
                    }
                }
            }
            Button(onClick = { configDialog = true }) {
                Text(stringResource(R.string.config_title))
            }
            Button(onClick = { navController.navigate("logs") }) {
                Text(stringResource(R.string.logs_title))
            }
            Text(
                text = stringResource(R.string.server_info, ipAddress, serverPort),
                style = MaterialTheme.typography.labelLarge
            )
        }

        if (configDialog) {
            var newPort by rememberSaveable {
                mutableStateOf(if (serverPort >= 0) serverPort.toString() else "")
            }
            AlertDialog(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null
                    )
                },
                title = { Text("Конфиг") },
                onDismissRequest = { configDialog = false },
                dismissButton = {
                    TextButton(onClick = { configDialog = false }) {
                        Text("Отмена")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        configDialog = false
                        viewModel.updatePort(newPort.toInt())
                    }) {
                        Text("Сохранить")
                    }
                },
                text = {
                    Column {
                        OutlinedTextField(
                            label = { Text("Порт сервера") },
                            value = newPort,
                            onValueChange = { newPort = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                }
            )

        }
    }

    LaunchedEffect(Unit) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        if (connectivityManager is ConnectivityManager) {
            val link: LinkProperties =
                connectivityManager.getLinkProperties(connectivityManager.activeNetwork) as LinkProperties
            ipAddress = link.linkAddresses[1].address.hostAddress?.toString() ?: "null"
        }

    }
}