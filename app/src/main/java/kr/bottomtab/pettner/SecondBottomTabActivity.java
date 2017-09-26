package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by pyoinsoo on 2017-05-30.
 */

//두번째 하단탭을 클릭(터치)했을때의 Activity
public class SecondBottomTabActivity extends BottomNavigationParentActivity {
    private BackPressCloseHandler backPressCloseHandler;
    Toolbar toolbar;

    static final int NUM_PAGES = 3;

    int size;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    LinearLayout circles;
    TextView badge_notification_1;
    Button skip;
    Button done;
    ImageView photo;
    ImageButton next;
    boolean isOpaque = true;

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

        toolbar.setTitle("키트검사");
        toolbar.setTitleTextColor(Color.WHITE);

        backPressCloseHandler = new BackPressCloseHandler(this);
        new Asynccount().execute();

        badge_notification_1=(TextView) findViewById(R.id.badge_notification_1);



        skip = Button.class.cast(findViewById(R.id.skip));
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SecondBottomTabActivity.this,KitActivity.class);
                startActivity(i);
            }
        });

        next = ImageButton.class.cast(findViewById(R.id.next));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });

        done = Button.class.cast(findViewById(R.id.done));
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SecondBottomTabActivity.this,KitActivity.class);
                startActivity(i);
            }
        });


        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == NUM_PAGES - 1 && positionOffset > 0) {
                    if (isOpaque) {
                        pager.setBackgroundColor(Color.TRANSPARENT);
                        isOpaque = false;
                    }
                } else {
                    if (!isOpaque) {
                        pager.setBackgroundColor(Color.GRAY);
                        isOpaque = true;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                if (position == NUM_PAGES-1) {
                    skip.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);
                } else{
                    skip.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    done.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buildCircles();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(MySharedPreferencesManager.getInstance().getBadgecount()>0){
            badge_notification_1.setText("!");
            badge_notification_1.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pager!=null){
            pager.clearOnPageChangeListeners();
        }
    }


    private void buildCircles(){
        circles = LinearLayout.class.cast(findViewById(R.id.circles));

        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for(int i = 0 ; i < NUM_PAGES ; i++){
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.drawable.ic_swipe_indicator_white_18dp);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            circles.addView(circle);
        }

        setIndicator(0);
    }

    private void setIndicator(int index){
        if(index < NUM_PAGES){
            for(int i = 0 ; i < NUM_PAGES  ; i++){
                ImageView circle = (ImageView) circles.getChildAt(i);
                if(i == index){
                    circle.setColorFilter(Color.rgb(33,125,251));
                }else {
                    circle.setColorFilter(Color.GRAY);
                }
            }
        }
    }

    private void endTutorial(){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            backPressCloseHandler.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ProductTourFragment tp = null;
            switch(position){
                case 0:
                    tp = ProductTourFragment.newInstance(R.layout.fragment_guide1);
                    break;
                case 1:
                    tp = ProductTourFragment.newInstance(R.layout.fragment_guide2);
                    break;
                case 2:
                    tp = ProductTourFragment.newInstance(R.layout.fragment_guide3);
                    break;
            }

            return tp;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }



    @Override
    public int getCurrentActivityLayoutName() {
        return R.layout.activity_second_layout;
    }
    @Override
    public int getCurrentSelectedBottomMenuItemID() {
        return R.id.second_tab;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_items2, menu);
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
                Intent intent1=new Intent(SecondBottomTabActivity.this,AlarmActivity.class);
                startActivity(intent1);
                break;
            case R.id.action_personal:
                Intent intent2=new Intent(SecondBottomTabActivity.this,PersonalActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_photo:
                Intent intent3=new Intent(SecondBottomTabActivity.this,ColorBlobDetectionActivity.class);
                startActivity(intent3);
                break;
        }
        return true;
    }

    private class Asynccount extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;


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
                Log.e("resultJSON", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONObject j = jsonObject.getJSONObject("result");
                    JSONArray ja = j.getJSONArray("animals");

                    size = ja.length();
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
            if (size == 0) {
                Toast.makeText(BaseApplication.getContext(), "강아지를 등록하셔야 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SecondBottomTabActivity.this, PersonalAddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }
}
