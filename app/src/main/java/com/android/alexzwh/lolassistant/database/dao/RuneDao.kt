package com.android.alexzwh.lolassistant.database.dao

import androidx.room.*
import com.android.alexzwh.lolassistant.database.model.Rune

/**
 * User: zhongweihuan
 * Date: 2019/3/4
 * Description: 操作rune表
 */
@Dao
interface RuneDao {
	@Query("SELECT * FROM rune")
	fun getAllRunes(): List<Rune>

	@Insert
	fun insert(vararg rune: Rune)

	@Update
	fun update(vararg rune: Rune)

	@Delete
	fun delete(vararg rune: Rune)
}