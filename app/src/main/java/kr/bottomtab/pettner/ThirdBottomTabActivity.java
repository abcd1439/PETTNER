package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by pyoinsoo on 2017-05-30.
 */

//세번째 하단탭을 클릭(터치)했을때의 Activity
public class ThirdBottomTabActivity extends BottomNavigationParentActivity {
    private BackPressCloseHandler backPressCloseHandler;
    Toolbar toolbar;
    MyFragmentPagerAdapter adapter;
    ViewPager pager;
    TabLayout tabLayout;

    TextView badge_notification_1;
/////////////////
    HospitalObject hospitalObject=new HospitalObject();
    ArrayList<String> sub=new ArrayList<>();
    ArrayList<String> date=new ArrayList<>();
    ArrayList<HospitalObject> recycleData=new ArrayList<>();
/////////////////
    ArrayList<QuestionObject> questionObject=new ArrayList<>();
/////////////////
    ArrayList<ReservationObject> reservObject=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.logo); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요

        toolbar.setTitle("의료코치");
        toolbar.setTitleTextColor(Color.WHITE);

        badge_notification_1=(TextView) findViewById(R.id.badge_notification_1);

        backPressCloseHandler = new BackPressCloseHandler(this);

        pager = (ViewPager) findViewById(R.id.pager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);


    }
    @Override
    protected void onResume(){
        super.onResume();
        if(MySharedPreferencesManager.getInstance().getBadgecount()>0){
            badge_notification_1.setText("!");
            badge_notification_1.setVisibility(View.VISIBLE);
        }
        new AsyncHospital().execute();
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

        public void addFragment(Fragment fragment, String tabTitle) {
            fragments.add(fragment);
            tabTitles.add(tabTitle);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCurrentActivityLayoutName() {
        return R.layout.activity_third_layout;
    }
    @Override
    public int getCurrentSelectedBottomMenuItemID() {
        return R.id.third_tab;
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
                Intent intent1=new Intent(ThirdBottomTabActivity.this,AlarmActivity.class);
                startActivity(intent1);
                break;
            case R.id.action_personal:
                Intent intent2=new Intent(ThirdBottomTabActivity.this,PersonalActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }

    private class AsyncHospital extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {


            Response response = null;
            OkHttpClient toClient;

            Log.e("tt","after: "+MySharedPreferencesManager.getInstance().getHospitalselect());
            try{
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_HOSPITAL_SELECT+MySharedPreferencesManager.getInstance().getHospitalselect())
                        .get()
                        .build();

                response = toClient.newCall(request).execute();
                String returedJSON;
                Log.e("tt","Get");

                returedJSON = response.body().string();
                Log.e("resultJSON", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONObject jsonData=jsonObject.getJSONObject("result");

                    hospitalObject.name=jsonData.getString("name");
                    hospitalObject.address=jsonData.getString("address");
                    hospitalObject.phone=jsonData.getString("tel");
                    hospitalObject.daysStart=jsonData.getString("daysStart");
                    hospitalObject.daysFinish=jsonData.getString("daysFinish");
                    hospitalObject.endStart=jsonData.getString("endStart");
                    hospitalObject.endFinish=jsonData.getString("endFinish");
                    hospitalObject.daysLunchStart=jsonData.getString("daysLunchStart");
                    hospitalObject.daysLunchFinish=jsonData.getString("daysLunchFinish");
                    hospitalObject.endLunchStart=jsonData.getString("endLunchStart");
                    hospitalObject.endLunchFinish=jsonData.getString("endLunchFinish");
                    hospitalObject.photomain=jsonData.getString("resizePhotoUrl");
                    hospitalObject.introduce=jsonData.getString("introduce");
                    JSONArray js1=jsonData.getJSONArray("hospitalSubjects");
                    int jsSize1=js1.length();
                    for(int i=0;i<jsSize1;i++){
                        JSONObject joData1=js1.getJSONObject(i);
                        sub.add(joData1.getString("subject"));
                    }
                    hospitalObject.setSubject(sub);
                    JSONArray js2=jsonData.getJSONArray("hospitalVacations");
                    int jsSize2=js2.length();
                    for(int i=0;i<jsSize2;i++){
                        JSONObject joData2=js2.getJSONObject(i);
                        date.add(joData2.getString("vacationDate"));
                    }
                    hospitalObject.setVacation(date);

                    JSONArray js3=jsonData.getJSONArray("vets");
                    int jsSize3=js3.length();
                    for(int i=0;i<jsSize3;i++){
                        HospitalObject reData=new HospitalObject();
                        JSONObject joData=js3.getJSONObject(i);
                        reData.name=joData.getString("name");
                        reData.hospitalSubjects=joData.getString("subject");
                        reData.photosub=joData.getString("resizePhotoUrl");
                        recycleData.add(reData);
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


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            new AsyncReservation().execute();


        }
    }


    private class AsyncReservation extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;
            sub=new ArrayList<>();
            date=new ArrayList<>();

            try {
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_HOSPITAL_RESERVATION_ADDSELECT)
                        .get()
                        .build();

                response = toClient.newCall(request).execute();
                String returedJSON;
                Log.e("tt", "Get");

                returedJSON = response.body().string();
                Log.e("resultJSON", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONArray jsonData = jsonObject.getJSONArray("data");
                    int jsonObjSize = jsonData.length();

                    for (int i = 0; i < jsonObjSize; i++) {
                        ReservationObject reserv=new ReservationObject();
                        JSONObject jsData=jsonData.getJSONObject(i);
                        reserv.resDatetime=jsData.getString("resDatetime");
                        reserv.status=jsData.getInt("status");
                        reserv.id=jsData.getString("id");
                        reserv.name=jsData.getString("name");

                        reservObject.add(reserv);


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

            return null;
        }
        @Override
        protected void onPostExecute(String result){
            new AsyncQuestion().execute();
        }
    }



    private class AsyncQuestion extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;
            sub=new ArrayList<>();
            date=new ArrayList<>();

            try {
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_QUESTION_ADDSELECT)
                        .get()
                        .build();

                response = toClient.newCall(request).execute();
                String returedJSON;
                Log.e("tt", "Get");

                returedJSON = response.body().string();
                Log.e("resultJSON", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONArray jsonData = jsonObject.getJSONArray("data");
                    int jsonObjSize = jsonData.length();

                    for (int i = 0; i < jsonObjSize; i++) {
                        QuestionObject q=new QuestionObject();
                        JSONObject jsData=jsonData.getJSONObject(i);
                        q.question=jsData.getString("question");
                        q.date=jsData.getString("createdAt").substring(0,10);
                        q.id=jsData.getInt("id");

                        q.name=jsData.getString("name");

                        /*JSONArray j2=jsData.getJSONArray("qnaSubs");

                        q.subNum=j2.length();*/

                        questionObject.add(q);
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

                    return null;
        }
        @Override
        protected void onPostExecute(String result){

            adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(HospitalFragment.newInstance("주치병원",hospitalObject,recycleData), "주치병원");
            adapter.addFragment(ReservationFragment.newInstance("예약관리",reservObject), "예약관리");
            adapter.addFragment(QuestionFragment.newInstance("Q&A",questionObject), "Q&A");
            pager.setAdapter(adapter);
            tabLayout.setupWithViewPager(pager);
        }


    }
}
