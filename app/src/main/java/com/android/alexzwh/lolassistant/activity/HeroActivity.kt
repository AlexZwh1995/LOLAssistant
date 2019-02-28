package com.android.alexzwh.lolassistant.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.android.alexzwh.lolassistant.R
import com.android.alexzwh.lolassistant.adapter.HeroRuneAdapter
import com.android.alexzwh.lolassistant.base.BaseActivity
import com.android.alexzwh.lolassistant.model.Hero
import com.android.alexzwh.lolassistant.model.Rune
import com.android.alexzwh.lolassistant.model.RunesDetail
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.android.synthetic.main.activity_hero.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import kotlin.concurrent.thread

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 英雄详情界面
 */
class HeroActivity : BaseActivity() {
	val msg_finish = 0
	val mAdapter: HeroRuneAdapter = HeroRuneAdapter(null)
	lateinit var mHero: Hero
	lateinit var mElements: Elements
	private val mPositionRuneMap = mutableMapOf<String, MutableList<RunesDetail>>()

	companion object {
		fun newIntent(hero: Hero) {
			val bundle = Bundle()
			bundle.putSerializable("hero", hero)
			ActivityUtils.startActivity(bundle, HeroActivity::class.java)
		}
	}

	override fun initView() {
		hero_rune_rv.apply {
			adapter = mAdapter
			layoutManager = LinearLayoutManager(this@HeroActivity)
		}
	}

	override fun initData() {
		mDialogUtil.showProgressDialog()
		mHero = intent.getSerializableExtra("hero") as Hero
		supportActionBar?.title = mHero.nickname

		thread {
			mElements = Jsoup.connect("https://www.op.gg/champion/${mHero.name.toLowerCase()}/statistics/${mHero.positions[0]}")
					.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
					.header("Content-Type", "application/json;charset=UTF-8")
					.get()
					.select("tbody.tabItem")
					.select("tr")
			mHandler.sendEmptyMessage(msg_finish)
		}
	}

	override fun getLayoutId(): Int = R.layout.activity_hero

	override fun getToolBar(): Toolbar = hero_tool_bar

	/**
	 * 数据获取完成后处理的Handler
	 */
	private val mHandler = object : Handler(Looper.getMainLooper()) {
		override fun handleMessage(msg: Message) {
			if (msg.what == msg_finish) {
				mDialogUtil.dismissProgressDialog()
				mElements.forEach { element ->
					// 胜率
					val winRate = element.select("strong")[0].text()
					// 登场率
					val debutRate = element.select("strong")[1].text()
					// 主符文
					val mainList = mutableListOf<Rune>()
					val mainElements = element.select("div.perk-page")[0].select("img")
					for (i in mainElements.indices) {
						val active: Boolean = !mainElements[i].attr("src").contains("e_grayscale")
						val imgId = if (i == 0) {
							mainElements[i].attr("src").substring(49, 53)
						} else {
							mainElements[i].attr("src").substring(44, 48)
						}
						mainList.add(Rune(active,
								imgId,
								mainElements[i].attr("title").replace(Regex("<.+?>"), ""),
								mainElements[i].attr("alt")))
					}
					// 副符文
					val secondaryList = mutableListOf<Rune>()
					val secondaryElements = element.select("div.perk-page")[1].select("img")
					for (i in secondaryElements.indices) {
						val active: Boolean = !secondaryElements[i].attr("src").contains("e_grayscale")
						val imgId = if (i == 0) {
							secondaryElements[i].attr("src").substring(49, 53)
						} else {
							secondaryElements[i].attr("src").substring(44, 48)
						}
						secondaryList.add(Rune(active,
								imgId,
								secondaryElements[i].attr("title").replace(Regex("<.+?>"), ""),
								secondaryElements[i].attr("alt")))
					}
					LogUtils.i(RunesDetail(winRate, debutRate, mainList, secondaryList))
					mAdapter.addData(RunesDetail(winRate, debutRate, mainList, secondaryList))
				}
			}
		}
	}
}
