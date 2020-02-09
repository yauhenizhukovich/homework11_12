package com.gmail.supersonicleader.app.repository.enums;

public enum CreateActionEnum {

    CREATE_ROLE_TABLE(
            "CREATE TABLE IF NOT EXISTS role(" +
                    "id INT(11) PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(40) NOT NULL, " +
                    "description VARCHAR(100) NOT NULL" +
                    ");"),
    CREATE_USER_TABLE(
            "CREATE TABLE IF NOT EXISTS user(" +
                    "id INT(11) PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(40) NOT NULL UNIQUE, " +
                    "password VARCHAR(40) NOT NULL, " +
                    "created_by DATE NOT NULL, " +
                    "role_id INT(11) NOT NULL," +
                    "FOREIGN KEY (role_id) REFERENCES role(id)" +
                    ");");

    private final String query;

    CreateActionEnum(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
