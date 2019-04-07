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
import com.android.alexzwh.lolassistant.util.Constant
import com.android.alexzwh.lolassistant.util.JsoupUtil
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_hero.*
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import kotlin.concurrent.thread

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 英雄详情界面
 */
class HeroActivity : BaseActivity() {
	private val mAdapter: HeroRuneAdapter = HeroRuneAdapter(null)
	lateinit var mHero: Hero
	lateinit var mDocument: Document
	/**
	 * 符文元素
	 */
	lateinit var mRuneElements: Elements
	private val mPositionRunePageList = mutableListOf<RunesPage>()

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
			mHandler.sendEmptyMessage(Constant.MSG_JSOUP_FINISH)
		}
	}

	override fun getLayoutId(): Int = R.layout.activity_hero

	override fun getToolBar(): Toolbar = hero_tool_bar

	/**
	 * 数据获取完成后处理的Handler
	 */
	private val mHandler = object : Handler(Looper.getMainLooper()) {
		override fun handleMessage(msg: Message) {
			if (msg.what == Constant.MSG_JSOUP_FINISH) {
				handleData()
			}
		}
	}

	/**
	 * 处理获取的符文页数据
	 */
	private fun handleData() {
		mRuneElements = mDocument.select("tbody.tabItem")
				.select("tr")
		mDialogUtil.dismissProgressDialog()
		mRuneElements.forEach { element ->
			// 胜率
			val winRate = element.select("strong")[0].text()
			// 登场率
			val debutRate = element.select("strong")[1].text()
			// 主符文
			val mainList = makeRuneList(0, element)
			// 副符文
			val secondaryList = makeRuneList(1, element)
			// 自适应符文
			val adaptiveList = makeRuneList(2, element)

			mAdapter.addData(RunesPage(
					winRate = winRate,
					debutRate = debutRate,
					mainList = mainList,
					secondaryList = secondaryList,
					adaptiveList = adaptiveList))
		}
	}

	/**
	 * @param type 符文类型 0 主符文 1 副符文 2 自适应符文
	 */
	private fun makeRuneList(type: Int, element: Element): MutableList<Rune> {
		val list = arrayListOf<Rune>()
		val elements = when (type) {
			0 -> element.select("div.perk-page")[0].select("img")
			1 -> element.select("div.perk-page")[1].select("img")
			2 -> element.select("div.fragment-page").select("img")
			else -> null
		} ?: return mutableListOf()
		for (i in elements.indices) {
			if (type == 2) {
				val active = !elements[i].attr("src").contains("e_grayscale")
				if (active) {
					val imgId = elements[i].attr("src").substring(49, 53)
					list.add(Rune(
							name = elements[i].attr("alt"),
							imgId = imgId,
							description = elements[i].attr("title").replace(Regex("<.+?>"), ""),
							active = true))
				}
			} else {
				val imgId = if (i == 0) {
					elements[i].attr("src").substring(49, 53)
				} else {
					elements[i].attr("src").substring(44, 48)
				}
				list.add(Rune(
						name = elements[i].attr("alt"),
						imgId = imgId,
						description = elements[i].attr("title").replace(Regex("<.+?>"), ""),
						active = !elements[i].attr("src").contains("e_grayscale")))
			}

		}

		return list
	}
}