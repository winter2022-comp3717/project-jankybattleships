package com.bcit.jankybattleships.data;

import java.util.Random;

public class RoomCodeGenerator {

    private static final String ALPHANUMERIC =
            "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int CODE_LENGTH = 6;

    public static String generateRoomCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        while (code.length() < CODE_LENGTH) {
            code.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return code.toString();
    }
}
