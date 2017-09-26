package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;
import static kr.bottomtab.pettner.NetworkDefineConstant.SERVER_URL_PET_HEALTH_ADDSELECT;

/**
 * Created by user on 2017-06-06.
 */

public class KitSaveActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView date;
    TextView status1,status2,status3,number1,number2,number3;
    ImageView back,save;
    ArrayList<Integer> recordfloat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kitsave);
        if(recordfloat==null){
            recordfloat=new ArrayList<>();
        }


        Intent i=getIntent();
        int data1=i.getIntExtra("value1",-1);
        int data2=i.getIntExtra("value2",-1);
        int data3=i.getIntExtra("value3",-1);

        status1=(TextView) findViewById(R.id.status1);
        status2=(TextView) findViewById(R.id.status2);
        status3=(TextView) findViewById(R.id.status3);
        number1=(TextView) findViewById(R.id.number1);
        number2=(TextView) findViewById(R.id.number2);
        number3=(TextView) findViewById(R.id.number3);


        Button firstbtn,secondbtn,thirdbtn;
        firstbtn=(Button)findViewById(R.id.firstbtn);
        secondbtn=(Button)findViewById(R.id.secondbtn);
        thirdbtn=(Button)findViewById(R.id.thirdbtn);
        if(data1==0.1||data1==-1){
            firstbtn.setBackgroundColor(Color.rgb(254,202,152));
            status1.setText("정상");
            status1.setTextColor(Color.rgb(33,125,251));
            data1=0;
        }else if(data1==1){
            firstbtn.setBackgroundColor(Color.rgb(250,168,146));
            status1.setText("비정상");
            status1.setTextColor(Color.rgb(255,64,129));
        }else if(data1==2){
            firstbtn.setBackgroundColor(Color.rgb(235,146,142));
            status1.setText("비정상");
            status1.setTextColor(Color.rgb(255,64,129));
        }else if(data1==4){
            firstbtn.setBackgroundColor(Color.rgb(240,138,134));
            status1.setText("비정상");
            status1.setTextColor(Color.rgb(255,64,129));
        }else if(data1==8){
            firstbtn.setBackgroundColor(Color.rgb(232,107,137));
            status1.setText("비정상");
            status1.setTextColor(Color.rgb(255,64,129));
        }
        number1.setText(Integer.toString(data1));

        if(data2==0||data2==-1){
            secondbtn.setBackgroundColor(Color.rgb(144,208,182));
            status2.setText("정상");
            status2.setTextColor(Color.rgb(33,125,251));
            data2=0;
        }else if(data2==100){
            secondbtn.setBackgroundColor(Color.rgb(140,196,135));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }else if(data2==250){
            secondbtn.setBackgroundColor(Color.rgb(123,176,86));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }else if(data2==500){
            secondbtn.setBackgroundColor(Color.rgb(140,144,67));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }else if(data2==1000){
            secondbtn.setBackgroundColor(Color.rgb(129,118,54));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }else if(data2==2000){
            secondbtn.setBackgroundColor(Color.rgb(119,85,58));
            status2.setText("비정상");
            status2.setTextColor(Color.rgb(255,64,129));
        }
        number2.setText(Integer.toString(data2));

        if(data3==0||data3==-1){
            thirdbtn.setBackgroundColor(Color.rgb(220,233,119));
            status3.setText("정상");
            status3.setTextColor(Color.rgb(33,125,251));
            data3=0;
        }else if(data3==1){
            thirdbtn.setBackgroundColor(Color.rgb(186,211,109));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }else if(data3==30){
            thirdbtn.setBackgroundColor(Color.rgb(165,193,119));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }else if(data3==100){
            thirdbtn.setBackgroundColor(Color.rgb(144,185,17));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }else if(data3==300){
            thirdbtn.setBackgroundColor(Color.rgb(112,175,154));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }else if(data3==2000){
            thirdbtn.setBackgroundColor(Color.rgb(89,156,138));
            status3.setText("비정상");
            status3.setTextColor(Color.rgb(255,64,129));
        }
        number3.setText(Integer.toString(data3));



        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        toolbar.setTitle("키트검사");
        toolbar.setTitleTextColor(Color.WHITE);

        date=(TextView) findViewById(R.id.date);

        Calendar c = Calendar.getInstance( );
        date.setText((c.get(Calendar.MONTH)+1)+"월 "+c.get(Calendar.DATE)+"일 키트 종합 결과");



        back=(ImageView) findViewById(R.id.back);
        save=(ImageView) findViewById(R.id.save);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(KitSaveActivity.this, KitActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncKitInsert().execute((String) number1.getText(),(String) number2.getText(),(String) number3.getText());

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent i= new Intent(this, KitActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

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
        Intent i = new Intent(this, KitActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    public class AsyncKitInsert extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            for(int i=1;i<4;i++){
                boolean flag;
                String kitInsertResultValue = "";

                Response response = null;
                OkHttpClient toServer;


                try{
                    toServer = OkHttpInitSingtonManager.getOkHttpClient();
                    //요청 Form세팅
                    RequestBody postBody = new FormBody.Builder()
                            .add("recordType", Integer.toString(i))
                            .add("recordFloat", params[i-1])
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
                            kitInsertResultValue = jsonObject.optString("result");
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
            }
            return null;


        }
        @Override
        protected void onPostExecute(String result) {
                /*Intent i= new Intent(KitSaveActivity.this, PersonalActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();*/
        }
    }
}
