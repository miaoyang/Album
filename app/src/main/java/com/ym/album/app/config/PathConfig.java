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
    }

    interface Person{
        String PERSON = "/person";
        String PERSON_HOME = PERSON + "/home";
        String PERSON_INFO = PERSON + "/person_info";
    }
}
