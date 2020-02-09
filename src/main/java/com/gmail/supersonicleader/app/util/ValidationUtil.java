package com.gmail.supersonicleader.app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gmail.supersonicleader.app.util.constant.ValidationConstant.*;

public class ValidationUtil {

    public static void checkUsername(String username) throws IllegalArgumentException {
        if (username.length() < MIN_SYMBOLS_USERNAME || username.length() > MAX_SYMBOLS_USERNAME) {
            throw new IllegalArgumentException(
                    "Username length should be from " + MIN_SYMBOLS_USERNAME + " to " + MAX_SYMBOLS_USERNAME + "."
            );
        }
        Pattern pattern = Pattern.compile(USERNAME_REGEX);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Username can contain letters, digits and \"_\", \"-\" characters.");
        }
    }

    public static void checkPassword(String password) throws IllegalArgumentException {
        if (password.length() < MIN_SYMBOLS_PASSWORD || password.length() > MAX_SYMBOLS_PASSWORD) {
            throw new IllegalArgumentException(
                    "Password length should be from " + MIN_SYMBOLS_PASSWORD + " to " + MAX_SYMBOLS_PASSWORD + "."
            );
        }
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Password can contain only letters and digits.");
        }
    }

}
