package com.kaajjo.clientserverexam.presentation.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.ImportExport
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SwipeVertical
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kaajjo.clientserverexam.R
import com.kaajjo.clientserverexam.data.local.database.entity.SwipeAction
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    navController: NavController,
    viewModel: LogsViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.logs_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        },
    ) { innerPadding ->
        val logs by viewModel.logs.collectAsState(initial = emptyList())

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            items(logs) { log ->
                LogItem(log)
            }
        }
    }
}

@Composable
fun LogItem(
    log: SwipeAction,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColoredBox {
                Text(
                    text = log.clientIp,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            ColoredBox(
                background =
                if (log.gestureCompleted) Color(0xFF4C662B)
                else MaterialTheme.colorScheme.errorContainer
            ) {
                Text(
                    text = if (log.gestureCompleted) stringResource(R.string.gesture_completed)
                    else stringResource(R.string.gesture_canceled),
                    color =
                    if (log.gestureCompleted) Color(0xFFFFFFFF)
                    else MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconRow(
                icon = Icons.Outlined.Info,
                text = stringResource(R.string.initial_position_x_y, log.posX, log.posY)
            )
            IconRow(
                icon = Icons.Rounded.SwipeVertical,
                text = stringResource(R.string.swipe_length, log.swipeLength)
            )
            IconRow(
                icon = Icons.Rounded.ImportExport,
                text = stringResource(
                    R.string.swipe_direction,
                    if (log.swipeDown) stringResource(R.string.swipe_direction_down) else stringResource(
                        R.string.swipe_direction_up
                    )
                )
            )
            IconRow(
                icon = Icons.Rounded.Schedule,
                text = log.dateTime.format(
                    DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")
                )
            )
        }
    }
}

@Composable
fun IconRow(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun ColoredBox(
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.secondaryContainer,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(background)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        content()
    }
}