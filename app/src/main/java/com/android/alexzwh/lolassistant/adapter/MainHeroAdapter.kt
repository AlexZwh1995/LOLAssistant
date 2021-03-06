package com.android.alexzwh.lolassistant.adapter

import android.widget.ImageView
import com.android.alexzwh.lolassistant.R
import com.android.alexzwh.lolassistant.database.model.Hero
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 英雄列表适配器
 */
class MainHeroAdapter(data: MutableList<Hero>?) : BaseQuickAdapter<Hero, BaseViewHolder>(R.layout.item_main_hero, data) {
	override fun convert(helper: BaseViewHolder, item: Hero) {
		helper.setText(R.id.item_main_hero_name_tv, item.nickname)
		val headIv = helper.getView<ImageView>(R.id.item_main_hero_head_iv)
		val resId: Int = mContext.resources.getIdentifier(item.name, "drawable", mContext.packageName)
		Glide.with(mContext)
				.load(resId)
				.apply(RequestOptions.bitmapTransform(CircleCrop()))
				.apply(RequestOptions.overrideOf(headIv.width))
				.into(headIv)
	}
}