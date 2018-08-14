package com.htrucci.apiversion.vo;

public enum Version {
    V1("v1"), V2("v2"), V3("v3"), V4("v4"), V5("v5");

    private final String version;

    Version(String version){
        this.version = version;
    }

    public String getVersion(){
        return version;
    }
}
