package me.laum.data;

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
    private final long arrivalTime;
    @SerializedName("depplacename")
    private final String departureName;
    @SerializedName("depplandtime")
    private final long departureTime;
    @SerializedName("traingradename")
    private final String trainName;
    @SerializedName("trainno")
    private final int trainNumber;
}
