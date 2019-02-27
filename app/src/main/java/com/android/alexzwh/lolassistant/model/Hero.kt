package com.android.alexzwh.lolassistant.model

import java.io.Serializable

/**
 * User: zhongweihuan
 * Date: 2019/1/1
 * Description: 英雄Bean
 */
data class Hero(val name: String, val nickname: String, val positions: MutableList<String>) : Serializable