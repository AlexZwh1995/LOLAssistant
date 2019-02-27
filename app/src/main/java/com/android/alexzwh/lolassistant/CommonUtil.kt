package com.android.alexzwh.lolassistant

import com.blankj.utilcode.util.StringUtils
import org.jsoup.nodes.Element

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 通用工具类
 */
object CommonUtil {
	fun formatHeroName(name: String): String {
		return when (name) {
			"aurelionsol" -> "AurelionSol"
			"drmundo" -> "DrMundo"
			"jarvaniv" -> "JarvanIV"
			"kogmaw" -> "KogMaw"
			"leesin" -> "LeeSin"
			"masteryi" -> "MasterYi"
			"missfortune" -> "MissFortune"
			"reksai" -> "RekSai"
			"tahmkench" -> "TahmKench"
			"twistedfate" -> "TwistedFate"
			"monkeyking" -> "MonkeyKing"
			"xinzhao" -> "XinZhao"
			else -> StringUtils.upperFirstLetter(name)
		}
	}

	fun formatHeroPositions(positions: MutableList<Element>): MutableList<String> {
		val result = mutableListOf<String>()
		positions.toMutableList().forEach {
			when (it.text()) {
				"上单" -> result.add("top")
				"打野" -> result.add("jungle")
				"中单" -> result.add("mid")
				"Bottom" -> result.add("bot")
				"辅助" -> result.add("support")
			}
		}
		return result
	}
}