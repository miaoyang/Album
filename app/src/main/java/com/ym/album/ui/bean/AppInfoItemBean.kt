package com.ym.album.ui.bean

import android.graphics.drawable.Drawable

data class AppInfoItemBean(
    var appName:String,
    var pkgName:String,
    var versionName:String,
    var versionCode :Int,
    var launchClassName:String,
    var appIcon:Drawable
)
