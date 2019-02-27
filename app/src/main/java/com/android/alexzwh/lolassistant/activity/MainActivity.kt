package com.android.alexzwh.lolassistant.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.widget.SearchView.OnQueryTextListener
import com.android.alexzwh.lolassistant.CommonUtil
import com.android.alexzwh.lolassistant.DialogUtil
import com.android.alexzwh.lolassistant.R
import com.android.alexzwh.lolassistant.adapter.MainHeroAdapter
import com.android.alexzwh.lolassistant.model.Hero
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.text.Collator
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
	private val msg_finish = 0
	val mAdapter: MainHeroAdapter = MainHeroAdapter(null)
	lateinit var mDialogUtil: DialogUtil
	lateinit var mElements: Elements
	private val mAllHeroList = mutableListOf<Hero>()
	private val mFilterHeroList = mutableListOf<Hero>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initRv()
		initSearchView()
		// 注册键盘变化事件，隐藏键盘时清除搜索栏焦点
		KeyboardUtils.registerSoftInputChangedListener(this) {
			if (!KeyboardUtils.isSoftInputVisible(this)) {
				main_searchView.clearFocus()
			}
		}
		mDialogUtil = DialogUtil(this)

		mDialogUtil.showProgressDialog()
		thread {
			mElements = Jsoup.connect("https://www.op.gg/champion/statistics")
					.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
					.header("Content-Type", "application/json;charset=UTF-8")
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
					.get()
					.select("div.champion-index__champion-list")
					.select("div[data-champion-key]")
			mHandler.sendEmptyMessage(msg_finish)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		KeyboardUtils.unregisterSoftInputChangedListener(this)
	}

	private val mHandler = object : Handler(Looper.getMainLooper()) {
		override fun handleMessage(msg: Message) {
			if (msg.what == msg_finish) {
				mDialogUtil.dismissProgressDialog()
				mElements.forEach { element ->
					val name = element.attr("data-champion-key")
					val nickname = element.attr("data-champion-name")
					val positions = CommonUtil.formatHeroPositions(element.select("span"))
					mAllHeroList.add(Hero(name, nickname, positions))
				}
				LogUtils.i("英雄总数：" + mElements.size)
				// 根据英雄中文名排序
				Collections.sort(mAllHeroList, object : Comparator<Hero> {
					val collator = Collator.getInstance(java.util.Locale.CHINA)
					override fun compare(o1: Hero, o2: Hero): Int {
						val compareValue = collator.compare(o1.nickname, o2.nickname)
						if (compareValue > 0) {
							return 1
						}
						if (compareValue < 0) {
							return -1
						}
						return 0
					}
				})
				mAdapter.setNewData(mAllHeroList)
			}
		}
	}

	// 根据关键字过滤英雄
	private fun filterHero(keyword: String?) {
		if (keyword.isNullOrEmpty()) {
			mAdapter.setNewData(mAllHeroList)
			return
		}
		mFilterHeroList.clear()
		mAllHeroList.forEach {
			if (it.nickname.contains(keyword!!)) {
				mFilterHeroList.add(it)
			}
		}
		mAdapter.setNewData(mFilterHeroList)
	}

	// 初始化搜索栏
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

	private fun initRv() {
		main_hero_rv.layoutManager = GridLayoutManager(this, 4)
		main_hero_rv.adapter = mAdapter
		mAdapter.setOnItemClickListener { adapter, view, position ->
			val hero = mAdapter.getItem(position)
			if (hero!!.positions.isNotEmpty()) {
				HeroActivity.newIntent(hero)
			} else {
				ToastUtils.showShort("暂无英雄数据")
			}
		}
	}
}
