package com.osrsclan.models;

import lombok.Getter;
import lombok.Setter;

public class OsrsClanRank extends InteractsWithApi {
    @Setter
    private int id;

    @Setter
    @Getter
    private String name;

}
