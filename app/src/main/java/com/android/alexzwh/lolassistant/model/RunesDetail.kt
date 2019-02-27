package com.android.alexzwh.lolassistant.model

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 符文页Bean
 */
data class RunesDetail(val winRate: String,
                       val debutRate: String,
                       val mainList: MutableList<Rune>? = null,
                       val secondaryList: MutableList<Rune>? = null)