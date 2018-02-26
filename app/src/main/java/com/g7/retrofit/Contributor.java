package com.g7.retrofit;

/**
 * Created by newagesmb on 26/2/18.
 */

public class Contributor {
    String login;
    String html_url;

    int contributions;

    @Override
    public String toString() {
        return login + " (" + contributions + ")";
    }
}
