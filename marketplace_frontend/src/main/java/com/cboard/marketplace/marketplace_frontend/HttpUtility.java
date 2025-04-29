package com.cboard.marketplace.marketplace_frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public enum HttpUtility
{
    HTTP_UTILITY;

    private final Gson gson;
    private final OkHttpClient client;
    private final MediaType JSON;
    private final ObjectMapper objMapper;

    HttpUtility()
    {
        gson = new Gson();
        client = new OkHttpClient();
        JSON = MediaType.get("application/json; charset=utf-8");
        objMapper = new ObjectMapper();
    }

    public Gson getGson() {
        return gson;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public MediaType getJSON() {
        return JSON;
    }
    public ObjectMapper getObjMapper()
    {
        return objMapper;
    }
}
