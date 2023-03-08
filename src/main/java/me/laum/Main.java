package me.laum;

import me.laum.data.AsyncResult;
import me.laum.data.CityCode;
import me.laum.data.PageResult;
import me.laum.data.Station;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (KorailApi api = new KorailApi("9RWFpzQa24mrQCaFwsRY%2FjSeJ8Mx0HU9NosZGs0fD3S%2Ft8N5iCC9WVvcRbrg83gBAn%2FinZ4Gn1Np425%2BTob2HQ%3D%3D")) {
            AsyncResult<List<CityCode>> result = api.getCityCodes();
            List<CityCode> cityCodes = result.waitData();
            cityCodes.forEach(System.out::println);
            System.out.println(cityCodes.get(14));
            System.out.println();
            System.out.println();

            List<Station> allStations = getAllStations(api, cityCodes.get(14), 1);
            allStations.forEach(System.out::println);

            api.getTrains().waitData().forEach(System.out::println);
        }
    }

    private static List<Station> getAllStations(KorailApi api, CityCode city, int page) {
        AsyncResult<PageResult<List<Station>>> stations = api.getStations(city, page);
        PageResult<List<Station>> pageResult = stations.waitData();
        List<Station> result = new ArrayList<>(pageResult.getData());
        if (pageResult.hasNext()) result.addAll(getAllStations(api, city, page + 1));
        return result;
    }
}