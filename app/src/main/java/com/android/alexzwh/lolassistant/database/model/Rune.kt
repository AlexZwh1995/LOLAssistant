package com.android.alexzwh.lolassistant.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 符文Bean
 */
@Entity(tableName = "rune", indices = [Index(value = ["name"], unique = true)])
data class Rune(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                var name: String? = null,
                @ColumnInfo(name = "img_id") var imgId: String? = null,
                var description: String? = null,
                var active: Boolean = false) {
	constructor() : this(0)
}