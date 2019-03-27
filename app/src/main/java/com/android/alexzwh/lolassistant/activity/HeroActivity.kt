package com.android.alexzwh.lolassistant.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.alexzwh.lolassistant.R
import com.android.alexzwh.lolassistant.adapter.HeroRuneAdapter
import com.android.alexzwh.lolassistant.base.BaseActivity
import com.android.alexzwh.lolassistant.database.model.Hero
import com.android.alexzwh.lolassistant.database.model.Rune
import com.android.alexzwh.lolassistant.database.model.RunesPage
import com.android.alexzwh.lolassistant.util.JsoupUtil
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_hero.*
import org.jsoup.nodes.Document
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
	lateinit var mDocument: Document
	private val mPositionRuneMap = mutableMapOf<String, MutableList<RunesPage>>()

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
			mDocument = JsoupUtil.get("https://www.op.gg/champion/${mHero.name?.toLowerCase()}/statistics/${mHero.positions?.get(0)}")
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
				mElements = mDocument.select("tbody.tabItem")
						.select("tr")
				mDialogUtil.dismissProgressDialog()
				mElements.forEach { element ->
					// 胜率
					val winRate = element.select("strong")[0].text()
					// 登场率
					val debutRate = element.select("strong")[1].text()
					// 主符文
					val mainList = arrayListOf<Rune>()
					val mainElements = element.select("div.perk-page")[0].select("img")
					for (i in mainElements.indices) {
						val imgId = if (i == 0) {
							mainElements[i].attr("src").substring(49, 53)
						} else {
							mainElements[i].attr("src").substring(44, 48)
						}
						mainList.add(Rune(
								name = mainElements[i].attr("alt"),
								imgId = imgId,
								description = mainElements[i].attr("title").replace(Regex("<.+?>"), ""),
								active = !mainElements[i].attr("src").contains("e_grayscale")))
					}
					// 副符文
					val secondaryList = arrayListOf<Rune>()
					val secondaryElements = element.select("div.perk-page")[1].select("img")
					for (i in secondaryElements.indices) {
						val imgId = if (i == 0) {
							secondaryElements[i].attr("src").substring(49, 53)
						} else {
							secondaryElements[i].attr("src").substring(44, 48)
						}
						secondaryList.add(Rune(
								name = secondaryElements[i].attr("alt"),
								imgId = imgId,
								description = secondaryElements[i].attr("title").replace(Regex("<.+?>"), ""),
								active = !secondaryElements[i].attr("src").contains("e_grayscale")))
					}
					// 自适应符文
					val adaptiveList = arrayListOf<Rune>()
					val adaptiveElements = element.select("div.fragment-page").select("img")
					for (i in adaptiveElements.indices) {
						val active = !adaptiveElements[i].attr("src").contains("e_grayscale")
						if (active) {
							val imgId = adaptiveElements[i].attr("src").substring(49, 53)
							adaptiveList.add(Rune(
									name = adaptiveElements[i].attr("alt"),
									imgId = imgId,
									description = adaptiveElements[i].attr("title").replace(Regex("<.+?>"), ""),
									active = true))
						}

					}
					mAdapter.addData(RunesPage(
							winRate = winRate,
							debutRate = debutRate,
							mainList = mainList,
							secondaryList = secondaryList,
							adaptiveList = adaptiveList))
				}
			}
		}
	}
}