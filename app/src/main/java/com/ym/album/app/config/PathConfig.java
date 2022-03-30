package com.ym.album.app.config;

public interface PathConfig {

    // login or register
    interface Account{
        String ACCOUNT = "/account";
        String LOGIN = ACCOUNT + "/login";
        String REGISTER = ACCOUNT + "/register";
        String FORGET_PASSWORD = ACCOUNT + "/forget_password";
        String RESET_PASSWORD  = ACCOUNT + "/reset_password";
    }

    interface HOME{
        String MAIN = "/main";
        String MAIN_ACTIVITY = MAIN + "/album";
    }

    interface Image{
        String IMAGE = "/image";
        String IMAGE_CLICK = IMAGE + "/click";
        // fragment
        String SELECT_IMAGE_FRAGMENT = IMAGE+"/select_image_fragment";
    }

    interface Person{
        String PERSON = "/person";
        String PERSON_HOME = PERSON + "/home";
        String PERSON_INFO_ACTIVITY = PERSON + "/person_info_activity";
        // fragment
        String PERSON_HEAD_IMAGE = PERSON + "/person_head_image";
        String PERSON_INFO_FRAGMENT = PERSON + "/person_info_fragment";
    }
}
