package me.laum;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.laum.data.AsyncResult;
import me.laum.data.CityCode;
import me.laum.data.PageResult;
import me.laum.data.StationData;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class KorailApi {
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

    public AsyncResult<PageResult<List<StationData>>> getStations(CityCode cityCode, int page) {
        if (shutdown) throw new IllegalStateException("this api object is shutdown.");
        AsyncResult<PageResult<List<StationData>>> result = new AsyncResult<>();
        service.getStations(apiKey, page, 10, cityCode.getCode())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        System.out.println(call.request().url());
                        if (response.isSuccessful()) {
                            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                            List<StationData> list = getItemList(json, StationData.class);
                            PageResult<List<StationData>> pageResult = getPageResult(json, list);
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

    public void shutdown() {
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        shutdown = true;
    }
}
