package com.android.alexzwh.lolassistant.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import com.android.alexzwh.lolassistant.R
import com.android.alexzwh.lolassistant.database.model.Rune
import com.blankj.utilcode.util.ImageUtils
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
		val bitmap: Bitmap
		if (item.active) {
			resId = mContext.resources.getIdentifier("rune_" + item.imgId, "drawable", mContext.packageName)
			bitmap = ImageUtils.addCircleBorder(ImageUtils.drawable2Bitmap(mContext.resources.getDrawable(resId, null)), 2, Color.BLUE)
		} else {
			resId = mContext.resources.getIdentifier("rune_" + item.imgId + "_gray", "drawable", mContext.packageName)
			bitmap = ImageUtils.drawable2Bitmap(mContext.resources.getDrawable(resId, null))
		}

		Glide.with(mContext)
				.load(bitmap)
				.apply(RequestOptions.bitmapTransform(CircleCrop()))
				.apply(RequestOptions.overrideOf(runeIv.width))
				.into(runeIv)
	}
}