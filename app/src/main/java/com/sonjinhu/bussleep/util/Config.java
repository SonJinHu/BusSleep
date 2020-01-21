package com.sonjinhu.bussleep.util;

/**
 * Created by sonjh on 2017-04-06.
 */

public class Config {
    private final String serviceKey = "3s0hPg6CF10KNEt0Ya4FNkSqH9ulFqK3VHqXw%2F9hLjBXBz4Ws4p%2BcW9%2FyjppAgVgS0hHFDq2EZVwbZEGoDF%2FZA%3D%3D";
    public final String ROUTE_URL
            = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList"
            + "?ServiceKey="
            + serviceKey
            + "&strSrch=";
    // http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList?ServiceKey=3s0hPg6CF10KNEt0Ya4FNkSqH9ulFqK3VHqXw%2F9hLjBXBz4Ws4p%2BcW9%2FyjppAgVgS0hHFDq2EZVwbZEGoDF%2FZA%3D%3D&strSrch=
    public final String STATION_URL
            = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute"
            + "?ServiceKey="
            + serviceKey
            + "&busRouteId=";
    // http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?ServiceKey=3s0hPg6CF10KNEt0Ya4FNkSqH9ulFqK3VHqXw%2F9hLjBXBz4Ws4p%2BcW9%2FyjppAgVgS0hHFDq2EZVwbZEGoDF%2FZA%3D%3D&busRouteId=241216002
    public final String POS_URL_ROUTEID
            = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid"
            + "?ServiceKey="
            + serviceKey
            + "&busRouteId=";
    // N26 - http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid?ServiceKey=3s0hPg6CF10KNEt0Ya4FNkSqH9ulFqK3VHqXw%2F9hLjBXBz4Ws4p%2BcW9%2FyjppAgVgS0hHFDq2EZVwbZEGoDF%2FZA%3D%3D&busRouteId=100100586
    // 272 - http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid?ServiceKey=3s0hPg6CF10KNEt0Ya4FNkSqH9ulFqK3VHqXw%2F9hLjBXBz4Ws4p%2BcW9%2FyjppAgVgS0hHFDq2EZVwbZEGoDF%2FZA%3D%3D&busRouteId=100100048
    // 성북21 - http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid?ServiceKey=3s0hPg6CF10KNEt0Ya4FNkSqH9ulFqK3VHqXw%2F9hLjBXBz4Ws4p%2BcW9%2FyjppAgVgS0hHFDq2EZVwbZEGoDF%2FZA%3D%3D&busRouteId=105900005
    public final String POS_URL_VEHID = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId"
            + "?ServiceKey="
            + serviceKey
            + "&vehId=";
    // http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId?ServiceKey=3s0hPg6CF10KNEt0Ya4FNkSqH9ulFqK3VHqXw%2F9hLjBXBz4Ws4p%2BcW9%2FyjppAgVgS0hHFDq2EZVwbZEGoDF%2FZA%3D%3D&vehId=106061522
}