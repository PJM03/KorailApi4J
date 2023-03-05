package me.laum.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class StationData {
    @SerializedName("nodename")
    private final String name;
    @SerializedName("nodeid")
    private final String code;
}
