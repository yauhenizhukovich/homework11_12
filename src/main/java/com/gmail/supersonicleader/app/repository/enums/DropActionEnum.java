package com.gmail.supersonicleader.app.repository.enums;

public enum DropActionEnum {
    DROP_USER_TABLE("DROP TABLE IF EXISTS user"),
    DROP_ROLE_TABLE("DROP TABLE IF EXISTS role");

    private final String query;

    DropActionEnum(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
