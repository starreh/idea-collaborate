package com.ideacollaborate.userservice.exception;

public enum ErrorCode {

    INTERNAL_ERROR("ERR_000", "Something went wrong"),

    INVALID_CREDENTIALS("ERR_001", "Invalid credentials"),
    INVALID_TOKEN("ERR_002", "Invalid token"),

    USER_ALREADY_EXISTS("ERR_003", "User already exists"),
    USER_NOT_FOUND("ERR_004", "User not found");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
