package com.osrsclan.models;

import lombok.Getter;
import lombok.Setter;

public class OsrsClanMember extends InteractsWithApi {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int rank;
}
