package me.laum.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class Train {
    @SerializedName("vehiclekndnm")
    private final String name;
    @SerializedName("vehiclekndid")
    private final int id;
}
