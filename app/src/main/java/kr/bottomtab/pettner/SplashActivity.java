package kr.bottomtab.pettner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

public class SplashActivity extends AppCompatActivity {

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));



        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        new AsyncToken().execute(refreshedToken);

    }
    private class AsyncDog extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;


            try{
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_USER)
                        .get()
                        .build();

                response = toClient.newCall(request).execute();
                String returedJSON;
                Log.e("tt","dogselect");

                returedJSON = response.body().string();
                Log.e("tt", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONObject j=jsonObject.getJSONObject("result");
                    JSONArray ja=j.getJSONArray("animals");

                    JSONObject jsData=ja.getJSONObject(0);
                    id= String.valueOf(jsData.getInt("id"));
                    Log.e("tt","dogselect: "+id);
                }catch(JSONException jsone){
                    Log.e("json에러", jsone.toString());
                }


            }catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            } finally{
                if(response != null) {
                    response.close(); //3.* 이상에서는 반드시 닫아 준다.
                }
            }
            Log.e("tt", "here-------------------------------");
            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            MySharedPreferencesManager.getInstance().setDogselect(id);

            Intent intent = new Intent(SplashActivity.this, FirstBottomTabActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private class AsyncToken extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("fcmToken", params[0])
                    .build();

            //request
            Request request = new Request.Builder()
                    .url(NetworkDefineConstant.SERVER_URL_USER)
                    .post(body)
                    .build();

            try {
                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("tt","after: "+params[0]);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            new AsyncDog().execute();

        }
    }

}
