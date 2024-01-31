package com.example.roomhandson.data.local.enitities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.roomhandson.utils.Constants
import java.io.Serializable

@Entity(tableName = Constants.NOTE_DB)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var note: String,
    var lastTouchTime: String,
):Serializable