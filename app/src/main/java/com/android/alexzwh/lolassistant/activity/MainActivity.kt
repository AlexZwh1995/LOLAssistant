package com.android.alexzwh.lolassistant.activity

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.widget.SearchView.OnQueryTextListener
import com.android.alexzwh.lolassistant.CommonUtil
import com.android.alexzwh.lolassistant.JsoupUtil
import com.android.alexzwh.lolassistant.R
import com.android.alexzwh.lolassistant.adapter.MainHeroAdapter
import com.android.alexzwh.lolassistant.base.BaseActivity
import com.android.alexzwh.lolassistant.model.Hero
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.nodes.Document
import java.text.Collator
import java.util.*
import kotlin.concurrent.thread

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 主界面（英雄列表）
 */
class MainActivity : BaseActivity() {
	val msg_finish = 0
	val mAdapter: MainHeroAdapter = MainHeroAdapter(null)
	lateinit var mDocument: Document
	private val mAllHeroList = mutableListOf<Hero>()
	private val mFilterHeroList = mutableListOf<Hero>()

	override fun initView() {
		initRv()
		initSearchView()
		// 注册键盘变化事件，隐藏键盘时清除搜索栏焦点
		KeyboardUtils.registerSoftInputChangedListener(this) {
			if (!KeyboardUtils.isSoftInputVisible(this)) {
				main_searchView.clearFocus()
			}
		}
	}

	override fun initData() {
		mDialogUtil.showProgressDialog()
		thread {
			mDocument = JsoupUtil.get("https://www.op.gg/champion/statistics")
			mHandler.sendEmptyMessage(msg_finish)
		}
	}

	override fun getLayoutId(): Int = R.layout.activity_main

	override fun enableBack(): Boolean = false

	override fun onDestroy() {
		super.onDestroy()
		KeyboardUtils.unregisterSoftInputChangedListener(this)
	}

	/**
	 * 数据获取完成后处理的Handler
	 */
	private val mHandler = object : Handler(Looper.getMainLooper()) {
		override fun handleMessage(msg: Message) {
			if (msg.what == msg_finish) {
				val elements = mDocument.select("div.champion-index__champion-list")
						.select("div[data-champion-key]")
				mDialogUtil.dismissProgressDialog()
				elements.forEach { element ->
					val name = element.attr("data-champion-key")
					val nickname = element.attr("data-champion-name")
					val positions = CommonUtil.formatHeroPositions(element.select("span"))
					mAllHeroList.add(Hero(name, nickname, positions))
				}
				LogUtils.i("英雄总数：" + elements.size)
				// 根据英雄中文名排序
				Collections.sort(mAllHeroList, object : Comparator<Hero> {
					override fun compare(o1: Hero, o2: Hero): Int {
						val collator = Collator.getInstance(java.util.Locale.CHINA)
						return collator.compare(o1.nickname, o2.nickname)
					}
				})
				mAdapter.setNewData(mAllHeroList)
			}
		}
	}

	/**
	 * 根据关键字过滤英雄
	 */
	private fun filterHero(keyword: String?) {
		if (keyword.isNullOrEmpty()) {
			mAdapter.setNewData(mAllHeroList)
			return
		}
		mFilterHeroList.clear()
		mAllHeroList.forEach {
			if (it.nickname.contains(keyword)) {
				mFilterHeroList.add(it)
			}
		}
		mAdapter.setNewData(mFilterHeroList)
	}

	/**
	 * 初始化搜索栏
	 */
	private fun initSearchView() {
		main_searchView.setOnQueryTextListener(object : OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				filterHero(query)
				return true
			}

			override fun onQueryTextChange(newText: String?): Boolean {
				filterHero(newText)
				return true
			}
		})
	}

	/**
	 * 初始化RecyclerView
	 */
	private fun initRv() {
		main_hero_rv.apply {
			layoutManager = GridLayoutManager(this@MainActivity, 4)
			adapter = mAdapter
			mAdapter.setOnItemClickListener { _, _, position ->
				val hero = mAdapter.getItem(position)
				if (hero!!.positions.isNotEmpty()) {
					HeroActivity.newIntent(hero)
				} else {
					ToastUtils.showShort("暂无英雄数据")
				}
			}
		}

	}
}
