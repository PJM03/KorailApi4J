import me.laum.KorailApi;
import me.laum.data.AsyncResult;
import me.laum.data.CityCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ApiSampleDataTest {
    static String apiKey = "9RWFpzQa24mrQCFwsRY%2FjSeJ8Mx0HU9NosZGs0fD3S%2Ft8N5iCC9WVvcRbrg83gBAn%2FinZ4Gn1Np425%2BTob2HQ%3D%3D";
    static KorailApi api = new KorailApi(apiKey);
//    KorailApi shutdownApi = new KorailApi(apiKey);
    @Test
    void getCityCodes() {
        AsyncResult<List<CityCode>> cityCodes = api.getCityCodes();
        cityCodes.waitData().forEach(System.out::println);
    }

    @AfterAll
    static void afterAll() {
        api.close();
    }
}
