package com.android.alexzwh.lolassistant

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: Jsoup工具类
 */
object JsoupUtil {
	fun get(url: String): Document {
		return Jsoup.connect(url)
				.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
				.header("Content-Type", "application/json;charset=UTF-8")
				.get()
	}
}