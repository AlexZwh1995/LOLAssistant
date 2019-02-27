package com.android.alexzwh.lolassistant.model

/**
 * User: zhongweihuan
 * Date: 2018/9/11
 * Description:
 */
data class RunesDetail(val winRate: String,
                       val debutRate: String,
                       val mainList: MutableList<Rune>? = null,
                       val secondaryList: MutableList<Rune>? = null)