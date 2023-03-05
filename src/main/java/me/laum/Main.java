package me.laum;

import me.laum.data.AsyncResult;
import me.laum.data.CityCode;
import me.laum.data.PageResult;
import me.laum.data.StationData;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        KorailApi api = new KorailApi("9RWFpzQa24mrQCaFwsRY%2FjSeJ8Mx0HU9NosZGs0fD3S%2Ft8N5iCC9WVvcRbrg83gBAn%2FinZ4Gn1Np425%2BTob2HQ%3D%3D");
        AsyncResult<List<CityCode>> result = api.getCityCodes();
        List<CityCode> cityCodes = result.waitData();
        cityCodes.forEach(System.out::println);
        System.out.println(cityCodes.get(14));
        AsyncResult<PageResult<List<StationData>>> stations = api.getStations(cityCodes.get(14), 1);
        PageResult<List<StationData>> pageResult = stations.waitData();
        System.out.println(pageResult.getTotalCount());
        pageResult.getData().forEach(System.out::println);
        api.shutdown();
    }
}