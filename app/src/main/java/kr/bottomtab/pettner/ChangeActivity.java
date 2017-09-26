package kr.bottomtab.pettner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by user on 2017-06-05.
 */

public class ChangeActivity extends AppCompatActivity {
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    public static final String PREF_NAME = "prefs";
    public static final String KEY_DOGSELECT= "dogselect";

    int size;
    String id;
    Toolbar toolbar;
    RecyclerView recyclerView;
    public MyAdapter adapter;
    ArrayList<PersonalObject> personal=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요

        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Asynccount().execute();

            }
        });
        mPrefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();


        recyclerView = (RecyclerView)findViewById(R.id.recycler_list);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);






    }

    @Override
    public void onResume(){
        super.onResume();
        new AsyncPet().execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent i= new Intent(this, PersonalActivity.class);
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
        Intent i = new Intent(this, PersonalActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        ArrayList<String> name=new ArrayList<>();
        ArrayList<String> photo=new ArrayList<>();
        View view;

        public MyAdapter(ArrayList<PersonalObject> p) {
            for(int i=0;i<p.size();i++){
                name.add(p.get(i).name);
                photo.add(p.get(i).photo);
            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView profile;
            TextView dogname;
            public ViewHolder(View v) {
                super(v);
                dogname=(TextView) v.findViewById(R.id.dogname);

                profile=(ImageView) v.findViewById(R.id.profile);

            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_change, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            holder.dogname.setText(name.get(position));
            Glide.with(ChangeActivity.this).load(photo.get(position)).centerCrop().override(95,95).error(R.drawable.dog_bprofile).into(holder.profile);

            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncDog().execute(Integer.toString(position));

                }
            });
        }
        @Override
        public int getItemCount() {
            return name.size();
        }
    }

    private class AsyncPet extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;


            try{
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_PET_ADD)
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

                    int jsize=jsonData.length();
                    for(int i=0;i<jsize;i++){
                        PersonalObject p=new PersonalObject();
                        JSONObject json=jsonData.getJSONObject(i);
                        p.name=json.getString("name");
                        p.photo=json.getString("resizePhotoUrl");
                        personal.add(p);
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
            adapter = new MyAdapter(personal);
            recyclerView.setAdapter(adapter);
        }

    }
    private class AsyncDog extends AsyncTask<String,Integer,String>{
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

                    JSONObject jsData=ja.getJSONObject(Integer.parseInt(params[0]));
                    id= String.valueOf(jsData.getInt("id"));


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
            MySharedPreferencesManager.getInstance().setDogselect(id);


            Intent intent=new Intent(ChangeActivity.this,PersonalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
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
            if(size==3){
                Toast.makeText(BaseApplication.getContext(),"최대 3마리만 등록할 수 있습니다.",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent=new Intent(ChangeActivity.this,PersonalAddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }
}

