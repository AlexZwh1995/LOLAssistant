package com.android.alexzwh.lolassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.alexzwh.lolassistant.database.dao.HeroDao
import com.android.alexzwh.lolassistant.database.dao.RuneDao
import com.android.alexzwh.lolassistant.database.dao.RunesPageDao
import com.android.alexzwh.lolassistant.database.model.Hero
import com.android.alexzwh.lolassistant.database.model.Rune
import com.android.alexzwh.lolassistant.database.model.RunesPage

/**
 * User: zhongweihuan
 * Date: 2019/3/11
 * Description:
 */
@Database(entities = [Hero::class, Rune::class, RunesPage::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun heroDao(): HeroDao
	abstract fun runeDao(): RuneDao
	abstract fun runesPageDao(): RunesPageDao

	companion object {
		@Volatile
		private var INSTANCE: AppDatabase? = null

		fun getInstance(context: Context) {
			if (INSTANCE == null) {
				synchronized(AppDatabase::class.java) {
					if (INSTANCE == null) {
						INSTANCE = Room.databaseBuilder(context.applicationContext,
								AppDatabase::class.java, "lolassitant.db")
								.build()
					}
				}
			}
		}
	}
}