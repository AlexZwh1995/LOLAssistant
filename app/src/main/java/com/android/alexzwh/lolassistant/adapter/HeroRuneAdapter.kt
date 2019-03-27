package com.android.alexzwh.lolassistant.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.alexzwh.lolassistant.R
import com.android.alexzwh.lolassistant.database.model.RunesPage
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener
import com.chad.library.adapter.base.BaseViewHolder

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 英雄符文页适配器
 */
class HeroRuneAdapter(data: MutableList<RunesPage>?) : BaseQuickAdapter<RunesPage, BaseViewHolder>(R.layout.item_hero_rune, data) {
	override fun convert(helper: BaseViewHolder, item: RunesPage) {
		helper.setText(R.id.debut_rate_tv, "登场率：" + item.debutRate)
				.setText(R.id.win_rate_tv, "胜率：" + item.winRate)

		val onItemChildClickListener = OnItemChildClickListener { _, _, position ->
			if (position == 0) {
				return@OnItemChildClickListener
			}
			val description = item.mainList!![position].description?.replace("%", "%%")
			val name = item.mainList!![position].name
			// 用：分隔名称和描述
			ToastUtils.showLong(description?.replace(name!!, "$name："))
		}
		// 主符文
		val mainRv = helper.getView<RecyclerView>(R.id.main_rune_rv)
		val mainAdapter = RuneItemAdapter(item.mainList)
		mainAdapter.onItemChildClickListener = onItemChildClickListener
		mainRv.adapter = mainAdapter
		val mainLayoutManager = GridLayoutManager(mContext, 12)
		mainLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
			override fun getSpanSize(position: Int): Int {
				when (item.mainList?.size) {
					// 启迪、巫术、坚决
					13 -> {
						return if (position == 0) {
							12
						} else {
							4
						}
					}
					// 精密
					14 -> {
						return when {
							position == 0 -> 12
							position < 5 -> 3
							else -> 4
						}
					}
					// 主宰
					else -> {
						return when {
							position == 0 -> 12
							position < 5 || position > 10 -> 3
							else -> 4
						}
					}
				}
			}
		}
		mainRv.layoutManager = mainLayoutManager
		// 副符文
		val secondaryRv = helper.getView<RecyclerView>(R.id.secondary_rune_rv)
		val secondaryAdapter = RuneItemAdapter(item.secondaryList)
		secondaryAdapter.onItemChildClickListener = onItemChildClickListener
		secondaryRv.adapter = secondaryAdapter
		val secondaryLayoutManager = GridLayoutManager(mContext, 12)
		secondaryLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
			override fun getSpanSize(position: Int): Int {
				when (item.secondaryList?.size) {
					// 启迪、巫术、坚决、精密
					10 -> {
						return if (position == 0) {
							12
						} else {
							4
						}
					}
					// 主宰
					else -> {
						return when {
							position == 0 -> 12
							position > 6 -> 3
							else -> 4
						}
					}
				}
			}
		}
		secondaryRv.layoutManager = secondaryLayoutManager
		// 自适应符文
		val adaptiveRv = helper.getView<RecyclerView>(R.id.adaptive_rune_rv)
		val adaptiveAdapter = RuneItemAdapter(item.adaptiveList)
		adaptiveAdapter.onItemChildClickListener = OnItemChildClickListener { _, _, position ->
			ToastUtils.showLong(item.adaptiveList!![position].description?.replace("%", "%%"))
		}
		adaptiveRv.adapter = adaptiveAdapter
		val adaptiveLayoutManager = GridLayoutManager(mContext, 1)
		adaptiveRv.layoutManager = adaptiveLayoutManager
	}
}