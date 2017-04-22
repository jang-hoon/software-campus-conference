package kr.lunawyrd.handwritingrecognition.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lunawyrd on 2017-03-21.
 */

public class RetrofitManager {

    public static final String API_URL = "http://192.168.0.3:8000/";

    private static RetrofitManager mInstance;

    private Retrofit mRetrofit;

    private RetrofitManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitManager getInstance(){
        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance;
    }

    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }


}
