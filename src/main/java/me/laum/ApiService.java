package me.laum;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("getCtyCodeList?_type=json")
    Call<String> getCityCodes(@Query(value = "serviceKey", encoded = true) String serviceKey);
    @GET("getCtyAcctoTrainSttnList?_type=json")
    Call<String> getStations(@Query(value = "serviceKey", encoded = true) String serviceKey,
                             @Query("pageNo") int pageNo,
                             @Query("numOfRows") int numOfRows,
                             @Query("cityCode") int cityCode);
}
