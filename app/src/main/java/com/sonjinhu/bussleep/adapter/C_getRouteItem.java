package com.sonjinhu.bussleep.adapter;

public class C_getRouteItem {
    private String routeId;
    private String routeNm;
    private String routeTp;
    private int routeTpColor;
    private String stStaNm;
    private String edStaNm;

    public void setRouteNm(String routeNo) {
        routeNm = routeNo;
    }

    public void setRouteTp(String routeTp) {
        this.routeTp = routeTp;
    }

    public void setRouteTpColor(int routeTpColor) {
        this.routeTpColor = routeTpColor;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public void setStartNodeNm(String startNodeNm) {
        stStaNm = startNodeNm;
    }

    public void setEndNodeNm(String endNodeNm) {
        edStaNm = endNodeNm;
    }

    public String getRouteNm() {
        return this.routeNm;
    }

    public String getRouteTp() {
        return this.routeTp;
    }

    int getRouteTpColor() {
        return routeTpColor;
    }

    public String getRouteId() {
        return this.routeId;
    }

    public String getStartNodeNm() {
        return this.stStaNm;
    }

    public String getEndNodeNm() {
        return this.edStaNm;
    }
}