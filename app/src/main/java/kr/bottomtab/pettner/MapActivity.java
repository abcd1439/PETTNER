package kr.bottomtab.pettner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bottomtab.ultraviewpager.UltraViewPager;
import com.bottomtab.ultraviewpager.transformer.UltraDepthScaleTransformer;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback ,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,AdapterView.OnItemSelectedListener, View.OnClickListener {

    private UltraViewPager ultraViewPager;
    private PagerAdapter adapter;
    private UltraViewPager.Orientation gravity_indicator;



    ArrayList<HospitalLocationObject> location=new ArrayList<>();

    GoogleMap map;

    Double lat;
    Double lng;

    ImageView loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요




        toolbar.setTitle("병원찾기");
        toolbar.setTitleTextColor(Color.WHITE);


        ultraViewPager = (UltraViewPager) findViewById(R.id.ultra_viewpager);

        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        adapter = new UltraPagerAdapter(true);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMultiScreen(0.6f);
        ultraViewPager.setItemRatio(1.0f);
        ultraViewPager.setAutoMeasureHeight(true);
        gravity_indicator = UltraViewPager.Orientation.HORIZONTAL;
        ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());


        ultraViewPager.setInfiniteLoop(true);
        ultraViewPager.initIndicator();
        ultraViewPager.getIndicator().setOrientation(gravity_indicator);



        //구글 맵 지도를 꺼낸다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);


        ImageView searchBtn = (ImageView)findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                lat = map.getCameraPosition().target.latitude;
                lng = map.getCameraPosition().target.longitude;
                Log.e("tt", String.valueOf(lat) + " " + String.valueOf(lng));

                new HospitalAsync().execute();

            }
        });

    }






    //마커 추가
    private void addMarker(LatLng position, HospitalLocationObject data) {

        MarkerOptions options = new MarkerOptions();

        options.position(position); //위도 경도위치
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker);
        options.icon(icon);
        options.anchor(0.5f, 1);
        String oo;
        if(data.onOff==0){
            oo="CLOSED";
        }else{
            oo="OPEN";
        }
        options.title(data.name+"("+oo+")");//타이틀
        options.snippet(data.introduce);
        Log.e("tt",data.name);
        options.draggable(true);

        map.addMarker(options).showInfoWindow();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                  != PackageManager.PERMISSION_GRANTED) {
            return;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;



        map.getUiSettings().setZoomControlsEnabled(true);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4741475279, 127.0496880232),14));

        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }








    }

    private static final String TAG = "MainActivity";


    @Override
    public boolean onMarkerClick(Marker marker) { //마커가 클릭되면 현재 클릭된 마커를 지도의 가운데로 이동
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        map.animateCamera(center);

        marker.showInfoWindow();
        return true;
    }





    @Override
    public void onInfoWindowClick(Marker marker) {
        /*Toast.makeText(this, "Info window 클릭함!", Toast.LENGTH_SHORT).show();
        marker.hideInfoWindow();*/
    }














    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent i= new Intent(MapActivity.this, ThirdBottomTabActivity.class);
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
        Intent i = new Intent(MapActivity.this, ThirdBottomTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }






    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (ultraViewPager.getIndicator() == null) {
            ultraViewPager.initIndicator();
            ultraViewPager.getIndicator().setOrientation(gravity_indicator);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        ultraViewPager.getIndicator().build();
    }

    public class UltraPagerAdapter extends PagerAdapter {
        private boolean isMultiScr;
        private int selectedPosition=-1;
        private ImageView selectedHeart;

        public UltraPagerAdapter(boolean isMultiScr) {
            this.isMultiScr = isMultiScr;
        }

        @Override
        public int getCount() {
            return location.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.layout_pagerchild, null);
            //new LinearLayout(container.getContext());
            ImageView image= (ImageView) linearLayout.findViewById(R.id.image);
            TextView text=(TextView)linearLayout.findViewById(R.id.text);
            final ImageView heart=(ImageView) linearLayout.findViewById(R.id.heart);
            linearLayout.setId(R.id.item_id);

            if(position%getCount()==selectedPosition){
                heart.setImageResource(R.drawable.icon_aheart);
            }
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedHeart!=null) {
                        selectedHeart.setImageResource(R.drawable.icon_heart);
                    }
                    heart.setImageResource(R.drawable.icon_aheart);
                    selectedHeart=heart;
                    selectedPosition=position%getCount();

                    String hospital=location.get(position).id;
                    new AsyncHospital().execute(hospital);
                    Log.e("tt", "clicked!");
                }

            });
            Glide.with(MapActivity.this).load(location.get(position).resizePhotoUrl).centerCrop().error(R.drawable.origin_back).into(image);
            text.setText(location.get(position).name);

            container.addView(linearLayout);
            return linearLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            LinearLayout view = (LinearLayout) object;
            container.removeView(view);
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }
    }

    private class HospitalAsync extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;

            String uri = Uri.parse(NetworkDefineConstant.SERVER_URL_HOSPITAL)
                    .buildUpon()
                    .appendQueryParameter("geoCordLat", String.valueOf(lat))
                    .appendQueryParameter("geoCordLng", String.valueOf(lng))

                    .build().toString();
            Log.e("tt", uri);
            try{
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(uri)
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

                    location.clear();
                    for(int i=0;i<jsonData.length();i++){
                        HospitalLocationObject h=new HospitalLocationObject();
                        JSONObject j=jsonData.getJSONObject(i);

                        h.longitude=j.getDouble("lng");
                        h.latitude=j.getDouble("lat");
                        h.name=j.getString("name");
                        h.introduce=j.getString("introduce");
                        h.onOff=j.getInt("onOff");
                        h.name=j.getString("name");
                        h.resizePhotoUrl=j.getString("resizePhotoUrl");
                        h.id=j.getString("id");

                        location.add(h);

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
        protected void onPostExecute(String a){
            map.clear();
            for(int i=0;i<location.size();i++){
                LatLng latLng = new LatLng(location.get(i).latitude, location.get(i).longitude);
                Log.e("tt",String.valueOf(latLng));
                addMarker(latLng, location.get(i));


                ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                adapter = new UltraPagerAdapter(true);
                ultraViewPager.setAdapter(adapter);
                ultraViewPager.setMultiScreen(0.6f);
                ultraViewPager.setItemRatio(1.0f);
                ultraViewPager.setAutoMeasureHeight(true);
                gravity_indicator = UltraViewPager.Orientation.HORIZONTAL;
                ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());


                ultraViewPager.setInfiniteLoop(true);
                ultraViewPager.initIndicator();
                ultraViewPager.getIndicator().setOrientation(gravity_indicator);

            }


        }
    }

    private class AsyncHospital extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            String id=params[0];
            MySharedPreferencesManager.getInstance().setHospitalselect(id);
            boolean flag;
            Response response = null;
            OkHttpClient toServer;
            try{
                toServer = OkHttpInitSingtonManager.getOkHttpClient();
                //요청 Form세팅
                RequestBody postBody = new FormBody.Builder()
                        .add("hospitalId", id)

                        .build();


                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_USER)
                        .post(postBody)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                flag = response.isSuccessful();
                String returedJSON;
                if( flag ){ //성공했다면
                    returedJSON = response.body().string();
                    Log.e("tt", returedJSON);

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

            return null;
        }
        @Override
        protected void onPostExecute(String result){
            Intent i=new Intent(MapActivity.this,ThirdBottomTabActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }

}
