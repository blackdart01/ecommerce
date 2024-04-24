package com.lcb.ecommercebackend.project.model.responses;

import lombok.Data;

public enum StatusCodeEnum {
    RECORD_NOT_FOUND(282, "RECORD NOT FOUND", ""),
    DUPLICATE_ID(283, "DUPLICATE ID", ""),
    NEGATIVE_PRICE(283, "PRICE CAN NOT BE LESS THAN 0", ""),
    DUPLICATE_SKU(283, "DUPLICATE SKU", ""),
    UNMATCHED_SKU(283, "SKU DOES NOT MATCH", ""),
    NEGATIVE_PRODUCT_QUANITY(283, "QUANITY CANNOT BE LESS THAN 0", ""),
    NON_FLOAT_VALUE(283, "CAN NOT BE FLOATING VALUE", ""),
    NO_DATA(284, "NO DATA", ""),
    SUCCESS(200, "SUCCESS", ""),
    ID_DELETED(250, "ID DELETED", ""),
    OK_DELETED(251, "OK DELETED", ""),
    OK_MSG(252, "OK", ""),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR", ""),
    DISCOUNT_CREATION_API(1, "CREATE DISCOUNT", "POST"),
    PRODUCT_CREATION_API(2, "CREATE PRODUCT", "POST"),
    DISCOUNT_UPDATION_API(3, "UPDATE DISCOUNT", "PUT"),
    PRODUCT_UPDATION_API(4, "UPDATE PRODUCT", "PUT"),
    DISCOUNT_DELETION_API(5, "DELETE DISCOUNT", "DELETE"),
    PRODUCT_DELETION_API(6, "DELETE PRODUCT", "DELETE"),
    DATA_MISSING(7, "FIELD MISSING", ""),
    PRODUCT_ID_NOT_EXIST(288, "PRODUCT ID DOES NOT EXIST", ""),
    DATA_NOT_SAVED(288, "ISSUE IN DATA SAVING", ""),
    ID_UNMATCHED(288, "ID DOES NOT MATCHED", ""),
    ID_NOT_FOUND(8, "ID NOT FOUND", "");
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

