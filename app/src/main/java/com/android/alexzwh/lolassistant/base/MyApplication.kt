package com.android.alexzwh.lolassistant.base

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 自定义Application
 */
class MyApplication : Application() {
	override fun onCreate() {
		super.onCreate()

		Utils.init(this)
	}
}