package com.kaajjo.clientserverexam.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class SwipeAction(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "pos_x") val posX: Float,
    @ColumnInfo(name = "pos_y") val posY: Float,
    @ColumnInfo(name = "swipe_length") val swipeLength: Float,
    @ColumnInfo(name = "swipe_down") val swipeDown: Boolean,
    @ColumnInfo(name = "gesture_completed") val gestureCompleted: Boolean,
    @ColumnInfo(name = "datetime") val dateTime: LocalDateTime,
    @ColumnInfo(name = "client_ip") val clientIp: String
)