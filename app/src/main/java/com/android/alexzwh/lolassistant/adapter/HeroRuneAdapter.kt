package com.android.alexzwh.lolassistant.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.alexzwh.lolassistant.R
import com.android.alexzwh.lolassistant.model.RunesDetail
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener
import com.chad.library.adapter.base.BaseViewHolder

/**
 * User: zhongweihuan
 * Date: 2018/9/9
 * Description:
 */
class HeroRuneAdapter(data: MutableList<RunesDetail>?) : BaseQuickAdapter<RunesDetail, BaseViewHolder>(R.layout.item_hero_rune, data) {
	override fun convert(helper: BaseViewHolder, item: RunesDetail) {
		helper.setText(R.id.debut_rate_tv, "登场率：" + item.debutRate)
				.setText(R.id.win_rate_tv, "胜率：" + item.winRate)
		// 主符文
		val mainRv = helper.getView<RecyclerView>(R.id.main_rune_rv)
		val mainAdapter = RuneItemAdapter(item.mainList)
		mainAdapter.onItemChildClickListener = OnItemChildClickListener { _, _, position ->
			ToastUtils.showLong(item.mainList!![position].description.replace("%", "%%"))
		}
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
		secondaryAdapter.onItemChildClickListener = OnItemChildClickListener { _, _, position ->
			ToastUtils.showShort(item.secondaryList!![position].description.replace("%", "%%"))
		}
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
	}
}