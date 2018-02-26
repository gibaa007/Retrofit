package com.g7.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by newagesmb on 26/2/18.
 */

public class CommonModal<T> {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("userData")
    @Expose
    private T body;
    @SerializedName("message")
    @Expose
    private String function;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
