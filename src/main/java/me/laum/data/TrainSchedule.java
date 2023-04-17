package me.laum.data;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class TrainSchedule {
    @SerializedName("adultcharge")
    private final int cost;
    @SerializedName("arrplacename")
    private final String arrivalName;

    @SerializedName("arrplandtime")
    @JsonAdapter(DateAdapter.class)
    private final LocalDateTime arrivalTime;

    @SerializedName("depplacename")
    private final String departureName;
    
    @SerializedName("depplandtime")
    @JsonAdapter(DateAdapter.class)
    private final LocalDateTime departureTime;
    
    @SerializedName("traingradename")
    private final String trainName;
    @SerializedName("trainno")
    private final int trainNumber;

    private static class DateAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        @Override
        public LocalDateTime deserialize(JsonElement json, Type t, JsonDeserializationContext ctx) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), FMT);
        }

        @Override
        public JsonElement serialize(LocalDateTime src, Type t, JsonSerializationContext ctx) {
            return ctx.serialize(src.format(FMT));
        }
    }
}

