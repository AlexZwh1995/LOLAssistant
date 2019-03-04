package com.android.alexzwh.lolassistant.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.android.alexzwh.lolassistant.DialogUtil
import com.blankj.utilcode.util.ActivityUtils

/**
 * User: zhongweihuan
 * Date: 2019/2/28
 * Description: 基类Activity
 */
abstract class BaseActivity : AppCompatActivity() {
	/**
	 * 对话框工具类对象
	 */
	lateinit var mDialogUtil: DialogUtil

	/**
	 * 初始化数据（从网络或者本地），最先执行
	 */
	protected abstract fun initData()

	/**
	 * 初始化视图，在初始化视图完成后执行
	 */
	protected abstract fun initView()

	/**
	 * 左上角返回键（默认启用）
	 * @return true 启用 false 禁用
	 */
	protected fun enableBack(): Boolean = true

	/**
	 * 返回layout id
	 */
	protected abstract fun getLayoutId(): Int

	/**
	 * 返回ToolBar对象（默认不启用）
	 * @return null 不启用ToolBar
	 */
	protected open fun getToolBar(): Toolbar? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(getLayoutId())
		if (getToolBar() != null) {
			setSupportActionBar(getToolBar())
		}

		if (enableBack()) {
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
		}
		mDialogUtil = DialogUtil(this)
		initData()
		initView()
	}

	override fun onDestroy() {
		mDialogUtil.dismissProgressDialog()
		super.onDestroy()
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when (item?.itemId) {
			// 返回键结束当前Activity
			android.R.id.home -> {
				ActivityUtils.finishActivity(this)
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}
}