package com.android.alexzwh.lolassistant

import android.content.Context
import android.support.v4.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog


/**
 * User: zhongweihuan
 * Date: 2018/3/26
 * Description: Dialog工具类
 */
class DialogUtil(context: Context) {
	private var progressDialog: MaterialDialog = MaterialDialog.Builder(context)
			.progress(true, 0)
			.content("正在从op.gg查询数据...")
			.widgetColor(ContextCompat.getColor(context, R.color.colorPrimary))
			.canceledOnTouchOutside(false)
			.build()

	/**
	 * 显示进度Dialog
	 */
	fun showProgressDialog() {
		progressDialog.show()
	}

	/**
	 * 隐藏进度Dialog
	 */
	fun dismissProgressDialog() {
		progressDialog.dismiss()
	}
}