package me.laum;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.laum.data.*;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class KorailApi implements AutoCloseable{
    private static final String SERVICE_URL = "http://apis.data.go.kr/1613000/TrainInfoService/";
    private static final Gson gson = new Gson();

    private static final OkHttpClient client = new OkHttpClient.Builder().build();
    private static final ApiService service = new Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(ApiService.class);
    private final String apiKey;
    private boolean shutdown = false;
    public AsyncResult<List<CityCode>> getCityCodes() {
        if (shutdown) throw new IllegalStateException("this api object is shutdown.");
        AsyncResult<List<CityCode>> result = new AsyncResult<>();
        service.getCityCodes(apiKey).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    System.out.println(call.request().url());
                    JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                    List<CityCode> list = getItemList(json, CityCode.class);
                    result.setData(list);
                } else throw new RuntimeException("Request failed. :: " + response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                throw new RuntimeException("Request failed. :: " + t);
            }
        });
        return result;
    }

    public AsyncResult<List<Train>> getTrains() {
        if (shutdown) throw new IllegalStateException("this api object is shutdown.");
        AsyncResult<List<Train>> result = new AsyncResult<>();
        service.getTrains(apiKey).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                    List<Train> list = getItemList(json, Train.class);
                    result.setData(list);
                } else throw new RuntimeException("Request failed. :: " + response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                throw new RuntimeException("Request failed. :: " + t);
            }
        });
        return result;
    }

    public AsyncResult<PageResult<List<Station>>> getStations(CityCode cityCode, int page) {
        if (shutdown) throw new IllegalStateException("this api object is shutdown.");
        if (page < 1) throw new IllegalArgumentException("page is more than 1.");
        AsyncResult<PageResult<List<Station>>> result = new AsyncResult<>();
        service.getStations(apiKey, page, cityCode.getCode())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                            List<Station> list = getItemList(json, Station.class);
                            PageResult<List<Station>> pageResult = getPageResult(json, list);
                            result.setData(pageResult);
                        } else throw new RuntimeException("Request failed. :: " + response.code());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        throw new RuntimeException("Request failed. :: " + t);
                    }
                });
        return result;
    }

    public AsyncResult<PageResult<List<TrainSchedule>>> getTrainSchedules(Station departure, Station arrival, String date, Train train, int page) {
        if (shutdown) throw new IllegalStateException("this api object is shutdown.");
        if (page < 1) throw new IllegalArgumentException("page is more than 1.");
        AsyncResult<PageResult<List<TrainSchedule>>> result = new AsyncResult<>();
        service.getTrainSchedules(apiKey, page, departure.getCode(), arrival.getCode(), date, train.getId())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            System.out.println(call.request().url());
                            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                            List<TrainSchedule> list = getItemList(json, TrainSchedule.class);
                            PageResult<List<TrainSchedule>> pageResult = getPageResult(json, list);
                            result.setData(pageResult);
                        } else throw new RuntimeException("Request failed. :: " + response.code());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        throw new RuntimeException("Request failed. :: " + t);
                    }
                });
        return result;
    }

    private <T> PageResult<T> getPageResult(JsonObject json, T data) {
        JsonObject body = json.getAsJsonObject("response")
                .getAsJsonObject("body");
        return new PageResult<>(body.get("pageNo").getAsInt(), body.get("totalCount").getAsInt(), data);
    }

    private <T> List<T> getItemList(JsonObject json, Class<T> itemClass) {
        return json.getAsJsonObject("response")
                .getAsJsonObject("body")
                .getAsJsonObject("items")
                .getAsJsonArray("item")
                .asList().stream().map(ele -> gson.fromJson(ele, itemClass))
                .collect(Collectors.toList());
    }

    @Override
    public void close() {
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        shutdown = true;
    }
}
