package kr.bottomtab.pettner;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by pyoinsoo on 2016-11-06.
 */

public class OkHttpInitSingtonManager {
    private static OkHttpClient okHttpClient;
    static{
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }
   public static OkHttpClient getOkHttpClient(){
       if( okHttpClient != null){
           return okHttpClient;
       }else{
           okHttpClient = new OkHttpClient.Builder()
                   .connectTimeout(10, TimeUnit.SECONDS)
                   .readTimeout(10, TimeUnit.SECONDS)
                   .build();
       }
       return okHttpClient;
   }
}
