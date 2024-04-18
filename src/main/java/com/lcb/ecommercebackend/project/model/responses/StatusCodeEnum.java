package com.lcb.ecommercebackend.project.model.responses;

import lombok.Data;

public enum StatusCodeEnum {
    RECORD_NOT_FOUND(282, "RECORD NOT FOUND", ""),
    DUPLICATE_ID(283, "DUPLICATE ID", ""),
    NO_DATA(284, "NO DATA", ""),
    SUCCESS(200, "SUCCESS", ""),
    ID_DELETED(250, "ID DELETED", ""),
    OK_DELETED(251, "OK DELETED", ""),
    OK_MSG(252, "OK", ""),
    DISCOUNT_CREATION_API(1, "CREATE DISCOUNT", "POST"),
    DISCOUNT_UPDATION_API(2, "UPDATE DISCOUNT", "PUT"),
    DISCOUNT_DELETION_API(2, "DELETE DISCOUNT", "DELETE"),
    DATA_MISSING(3, "FIELD MISSING", ""),
    ID_NOT_FOUND(4, "ID NOT FOUND", "");
//    DUPLICATE_ID(283, "OK Done!");

    private final int statusCode;
    private final String messgage;
    private final String requestCode;

    StatusCodeEnum(int code, String message, String requestCode) {
        this.statusCode = code;
        this.messgage = message;
        this.requestCode = requestCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessgage() {
        return messgage;
    }

    public String getRequestCode() {
        return requestCode;
    }
}

