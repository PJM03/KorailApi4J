package me.laum;

import me.laum.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final Map<String, Station> STATION_MAP = new HashMap<>();
    private static final Map<String, CityCode> CITYCODE_MAP = new HashMap<>();
    private static final Map<String, Train> TRAIN_MAP = new HashMap<>();

    public static void main(String[] args) {
        try (KorailApi api = new KorailApi("9RWFpzQa24mrQCaFwsRY%2FjSeJ8Mx0HU9NosZGs0fD3S%2Ft8N5iCC9WVvcRbrg83gBAn%2FinZ4Gn1Np425%2BTob2HQ%3D%3D")) {
            api.getCityCodes().waitData().forEach(cityCode -> {
                CITYCODE_MAP.put(cityCode.getName(), cityCode);
                getAllStations(api, cityCode, 1).forEach(station -> STATION_MAP.put(station.getName(), station));
            });
            api.getTrains().waitData().forEach(train -> TRAIN_MAP.put(train.getName(), train));
            System.out.println(TRAIN_MAP);
            System.out.println(TRAIN_MAP.get("KTX"));
            api.getTrainSchedules(STATION_MAP.get("서울"), STATION_MAP.get("대전"), "20230308", TRAIN_MAP.get("KTX"), 1)
                    .waitData().getData()
                    .forEach(System.out::println);
        }
    }

    private static List<Station> getAllStations(KorailApi api, CityCode city, int startPage) {
        AsyncResult<PageResult<List<Station>>> stations = api.getStations(city, startPage);
        PageResult<List<Station>> pageResult = stations.waitData();
        List<Station> result = new ArrayList<>(pageResult.getData());
        if (pageResult.hasNext()) result.addAll(getAllStations(api, city, startPage + 1));
        return result;
    }
}