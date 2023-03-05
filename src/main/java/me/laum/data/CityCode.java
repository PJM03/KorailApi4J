package me.laum.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class CityCode {
    @SerializedName("cityname")
    private final String name;

    @SerializedName("citycode")
    private final int code;
}
