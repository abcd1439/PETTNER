package kr.bottomtab.pettner;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bottomtab.ultraviewpager.UltraViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;
import static kr.bottomtab.pettner.NetworkDefineConstant.SERVER_URL_PET_HEALTH_ADDSELECT;


public class WeightActivity extends Activity implements View.OnClickListener {
    private UltraViewPager ultraViewPager;
    private PagerAdapter adapter;

    HealthObject entityObject;

    private UltraViewPager.Orientation gravity_indicator;

    TextView text,monthdate;
    ImageView image,button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        if( entityObject == null ){
            entityObject = new HealthObject();
        }

        text=(TextView) findViewById(R.id.text);
        monthdate=(TextView) findViewById(R.id.date);
        image=(ImageView) findViewById(R.id.image);
        image.setAdjustViewBounds(true);

        button=(ImageView) findViewById(R.id.button);

        ultraViewPager = (UltraViewPager) findViewById(R.id.ultra_viewpager);

                setTitle("체중 기록");


                ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                adapter = new UltraPagerAdapter(true);
                ultraViewPager.setAdapter(adapter);
                ultraViewPager.setMultiScreen(0.1f);
                ultraViewPager.setItemRatio(0.4f);
                ultraViewPager.setAutoMeasureHeight(true);
                gravity_indicator = UltraViewPager.Orientation.HORIZONTAL;

        initUI();
        ultraViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                text.setText(Integer.toString(ultraViewPager.getCurrentItem())+"kg");
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        Calendar c= Calendar.getInstance();
        int month=c.get(c.MONTH)+1;
        int date=c.get(c.DATE);
        monthdate.setText("오늘,"+month+"월 "+date+"일");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entityObject.type=4;
                entityObject.recordFloat=ultraViewPager.getCurrentItem();
                new AsyncBloodInsert().execute(entityObject);
                finish();
            }
        });
    }

    private void initUI() {
        ultraViewPager.setInfiniteLoop(true);
        ultraViewPager.initIndicator();
        ultraViewPager.getIndicator().setOrientation(gravity_indicator);
    }



    @Override
    public void onClick(View v) {
        ultraViewPager.getIndicator().build();
    }






    public class AsyncBloodInsert extends AsyncTask<HealthObject, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(HealthObject... healthInfo) {
            boolean flag;
            String healthInsertResultValue = "";
            HealthObject reqParams = healthInfo[0];
            Response response = null;
            OkHttpClient toServer;


            try{
                toServer = OkHttpInitSingtonManager.getOkHttpClient();
                //요청 Form세팅
                RequestBody postBody = new FormBody.Builder()
                        .add("recordType", Integer.toString(reqParams.type))
                        .add("recordFloat", Integer.toString(reqParams.recordFloat))
                        .build();
                //요청 세팅(form(Query String) 방식의 포스트)
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_PET_SELECT+MySharedPreferencesManager.getInstance().getDogselect()+SERVER_URL_PET_HEALTH_ADDSELECT)
                        .post(postBody)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                flag = response.isSuccessful();
                String returedJSON;
                if( flag ){ //성공했다면
                    returedJSON = response.body().string();
                    Log.e("resultJSON", returedJSON);
                    try {
                        JSONObject jsonObject = new JSONObject(returedJSON);
                        healthInsertResultValue = jsonObject.optString("result");
                    }catch(JSONException jsone){
                        Log.e("json에러", jsone.toString());
                    }
                }else{
                    //요청에러 발생시(http 에러)
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
            return healthInsertResultValue;
        }
        @Override
        protected void onPostExecute(String result) {

        }
    }
    @Override
    public void onPause(){
        super.onPause();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(WeightActivity.this, FirstBottomTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


}
