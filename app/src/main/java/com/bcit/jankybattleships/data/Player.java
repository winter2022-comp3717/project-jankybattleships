package com.bcit.jankybattleships.data;

import java.io.Serializable;

public class Player implements Serializable {

    public static final String EXTRA_PLAYER = "com.bcit.jankybattleships.data.PLAYER";

    private String id;
    private int playerNum;

    public Player(String id, int playerNum) {
        this.id = id;
        this.playerNum = playerNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }
}
