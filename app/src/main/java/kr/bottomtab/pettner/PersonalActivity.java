package kr.bottomtab.pettner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;
import static kr.bottomtab.pettner.NetworkDefineConstant.SERVER_URL_PET_EXAMINE_SELECT;
import static kr.bottomtab.pettner.NetworkDefineConstant.SERVER_URL_PET_HEALTH_ADDSELECT;

/**
 * Created by user on 2017-06-05.
 */

public class PersonalActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private CircleImageView mProfileImage,changeBtn;
    private int mMaxScrollSize;
    TextView text1,text2,text3;
    String name,type,sex,age,photo;
    int id;
    ViewPager viewPager;
    TabLayout tabLayout;

    ArrayList<HealthObject> liverData;
    ArrayList<HealthObject> diabetesData;
    ArrayList<HealthObject> kidneyData;

    int size;

    ArrayList<PrescriptObject> prescript=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);




        tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
        viewPager  = (ViewPager) findViewById(R.id.materialup_viewpager);
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);






        changeBtn=(CircleImageView) findViewById(R.id.change_image);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(PersonalActivity.this, ChangeActivity.class);
                startActivity(i);
            }
        });


        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();


        ImageView back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(PersonalActivity.this, FirstBottomTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        ImageView setting=(ImageView) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Asynccount().execute();
            }
        });


    }


    @Override
    public void onResume(){
        super.onResume();
        new AsyncPersonal().execute();
        new AsyncPrescript().execute();

    }



    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
            changeBtn.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
            changeBtn.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
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

    public class AsyncPersonal extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {


            Response response = null;
            OkHttpClient toClient;


            try{
                Log.e("tt",MySharedPreferencesManager.getInstance().getDogselect());
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_PET_SELECT+MySharedPreferencesManager.getInstance().getDogselect())
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



                    name = jsonData.getString("name");
                    type= jsonData.getString("type");
                    sex = jsonData.getString("sex");
                    age=jsonData.getString("age");
                    photo=jsonData.getString("resizePhotoUrl");



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

            text1=(TextView)findViewById(R.id.text1);
            text1.setText(name);
            text2=(TextView) findViewById(R.id.text2);
            text2.setText(type);
            text3=(TextView) findViewById(R.id.text3);
            if(age==null){
                text3.setText("강아지를 등록시켜주세요.");
            }else {
                text3.setText(age + " | " + "소형견" + " | " + sex);
            }
            mProfileImage = (CircleImageView) findViewById(R.id.profile_image);
            Glide.with(PersonalActivity.this).load(photo).centerCrop().override(95,95).error(R.drawable.dog_sprofile).into(mProfileImage);

        }
    }


    private class AsyncPrescript extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;


            try {
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_PET_SELECT+MySharedPreferencesManager.getInstance().getDogselect()+SERVER_URL_PET_EXAMINE_SELECT)
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

                    int jsize = jsonData.length();
                    for (int i = 0; i < jsize; i++) {
                        PrescriptObject pres = new PrescriptObject();
                        JSONObject j = jsonData.getJSONObject(i);

                        pres.problem = j.getString("problem");
                        pres.answer = j.getString("answer");
                        pres.getDT(j.getString("prescriptDate"));

                        prescript.add(pres);
                    }


                } catch (JSONException jsone) {
                    Log.e("json에러", jsone.toString());
                }


            } catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            } finally {
                if (response != null) {
                    response.close(); //3.* 이상에서는 반드시 닫아 준다.
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            new AsyncHealth().execute();

        }
    }
    private class AsyncHealth extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;

            liverData=new ArrayList<>();
            diabetesData=new ArrayList<>();
            kidneyData=new ArrayList<>();


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

                        entity.type= jsData.getInt("recordType");
                        entity.recordFloat = jsData.getInt("recordFloat");
                        if(jsData.getString("prescriptIcon")=="null"){
                            entity.prescriptIcon="5";
                        }else{
                            entity.prescriptIcon=jsData.getString("prescriptIcon");
                        }

                        entity.prescriptText=jsData.getString("prescriptText");
                        String createdAt=jsData.getString("createdAt");
                        entity.getDT(createdAt);


                        if(!jsData.isNull("vet")){
                            JSONObject j=jsData.getJSONObject("vet");
                            entity.doctor=j.getString("name");
                        }



                        if(entity.type==1){
                            liverData.add(entity);
                        }else if(entity.type==2){
                            diabetesData.add(entity);
                        }else if(entity.type==3){
                            kidneyData.add(entity);
                        }
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
            MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getSupportFragmentManager());

            adapter.addFragment(ExamineFragment.newInstance("진료기록",prescript), "진료기록");
            adapter.addFragment(ResultFragment.newInstance("검사결과",liverData,diabetesData,kidneyData), "검사결과");
            viewPager.setAdapter(adapter);

            tabLayout.setupWithViewPager(viewPager);

        }
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
        Intent i = new Intent(this, ThirdBottomTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    private class Asynccount extends AsyncTask<String,Integer,String>{
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
                Log.e("tt","Get");

                returedJSON = response.body().string();
                Log.e("resultJSON", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONObject j=jsonObject.getJSONObject("result");
                    JSONArray ja=j.getJSONArray("animals");

                    size=ja.length();
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
            if(size==0){
                Toast.makeText(BaseApplication.getContext(),"강아지를 등록하셔야 합니다.",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PersonalActivity.this,PersonalAddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else{
                Intent i= new Intent(PersonalActivity.this, PersonalEditActivity.class);
                startActivity(i);
            }
        }
    }


}