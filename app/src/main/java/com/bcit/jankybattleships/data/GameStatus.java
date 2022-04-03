package com.bcit.jankybattleships.data;

public enum GameStatus {
    JOIN_WAIT("JOIN_WAIT"),
    SHIP_PLACEMENT("SHIP_PLACEMENT"),
    PLACEMENT_WAIT_P1("PLACEMENT_WAIT_P1"),
    PLACEMENT_WAIT_P2("PLACEMENT_WAIT_P2"),
    P1_TURN("P1_TURN"),
    P2_TURN("P2_TURN");

    private final String status;

    GameStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
