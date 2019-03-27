package com.android.alexzwh.lolassistant.database.dao

import androidx.room.*
import com.android.alexzwh.lolassistant.database.model.RunesPage

/**
 * User: zhongweihuan
 * Date: 2019/3/4
 * Description: 操作runes_page表
 */
@Dao
interface RunesPageDao {
	@Query("SELECT * FROM runes_page WHERE hero_id = :heroId")
	fun getRunesPagesOfHero(heroId : Long): List<RunesPage>

	@Query("SELECT * FROM runes_page")
	fun getAllRunesPages(): List<RunesPage>

	@Insert
	fun insert(vararg runesPage: RunesPage)

	@Update
	fun update(vararg runesPage: RunesPage)

	@Delete
	fun delete(vararg runesPage: RunesPage)
}