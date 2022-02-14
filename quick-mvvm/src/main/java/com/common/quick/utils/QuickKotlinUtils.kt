package com.common.module.quick.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun Context.launch(targetClz: Class<out Context>, bundle: Bundle? = null) {
    QuickKotlinUtils.launch(this, targetClz, bundle)
}

fun Long.parseTime0ToCurrentTime(): Long {
    if (this == 0L) {
        return System.currentTimeMillis()
    } else {
        return this
    }
}

fun Int.dayToMilliTime(): Long {
    return this * 24 * 60 * 60 * 1000L;
}

fun Int.hourToMilliTime(): Long {
    return this * 60 * 60 * 1000L;
}

fun Int.minuteToMilliTime(): Long {
    return this * 60 * 1000L;
}

fun Int.secondToMilliTime(): Long {
    return this * 1000L;
}

class QuickKotlinUtils {

    companion object {

        @JvmStatic
        fun launch(context: Context, targetClz: Class<out Context>, bundle: Bundle? = null) {
            val i = Intent(context, targetClz)
            bundle?.apply {
                i.putExtras(this)
            }
            context.startActivity(i)
        }

        @JvmStatic
        fun launch(fragmentManager: FragmentManager, targetClz: Class<out DialogFragment>, bundle: Bundle? = null) {
            val dialog = targetClz.newInstance()
            dialog.arguments = bundle
            dialog.show(fragmentManager, targetClz.name)
        }

    }


}