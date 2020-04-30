package com.saizad.mvvm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sa.easyandroidfrom.ObjectUtils;
import com.sa.easyandroidfrom.StringUtils;

import java.util.List;

public class ErrorModel {


    @SerializedName("error")
    public Error error;

    public static class Error {
        @SerializedName("status")
        public int status;
        @SerializedName("fields")
        public List<FieldError> fields;
        @SerializedName("description")
        public String description;
        @SerializedName("error")
        public String error;
        @SerializedName("error_code")
        public Integer errorCode;
        @SerializedName("message")
        public String message;
    }

    public static String generateErrorMessage(ErrorModel errorModel) {
        if (ObjectUtils.isNotNull(errorModel.error.fields)) {
            StringBuilder fields = new StringBuilder();
            for (FieldError fieldError : errorModel.error.fields) {
                fields.append(fieldError.field).append(", ");
            }
            if(errorModel.error.fields.size() > 1){
                return StringUtils.stripTrailingLeadingNewLines(fields.toString()) + " fields are missing";
            }else {
                return StringUtils.stripTrailingLeadingNewLines(fields.toString()) + " field is missing";
            }
        }
        return errorModel.error.description;
    }

    public static class FieldError {

        @Expose
        @SerializedName("message")
        public List<String> message;
        @Expose
        @SerializedName("field")
        public String field;
    }
}
