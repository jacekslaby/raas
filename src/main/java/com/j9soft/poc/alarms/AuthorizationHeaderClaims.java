package com.j9soft.poc.alarms;

public class AuthorizationHeaderClaims {
    private String domain;
    private String adapterName;

    AuthorizationHeaderClaims(String domain, String adapterName) {
        this.domain = domain;
        this.adapterName = adapterName;
    }

    public String getAdapterName() {
        return this.adapterName;
    }

    public String getDomain() {
        return this.domain;
    }

}
