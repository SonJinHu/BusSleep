package com.sonjinhu.bussleep.adapter;

public class D_getPosItem {
    private boolean isBus1;
    private boolean isBus2;
    private boolean isBus3;
    private String plainNo1;
    private String plainNo2;
    private String plainNo3;
    private int site1;
    private int site2;
    private int site3;
    private String vehId1;
    private String vehId2;
    private String vehId3;
    private boolean isBtnStop;

    public void setBus1(boolean bus1) {
        isBus1 = bus1;
    }

    public void setBus2(boolean bus2) {
        isBus2 = bus2;
    }

    public void setBus3(boolean bus3) {
        isBus3 = bus3;
    }

    boolean isBus1() {
        return isBus1;
    }

    boolean isBus2() {
        return isBus2;
    }

    boolean isBus3() {
        return isBus3;
    }

    public void setPlainNo1(String plainNo1) {
        this.plainNo1 = plainNo1;
    }

    public void setPlainNo2(String plainNo2) {
        this.plainNo2 = plainNo2;
    }

    public void setPlainNo3(String plainNo3) {
        this.plainNo3 = plainNo3;
    }

    String getPlainNo1() {
        return plainNo1;
    }

    String getPlainNo2() {
        return plainNo2;
    }

    String getPlainNo3() {
        return plainNo3;
    }

    public void setSite1(int site1) {
        this.site1 = site1;
    }

    public void setSite2(int site2) {
        this.site2 = site2;
    }

    public void setSite3(int site3) {
        this.site3 = site3;
    }

    int getSite1() {
        return site1;
    }

    int getSite2() {
        return site2;
    }

    int getSite3() {
        return site3;
    }

    public void setVehId1(String vehId1) {
        this.vehId1 = vehId1;
    }

    public void setVehId2(String vehId2) {
        this.vehId2 = vehId2;
    }

    public void setVehId3(String vehId3) {
        this.vehId3 = vehId3;
    }

    String getVehId1() {
        return vehId1;
    }

    String getVehId2() {
        return vehId2;
    }

    String getVehId3() {
        return vehId3;
    }

    void setBtnStop(boolean btnStop) {
        isBtnStop = btnStop;
    }

    boolean isBtnStop() {
        return isBtnStop;
    }
}