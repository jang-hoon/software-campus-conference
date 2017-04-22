package kr.lunawyrd.handwritingrecognition.network;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by lunawyrd on 2017-03-21.
 */

public interface APIService {

    @Headers( "Content-Type: application/json" )
    @POST("/analysis")
    Call<String> analysis(@Body List<Float> lineList);
}
