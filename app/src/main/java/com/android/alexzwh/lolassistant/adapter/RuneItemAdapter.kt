package com.android.alexzwh.lolassistant.adapter

import android.widget.ImageView
import com.android.alexzwh.lolassistant.R
import com.android.alexzwh.lolassistant.database.model.Rune
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 英雄符文项适配器
 */
class RuneItemAdapter(data: MutableList<Rune>?) : BaseQuickAdapter<Rune, BaseViewHolder>(R.layout.item_rune, data) {
	override fun convert(helper: BaseViewHolder, item: Rune) {
		helper.addOnClickListener(R.id.item_rune_iv)
		val runeIv = helper.getView<ImageView>(R.id.item_rune_iv)
		val layoutParams = runeIv.layoutParams
		runeIv.layoutParams = layoutParams
		val resId: Int
		if (item.active) {
			resId = mContext.resources.getIdentifier("rune_" + item.imgId, "drawable", mContext.packageName)
//			rune.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_gray))
		} else {
			resId = mContext.resources.getIdentifier("rune_" + item.imgId + "_gray", "drawable", mContext.packageName)
		}
		Glide.with(mContext)
				.load(resId)
				.apply(RequestOptions.bitmapTransform(CircleCrop()))
				.into(runeIv)
	}
}