package kr.bottomtab.pettner;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bottomtab.jgraph.graph.JcoolGraph;
import com.bottomtab.jgraph.inter.OnGraphItemListener;
import com.bottomtab.jgraph.models.Jchart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;
import static com.bottomtab.jgraph.inter.BaseGraph.SELECETD_MSG_SHOW_TOP;
import static kr.bottomtab.pettner.NetworkDefineConstant.SERVER_URL_PET_HEALTH_ADDSELECT;

/**
 * Created by pyoinsoo on 2017-05-30.
 */
//첫번째 하단탭을 클릭(터치)했을때의 Activity
public class FirstBottomTabActivity extends BottomNavigationParentActivity {
    private BackPressCloseHandler backPressCloseHandler;
    private int flagWorking = -1;
    ImageView image0,image1,image2,image3;
    TextView badge_notification_1;
    ArrayList<HealthObject> returedData;
    ArrayList<HealthObject> weightData;
    ArrayList<Integer> liverData;
    ArrayList<Integer> diabetesData;
    ArrayList<Integer> kidneyData;
    ArrayList<String> dateData;

    private JcoolGraph mLineChar;

    String id;
    int flag=1;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLineChar = (JcoolGraph)findViewById(R.id.sug_recode_line);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.logo); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요

        toolbar.setTitle("헬스케어");
        toolbar.setTitleTextColor(Color.WHITE);

        backPressCloseHandler = new BackPressCloseHandler(this);
        new AsyncHospital().execute();


        badge_notification_1=(TextView) findViewById(R.id.badge_notification_1);


        mLineChar.setOnGraphItemListener(new OnGraphItemListener() {
            @Override
            public void onItemClick(int position) {
                if(flag==0){
                    int select=mLineChar.getSelected();
                    if(select==-1){
                        select=0;
                    }
                    HealthFragment f = HealthFragment.newInstance(liverData.get(select),diabetesData.get(select),kidneyData.get(select),dateData.get(select));
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, f);
                    ft.commit();
                }
            }
            @Override
            public void onItemLongClick(int position) {
            }
        });

        image0=(ImageView) findViewById(R.id.btn_0);
        image1=(ImageView) findViewById(R.id.btn_1);
        image2=(ImageView) findViewById(R.id.btn_2);
        image3=(ImageView) findViewById(R.id.btn_3);
    }
    @Override
    public int getCurrentActivityLayoutName() {
        return R.layout.activity_first_layout;
    }

    @Override
    public int getCurrentSelectedBottomMenuItemID() {

        return R.id.first_tab;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_items, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_alarm:
                //배지 카운터 설정(초기화)(기기별 호환문제)//
                Intent i = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

                i.putExtra("badge_count", 0); //다시 배지카운터를 0으로 초기화.//
                i.putExtra("badge_count_package_name", getApplicationContext().getPackageName());
                i.putExtra("badge_count_class_name", SplashActivity.class.getName());

                MySharedPreferencesManager.getInstance().setBadgecount(0);

                sendBroadcast(i); //브로드캐스트를 이용.//

                badge_notification_1.setVisibility(View.GONE);

                Intent intent1=new Intent(FirstBottomTabActivity.this,AlarmActivity.class);
                startActivity(intent1);
                break;
            case R.id.action_personal:
                Intent intent2=new Intent(FirstBottomTabActivity.this,PersonalActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();

        if(MySharedPreferencesManager.getInstance().getBadgecount()>0){
            badge_notification_1.setText("!");
            badge_notification_1.setVisibility(View.VISIBLE);
        }

        new AsyncHealthJSONList().execute();
    }

    private void drawChart(){
        List<Jchart> lines=new ArrayList<>();

        for(int i=0;i<5;i++){
            lines.add(new Jchart(0,0,"0", Color.parseColor("#636363")));
        }

        mLineChar.setLinePointRadio((int)mLineChar.getLineWidth());
        mLineChar.setNormalColor(Color.parseColor("#ffffff"));
        mLineChar.feedData(lines);

        ( (FrameLayout)mLineChar.getParent() ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mLineChar.postInvalidate();
            }
        });


        mLineChar.setSelectedMode(SELECETD_MSG_SHOW_TOP);
        mLineChar.setPaintShaderColors(Color.parseColor("#ffffff"));
        //mLineChar.setPaintShaderColors(Color.RED, Color.parseColor("#006699"),Color.parseColor("#FFF03D"), Color.parseColor("#A9E16F"), Color.parseColor("#75B9EF"));
        mLineChar.setShaderAreaColors(Color.parseColor("#0468dd"), Color.TRANSPARENT);

        OriginFragment f = new OriginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, f);
        ft.commit();


    }

    public void liverdata(View v){

                image0.setImageResource(R.drawable.liverbutton1);
                image1.setImageResource(R.drawable.diabetesbutton);
                image2.setImageResource(R.drawable.kidneybutton);
                image3.setImageResource(R.drawable.weightbutton);
                image0.setEnabled(false);
                image1.setEnabled(false);
                image2.setEnabled(false);
                image3.setEnabled(false);

                final List<Jchart> lines = new ArrayList<Jchart>();

                for(int i=0;i<returedData.size();i++){

                    if(returedData.get(i).type==1){
                        lines.add(new Jchart(0,returedData.get(i).recordFloat,returedData.get(i).date.substring(5).replace("-","/"), Color.parseColor("#636363")));
                    }
                }
                if(lines.size()<11){
                    mLineChar.setScrollAble(true);
                    mLineChar.setVisibleNums(lines.size());
                }else{
                    mLineChar.setScrollAble(true);
                    mLineChar.setVisibleNums(10);
                }
                mLineChar.setYaxisValues(0, 8, 5);

                if(flag==0){
                    mLineChar.feedData(lines);
                    mLineChar.aniShow_growing();
                }else{

                    mLineChar.feedData(lines);
                    mLineChar.aniShow_growing();
                    flag=0;
                }

                HealthFragment f = HealthFragment.newInstance(liverData.get(0),diabetesData.get(0),kidneyData.get(0),dateData.get(0));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, f);
                ft.commit();

                image0.setEnabled(false);
                image1.setEnabled(true);
                image2.setEnabled(true);
                image3.setEnabled(true);

    }
    public void diabetesdata(View v){
        image0.setImageResource(R.drawable.liverbutton);
        image1.setImageResource(R.drawable.diabetesbutton1);
        image2.setImageResource(R.drawable.kidneybutton);
        image3.setImageResource(R.drawable.weightbutton);
        image0.setEnabled(false);
        image1.setEnabled(false);
        image2.setEnabled(false);
        image3.setEnabled(false);

        final List<Jchart> lines = new ArrayList<>();

        for(int i=0;i<returedData.size();i++){

            if(returedData.get(i).type==2){
                lines.add(new Jchart(0,returedData.get(i).recordFloat,returedData.get(i).date.substring(5).replace("-","/"), Color.parseColor("#b8e986")));
            }
        }
        if(lines.size()<11){
            mLineChar.setScrollAble(true);
            mLineChar.setVisibleNums(lines.size());
        }else{
            mLineChar.setScrollAble(true);
            mLineChar.setVisibleNums(10);
        }

        mLineChar.setYaxisValues(0, 2000, 5);
        if(flag==0){
            mLineChar.aniChangeData(lines);
        }else {

            mLineChar.feedData(lines);
            mLineChar.aniShow_growing();
            flag = 0;
        }
        HealthFragment f = HealthFragment.newInstance(liverData.get(0),diabetesData.get(0),kidneyData.get(0),dateData.get(0));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, f);
        ft.commit();

        image0.setEnabled(true);
        image1.setEnabled(false);
        image2.setEnabled(true);
        image3.setEnabled(true);

    }
    public void kidneydata(View v){
        image0.setImageResource(R.drawable.liverbutton);
        image1.setImageResource(R.drawable.diabetesbutton);
        image2.setImageResource(R.drawable.kidneybutton1);
        image3.setImageResource(R.drawable.weightbutton);
        image0.setEnabled(false);
        image1.setEnabled(false);
        image2.setEnabled(false);
        image3.setEnabled(false);

        final List<Jchart> lines = new ArrayList<>();

        for(int i=0;i<returedData.size();i++){

            if(returedData.get(i).type==3){
                lines.add(new Jchart(0,returedData.get(i).recordFloat,returedData.get(i).date.substring(5).replace("-","/"), Color.parseColor("#b8e986")));
            }
        }
        if(lines.size()<11){
            mLineChar.setScrollAble(true);
            mLineChar.setVisibleNums(lines.size());
        }else{
            mLineChar.setScrollAble(true);
            mLineChar.setVisibleNums(10);
        }

        mLineChar.setYaxisValues(0, 2000, 5);

        if(flag==0){
            mLineChar.aniChangeData(lines);
        }else {

            mLineChar.feedData(lines);
            mLineChar.aniShow_growing();
            flag = 0;
        }
        HealthFragment f = HealthFragment.newInstance(liverData.get(0),diabetesData.get(0),kidneyData.get(0),dateData.get(0));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, f);
        ft.commit();

        image0.setEnabled(true);
        image1.setEnabled(true);
        image2.setEnabled(false);
        image3.setEnabled(true);

    }

    public void weightdata(View v){
        image0.setImageResource(R.drawable.liverbutton);
        image1.setImageResource(R.drawable.diabetesbutton);
        image2.setImageResource(R.drawable.kidneybutton);
        image3.setImageResource(R.drawable.weightbutton1);
        image0.setEnabled(false);
        image1.setEnabled(false);
        image2.setEnabled(false);
        image3.setEnabled(false);

        final List<Jchart> lines = new ArrayList<>();

        flag=1;
        weightData=new ArrayList<HealthObject>();


        for(int i=0;i<returedData.size();i++){

            if(returedData.get(i).type==4){
                Log.e("tt", String.valueOf(returedData.get(i).recordFloat));
                weightData.add(returedData.get(i));
                lines.add(new Jchart(0,returedData.get(i).recordFloat,returedData.get(i).date.substring(5).replace("-","/"), Color.parseColor("#b8e986")));
            }
        }
        Log.e("tt","weight");


        if(lines.size()<11){
            mLineChar.setScrollAble(true);
            mLineChar.setVisibleNums(lines.size());
        }else{
            mLineChar.setScrollAble(true);
            mLineChar.setVisibleNums(10);
        }
        mLineChar.setYaxisValues(0, 2000, 5);

        flag=1;
        mLineChar.setYaxisValues(0, 100, 5);
        mLineChar.postInvalidate();
        mLineChar.feedData(lines);
        mLineChar.aniShow_growing();

        WeightFragment sf = WeightFragment.newInstance(weightData);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, sf);
        ft.commit();



        if(liverData.size()==0){
            image0.setEnabled(false);
            image1.setEnabled(false);
            image2.setEnabled(false);
        }else{
            image0.setEnabled(true);
            image1.setEnabled(true);
            image2.setEnabled(true);
            image3.setEnabled(false);
        }


    }




    public class AsyncHealthJSONList extends AsyncTask<String, Integer, ArrayList<HealthObject>>{
        @Override
        protected void onPreExecute(){
            drawChart();
            startProgress();
        }
        @Override
        protected    ArrayList<HealthObject> doInBackground(String... params) {

            Response response = null;
            OkHttpClient toClient;
                returedData=new ArrayList<>();
            liverData=new ArrayList<>();
            diabetesData=new ArrayList<>();
            kidneyData=new ArrayList<>();
            dateData=new ArrayList<>();
            try{
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_PET_SELECT+MySharedPreferencesManager.getInstance().getDogselect()+SERVER_URL_PET_HEALTH_ADDSELECT)
                        .get()
                        .build();

                response = toClient.newCall(request).execute();
                String returedJSON;
                Log.e("tt","Get");

                    returedJSON = response.body().string();
                    Log.e("resultJSON", returedJSON);
                    try {
                        JSONObject jsonObject = new JSONObject(returedJSON);
                        JSONArray jsonData=jsonObject.getJSONArray("data");
                        int jsonObjSize = jsonData.length();

                        for (int i = 0; i < jsonObjSize; i++) {
                            HealthObject entity = new HealthObject();
                            JSONObject jsData=jsonData.getJSONObject(i);
                            entity.id = jsData.getInt("id");
                            entity.type= jsData.getInt("recordType");
                            entity.recordFloat = jsData.getInt("recordFloat");
                            String createdAt=jsData.getString("createdAt");
                            entity.getDT(createdAt);
                            returedData.add(entity);
                        }

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
            for(int i=0;i<returedData.size();i++){

                if(returedData.get(i).type==1){
                    liverData.add(returedData.get(i).recordFloat);
                    dateData.add(returedData.get(i).date);
                }else if(returedData.get(i).type==2){
                    diabetesData.add(returedData.get(i).recordFloat);
                }else if(returedData.get(i).type==3){
                    kidneyData.add(returedData.get(i).recordFloat);
                }
            }
            return returedData;
        }
        @Override
        protected void onPostExecute(ArrayList<HealthObject> result) {
            progressOFF();
            returedData = result;
            if(returedData.size()!=0&&liverData.size()==0){
                weightdata(image0);
                image0.setEnabled(false);
                image1.setEnabled(false);
                image2.setEnabled(false);

            }else if(returedData.size()==0&&liverData.size()==0){

                image0.setEnabled(false);
                image1.setEnabled(false);
                image2.setEnabled(false);
            }else if(liverData.size()!=0){
                liverdata(image0);
            }



        }
    }





    private void startProgress() {
        progressON("Loading...");
    }

    public void progressON(String message) {
        BaseApplication.getInstance().progressON(this, message);
    }

    public void progressOFF() {
        BaseApplication.getInstance().progressOFF();
    }




    private class AsyncHospital extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;
            Log.e("tt","병원정보가져오자");
            try {
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_USER)
                        .get()
                        .build();
                response = toClient.newCall(request).execute();
                String returedJSON;
                Log.e("tt", "Get");

                returedJSON = response.body().string();
                Log.e("tt", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONObject j = jsonObject.getJSONObject("result");
                    JSONObject jsonData=j.getJSONObject("hospital");
                    MySharedPreferencesManager.getInstance().setHospitalselect(String.valueOf(jsonData.getInt("id")));

                    Log.e("tt","before: "+MySharedPreferencesManager.getInstance().getHospitalselect());


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
                    return null;
        }
    }
}