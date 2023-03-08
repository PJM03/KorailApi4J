package me.laum;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("getCtyCodeList?_type=json")
    Call<String> getCityCodes(@Query(value = "serviceKey", encoded = true) String serviceKey);
    @GET("getVhcleKndList?_type=json")
    Call<String> getTrains(@Query(value = "serviceKey", encoded = true) String serviceKey);
    @GET("getCtyAcctoTrainSttnList?_type=json&numOfRows=10")
    Call<String> getStations(@Query(value = "serviceKey", encoded = true) String serviceKey,
                             @Query("pageNo") int pageNo,
                             @Query("cityCode") int cityCode);

    @GET("getStrtpntAlocFndTrainInfo?_type=json&numOfRows=10")
    Call<String> getTrainSchedules(@Query(value = "serviceKey", encoded = true) String serviceKey,
                             @Query("pageNo") int pageNo,
                             @Query("depPlaceId") String departures,
                             @Query("arrPlaceId") String arrivals,
                             @Query("depPlandTime") String date,
                             @Query("trainGradeCode") String trainCode);
}
