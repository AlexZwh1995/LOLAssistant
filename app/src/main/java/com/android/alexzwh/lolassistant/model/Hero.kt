package com.android.alexzwh.lolassistant.model

import java.io.Serializable

/**
 * User: zhongweihuan
 * Date: 2018/9/8
 * Description:
 */
data class Hero(val name : String, val nickname : String, val positions : MutableList<String>) : Serializable