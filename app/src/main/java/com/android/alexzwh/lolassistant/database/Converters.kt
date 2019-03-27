package com.android.alexzwh.lolassistant.database

import androidx.room.TypeConverter
import com.android.alexzwh.lolassistant.database.model.Rune
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * User: zhongweihuan
 * Date: 2019/3/11
 * Description: 转换器
 */
object Converters {
	@JvmStatic
	@TypeConverter
	fun string2RuneList(runeListInfo: String): MutableList<Rune> = Gson().fromJson(runeListInfo, object : TypeToken<MutableList<Rune>>() {}.type)

	@JvmStatic
	@TypeConverter
	fun runeList2String(runeList: MutableList<Rune>): String = Gson().toJson(runeList)

	@JvmStatic
	@TypeConverter
	fun string2PositionList(positionListInfo: String): MutableList<String> = Gson().fromJson(positionListInfo, object : TypeToken<MutableList<String>>() {}.type)

	@JvmStatic
	@TypeConverter
	fun positionList2String(positionList: MutableList<String>): String = Gson().toJson(positionList)
}