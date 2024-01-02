package com.osrsclan.models;

import com.google.gson.Gson;

public class InteractsWithApi {
    public String toRequest()
    {
        return new Gson().toJson(this);
    }
}
