package com.android.alexzwh.lolassistant.database.model

import androidx.room.*

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 符文页Bean
 */
@Entity(tableName = "runes_page",
		foreignKeys = [ForeignKey(
				entity = Hero::class,
				parentColumns = ["id"],
				childColumns = ["hero_id"],
				onDelete = ForeignKey.CASCADE)],
		indices = [Index("hero_id")])
data class RunesPage(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                     @ColumnInfo(name = "win_rate") var winRate: String? = null,
                     @ColumnInfo(name = "debut_rate") var debutRate: String? = null,
                     @ColumnInfo(name = "main_list") var mainList: MutableList<Rune>? = null,
                     @ColumnInfo(name = "secondary_list") var secondaryList: MutableList<Rune>? = null,
                     @ColumnInfo(name = "adaptive_list") var adaptiveList: MutableList<Rune>? = null,
                     var position: String? = null,
                     @ColumnInfo(name = "hero_id") var heroId: Long = 0) {
	constructor() : this(0)
}