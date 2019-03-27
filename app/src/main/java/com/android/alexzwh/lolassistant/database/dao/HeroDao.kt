package com.android.alexzwh.lolassistant.database.dao

import androidx.room.*
import com.android.alexzwh.lolassistant.database.model.Hero

/**
 * User: zhongweihuan
 * Date: 2019/3/4
 * Description: 操作hero表
 */
@Dao
interface HeroDao {
	@Query("SELECT * FROM hero")
	fun getAllHeros(): List<Hero>

	@Query("SELECT * FROM hero WHERE name = :name")
	fun getHero(name: String): Hero

	@Query("SELECT * FROM hero WHERE nick_name LIKE :keyword")
	fun getHeroWithKeyword(keyword: String): List<Hero>

	@Insert
	fun insert(vararg hero: Hero)

	@Update
	fun update(vararg hero: Hero)

	@Delete
	fun delete(vararg hero: Hero)
}