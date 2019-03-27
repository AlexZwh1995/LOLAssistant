package com.android.alexzwh.lolassistant.database.model

import androidx.room.*
import com.android.alexzwh.lolassistant.database.Converters
import java.io.Serializable

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 英雄Bean
 */
@Entity(tableName = "hero", indices = [Index(value = ["name"], unique = true)])
@TypeConverters(Converters::class)
data class Hero(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                var name: String? = null,
                @ColumnInfo(name = "nick_name") var nickname: String? = null,
                var positions: MutableList<String>? = null) : Serializable {
	constructor() : this(0)
}