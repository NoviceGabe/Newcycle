package com.example.newcycle.Utils;

import java.util.regex.Pattern;

public class Validator {

    public static final boolean isNameValid(String target){
        return Pattern.compile("[A-Z][a-z]+( [A-Z][a-z]+)?").matcher(target).matches();
    }

    public static final boolean isPhoneNumberValid(String target){
        return Pattern.compile("^\\+[0-9]{2}[0-9]{10}$").matcher(target).matches();
    }

    public static final boolean isEmailValid(String target){
        return Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$").matcher(target).matches();
    }

    public static final boolean isPasswordValid(String target){
        return Pattern.compile("^[A-Z](?=.*\\d)(?=.*[a-z])[\\w~@#$%^&*+=`|{}:;!.?\"()\\[\\]]{8,25}$").matcher(target).matches();
    }
}
