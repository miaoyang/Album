package com.ym.album.ui.activity

class Constant {
    class Account{
        companion object{
            public const val TELEPHONE:String = "telephone"
            public const val PASSWORD:String = "password"
            const  val AUTO_LOGIN_MAX_TIME = 1*24*60*60*1000 // 7 days
            const  val AUTO_LOGIN_TIME = "auto_login_time"
            const val IS_FIRST_LOGIN = "is_first_login";

        }
    }
    class Image{
        companion object{
            const val clickItemImage:String = "click_item_image"
        }
    }
}