package com.android.alexzwh.lolassistant

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * User: zhongweihuan
 * Date: 2018/3/24
 * Description:
 */
class MyApplication : Application() {
	override fun onCreate() {
		super.onCreate()

		Utils.init(this)
	}
}