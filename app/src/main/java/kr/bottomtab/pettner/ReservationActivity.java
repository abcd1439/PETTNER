package kr.bottomtab.pettner;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by user on 2017-05-30.
 */

public class ReservationActivity extends AppCompatActivity {
    ImageView cdatebtn;
    Button[] reservButton = new Button[20];
    TextView displaydate;
    Calendar c = Calendar.getInstance();
    int cdate, cmonth, cyear, chour, cmin, cflag;
    int year, month, date;
    String m;
    RecyclerView recyclerView;
    MyAdapter adapter;

    ArrayList<String> dd=new ArrayList<>();

    ImageView memberImage, reservbtn;
    EditText memo;

    Toolbar toolbar;

    int timecount=0,subjectcount=0;
    ReservationObject reserv = new ReservationObject();
    ArrayList<PersonalObject> personal = new ArrayList<>();

    private static final int PICK_FROM_GALLERY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        memberImage = (ImageView) findViewById(R.id.memberImage);
        reservButton[0] = (Button) findViewById(R.id.btn1);
        reservButton[1] = (Button) findViewById(R.id.btn2);
        reservButton[2] = (Button) findViewById(R.id.btn3);
        reservButton[3] = (Button) findViewById(R.id.btn4);
        reservButton[4] = (Button) findViewById(R.id.btn5);
        reservButton[5] = (Button) findViewById(R.id.btn6);
        reservButton[6] = (Button) findViewById(R.id.btn7);
        reservButton[7] = (Button) findViewById(R.id.btn8);
        reservButton[8] = (Button) findViewById(R.id.btn9);
        reservButton[9] = (Button) findViewById(R.id.btn10);
        reservButton[10] = (Button) findViewById(R.id.btn11);
        reservButton[11] = (Button) findViewById(R.id.btn12);
        reservButton[12] = (Button) findViewById(R.id.btn13);
        reservButton[13] = (Button) findViewById(R.id.btn14);
        reservButton[14] = (Button) findViewById(R.id.btn15);
        reservButton[15] = (Button) findViewById(R.id.btn16);
        reservButton[16] = (Button) findViewById(R.id.btn17);
        reservButton[17] = (Button) findViewById(R.id.btn18);
        reservButton[18] = (Button) findViewById(R.id.btn19);
        reservButton[19] = (Button) findViewById(R.id.btn20);


        for (int i = 0; i < 15; i++) {
            reservButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button newButton = (Button) v;
                    for (int i = 0; i < 15; i++) {
                        if (reservButton[i].isEnabled()) {
                            Button tempButton = reservButton[i];

                            if (tempButton == newButton) {
                                newButton.setTextColor(Color.WHITE);
                                newButton.setBackgroundColor(Color.rgb(255, 64, 129));
                                reservButton[i].setTag("1");
                            } else {
                                tempButton.setTextColor(Color.rgb(13, 94, 205));
                                tempButton.setBackgroundColor(Color.rgb(218, 235, 254));
                                reservButton[i].setTag("2");
                            }
                        }
                    }
                }
            });
        }
        for (int i = 15; i < 20; i++) {
            reservButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button newButton = (Button) v;
                    for (int i = 15; i < 20; i++) {
                        Button tempButton = reservButton[i];
                        if (tempButton == newButton) {
                            newButton.setTextColor(Color.WHITE);
                            newButton.setBackgroundColor(Color.rgb(255, 64, 129));
                            reservButton[i].setTag("1");
                        } else {
                            tempButton.setTextColor(Color.rgb(13, 94, 205));
                            tempButton.setBackgroundColor(Color.rgb(218, 235, 254));
                            reservButton[i].setTag("2");
                        }
                    }
                }
            });
        }


        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        toolbar.setTitle("예약하기");
        toolbar.setTitleTextColor(Color.WHITE);


        cdatebtn = (ImageView) findViewById(R.id.button);
        displaydate = (TextView) findViewById(R.id.textView);


        year = c.get(c.YEAR);
        month = c.get(c.MONTH) + 1;
        m = Integer.toString(month);
        if (m.length() == 1) {
            m = "0" + m;
        }

        date = c.get(c.DATE);
        chour = c.get(c.HOUR);
        cmin = c.get(c.MINUTE);
        cflag = c.get(c.AM_PM);

        if (cflag == 1) {
            chour += 12;
        }

        displaydate.setText(year + "년 " + month + "월 " + date + "일");

        for (int i = 0; i < 15; i++) {
            String a = (String) reservButton[i].getText();
            String h = a.substring(0, 2);
            String m = a.substring(3, 5);

            if (Integer.parseInt(h + m) < Integer.parseInt(chour + "" + cmin)) {
                reservButton[i].setTextColor(Color.rgb(149, 149, 149));
                reservButton[i].setBackgroundColor(Color.rgb(236, 236, 236));
                reservButton[i].setEnabled(false);
            } else {
                break;
            }
        }


        cdatebtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                new DatePickerDialog(ReservationActivity.this, d1, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }

        });


        memberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //이 인텐트는 될 수있으면 쓰지 말기 바람
                //  intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setAction(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_GALLERY);
            }
        });


        memo = (EditText) findViewById(R.id.memo);
        reservbtn = (ImageView) findViewById(R.id.reservbtn);

        reservbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 15; i++) {
                    if (reservButton[i].getTag() == "1") {
                        reserv.resDatetime = year + "-" + m + "-" + date + "T" + reservButton[i].getText().toString() + ":00.000Z";
                    }else{
                        timecount++;
                    }
                }
                for (int i = 15; i < 20; i++) {
                    if (reservButton[i].getTag() == "1") {
                        reserv.subject = reservButton[i].getText().toString();
                    }
                    else{
                        subjectcount++;
                    }
                }
                reserv.memo = memo.getText().toString();

                reserv.photo = fileLocation;


                HashSet<Integer> a = adapter.getHash();
                Iterator<Integer> t = a.iterator();

                while (t.hasNext()) {
                    reserv.setIdList(t.next());
                }



                if(timecount==15||subjectcount==5||reserv.memo.length()==0||fileLocation==null){
                    Toast.makeText(ReservationActivity.this,"입력이 정확한지 확인해주세요.",Toast.LENGTH_SHORT).show();
                    timecount=0;
                    subjectcount=0;
                }else{
                    new ReservationAdd().execute(reserv);

                    /*Intent i = new Intent(ReservationActivity.this, ThirdBottomTabActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();*/
                }

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        new AsyncReserv().execute();
        new AsyncTime().execute();


    }

    DatePickerDialog.OnDateSetListener d1 = new DatePickerDialog.OnDateSetListener() {

        @Override

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


            cdate = dayOfMonth;

            cmonth = monthOfYear + 1;

            cyear = year;

            displaydate.setText(cyear + "년 " + cmonth + "월 " + cdate + "일");

            chour = c.get(c.HOUR);
            cmin = c.get(c.MINUTE);

            cflag = c.get(c.AM_PM);

            if (cflag == 1) {
                chour += 12;
            }
            if (Integer.parseInt(year + "" + month + "" + date) == Integer.parseInt(cyear + "" + cmonth + "" + cdate)) {
                for (int i = 0; i < 15; i++) {
                    String a = (String) reservButton[i].getText();
                    String h = a.substring(0, 2);
                    String m = a.substring(3, 5);

                    if (Integer.parseInt(h + m) < Integer.parseInt(chour + "" + cmin)) {
                        reservButton[i].setTextColor(Color.rgb(149, 149, 149));
                        reservButton[i].setBackgroundColor(Color.rgb(236, 236, 236));
                        reservButton[i].setEnabled(false);
                    } else {
                        break;
                    }
                }
            } else if (Integer.parseInt(year + "" + month + "" + date) < Integer.parseInt(cyear + "" + cmonth + "" + cdate)) {
                for (int i = 0; i < 15; i++) {
                    reservButton[i].setTextColor(Color.rgb(13, 94, 205));
                    reservButton[i].setBackgroundColor(Color.rgb(218, 235, 254));
                    reservButton[i].setEnabled(true);
                }
            } else {
                for (int i = 0; i < 15; i++) {
                    reservButton[i].setTextColor(Color.rgb(149, 149, 149));
                    reservButton[i].setBackgroundColor(Color.rgb(236, 236, 236));
                    reservButton[i].setEnabled(false);
                }
            }
            for (int i = 0; i<dd.size(); i++) {
                String a="";
                if(String.valueOf(cmonth).length()==1){
                    a="0"+cmonth;
                }else{
                    a=String.valueOf(cmonth);
                }
                if ((cyear + "-" + a + "-" + cdate).equals(dd.get(i))){
                    for (int j = 0; j < 19; j++) {
                        reservButton[j].setTextColor(Color.rgb(149, 149, 149));
                        reservButton[j].setBackgroundColor(Color.rgb(236, 236, 236));
                        reservButton[j].setEnabled(false);
                    }
                }
            }


        }

    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent i = new Intent(this, ThirdBottomTabActivity.class);
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
        Intent i = new Intent(this, ThirdBottomTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    //업로드 전에 가져올 최종적으로 가져올 이미지의 절대주소
    private String fileLocation;

    @Override
    public void onResume() {
        super.onResume();
        if (!isSDCardAvailable()) {
            Toast.makeText(this, "SD 카드가 없어 종료 합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String currentAppPackage = getPackageName();

        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);

        checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                /*Toast.makeText(getApplication(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();*/
            }
        }
    }

    //현재 단말기의 SD카드가 마운트되지 않았거나 읽지전용임을 나타냄
    private boolean isSDCardAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static final String TAG = "PYO_FILE_UPLOAD";

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri returedImgURI;
        if (resultCode != Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_GALLERY: {
                returedImgURI = data.getData();
                if (returedImgURI != null) {
                    memberImage.setImageURI(returedImgURI);

                    //업로드 할 수 있도록 절대 주소를 알아낸다.
                    if (findImageFileNameFromUri(returedImgURI)) {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 성공");
                    } else {
                        Log.e(TAG, " 갤러리에서 절대주소 Pick 실패");
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");
                    if (tempSavedBitmapFile(returedBitmap)) {
                        Log.e(TAG, "갤러리에서 Uri값이 없어 실제 파일로 저장 성공");
                    } else {
                        Log.e(TAG, "갤러리에서 Uri값이 없어 실제 파일로 저장 실패");
                    }
                }
                //crop이 필요하면 여기서 다시 호출
                // cropUpLoadImageIntent(currentSelectedUri);
                break;
            }
        }
    }

    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //사용자가 퍼미션을 OK했을 경우
                } else {
                    Log.d("파일업로드", "Permission always deny");
                    //사용자가 퍼미션을 거절했을 경우
                }
                break;
        }
    }

    private boolean tempSavedBitmapFile(Bitmap tempBitmap) {
        boolean flag = false;
        try {
            String tempName = "upload_" + (System.currentTimeMillis() / 1000);
            String fileSuffix = ".jpg";
            //임시파일을 실행한다.(현재입이 종료되면 스스로 삭제)
            File tempFile = File.createTempFile(
                    tempName,            // prefix
                    fileSuffix,                   // suffix
                    myImageDir                   // directory
            );
            final FileOutputStream bitmapStream = new FileOutputStream(tempFile);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapStream);
            if (bitmapStream != null) {
                bitmapStream.close();
            }
            fileLocation = tempFile.getAbsolutePath();
        } catch (IOException i) {
            Log.e("저장중 문제발생", i.toString(), i);
        }
        return flag;
    }

    private boolean findImageFileNameFromUri(Uri tempUri) {
        boolean flag = false;

        //실제 Image Uri의 절대이름
        String[] IMAGE_DB_COLUMN = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = null;
        try {
            //Primary Key값을 추출
            String imagePK = String.valueOf(ContentUris.parseId(tempUri));
            //Image DB에 쿼리를 날린다.
            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_DB_COLUMN,
                    MediaStore.Images.Media._ID + "=?",
                    new String[]{imagePK}, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                fileLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                flag = true;
            }
        } catch (SQLiteException sqle) {
            Log.e("findImage....", sqle.toString(), sqle);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return flag;
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        ArrayList<PersonalObject> personalArray = new ArrayList<>();
        HashSet<Integer> selectedId = new HashSet<>();
        int i = 0;

        MyAdapter(ArrayList<PersonalObject> p) {
            this.personalArray = p;

        }

        public HashSet<Integer> getHash() {
            return selectedId;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView profile;
            TextView dogname;
            RelativeLayout pan;
            ViewHolder(View v) {
                super(v);
                dogname = (TextView) v.findViewById(R.id.dogname);

                profile = (ImageView) v.findViewById(R.id.profile);
                pan=(RelativeLayout) v.findViewById(R.id.pan);

            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_circle, parent, false);

            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
            final PersonalObject personalObject = this.personalArray.get(position);
            holder.dogname.setText(personalObject.name);
            Glide.with(ReservationActivity.this).load(personalObject.photo).centerCrop().override(95, 95).error(R.drawable.icon_profile).into(holder.profile);
            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (i == 0) {
                        holder.pan.setBackgroundColor(Color.rgb(255, 64, 129));
                        holder.dogname.setTextColor(Color.WHITE);
                        selectedId.add(personalObject.id);
                        i = 1;
                    } else {
                        holder.pan.setBackgroundColor(Color.WHITE);
                        holder.dogname.setTextColor(Color.parseColor("#959595"));
                        selectedId.remove(personalObject.id);
                        i = 0;
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return personalArray.size();
        }
    }


    public class ReservationAdd extends AsyncTask<ReservationObject, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ReservationObject... personalInfo) {
            boolean flag;

            ReservationObject reqParams = personalInfo[0];
            Response response = null;
            OkHttpClient client;



            try {
                client = OkHttpInitSingtonManager.getOkHttpClient();
                //요청 Form세팅

                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("resDatetime", reqParams.resDatetime)
                        .addFormDataPart("subject", reqParams.subject)

                        .addFormDataPart("status","1")
                        .addFormDataPart("hospitalId",MySharedPreferencesManager.getInstance().getHospitalselect());

                /*MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("resDatetime", reqParams.resDatetime)
                        .addFormDataPart("subject", reqParams.subject)
                        .addFormDataPart("memo", reqParams.memo)
                        .addFormDataPart("status","1")
                        .addFormDataPart("hospitalId",MySharedPreferencesManager.getInstance().getHospitalselect())   //MySharedPreferencesManager.getInstance().getHospitalselect()
                        .addFormDataPart("photos", "reservimage.jpg", RequestBody.create(MEDIA_TYPE_JPG, new File(reqParams.photo)));
*/

                /*for (int i = 0; i < reqParams.idlist.size(); i++) {
                    builder
                            .addFormDataPart("animals", Integer.toString(reqParams.idlist.get(i)));
                }*/
                RequestBody requestBody = builder.build();

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_HOSPITAL_RESERVATION_ADDSELECT)
                        .post(requestBody)
                        .build();
                //동기 방식

                response = client.newCall(request).execute();

                flag = response.isSuccessful();
                String returedJSON;
                if (flag) { //성공했다면
                    returedJSON = response.body().string();
                    Log.e("resultJSON", returedJSON);
                    try {
                        JSONObject jsonObject = new JSONObject(returedJSON);

                    } catch (JSONException jsone) {
                        Log.e("json에러", jsone.toString());
                    }
                } else {
                    //요청에러 발생시(http 에러)
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

        }
    }

    private class AsyncReserv extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            OkHttpClient toClient;


            try {
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_PET_ADD)
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
                        PersonalObject p = new PersonalObject();
                        JSONObject json = jsonData.getJSONObject(i);
                        p.name = json.getString("name");
                        p.photo = json.getString("resizePhotoUrl");
                        p.id = json.getInt("id");
                        personal.add(p);
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
            adapter = new MyAdapter(personal);
            recyclerView.setAdapter(adapter);
        }
    }

    private class AsyncTime extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {

            Response response = null;
            OkHttpClient toClient;


            try {
                toClient = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_HOSPITAL_SELECT+MySharedPreferencesManager.getInstance().getHospitalselect()) //
                        .get()
                        .build();

                response = toClient.newCall(request).execute();
                String returedJSON;
                Log.e("tt", "Get");

                returedJSON = response.body().string();
                Log.e("resultJSON", returedJSON);
                try {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    JSONObject jsonData = jsonObject.getJSONObject("result");
                    JSONArray j=jsonData.getJSONArray("hospitalVacations");
                    for(int i=0;i<j.length();i++){
                        JSONObject json=j.getJSONObject(i);
                        dd.add(json.getString("vacationDate").substring(0,10));
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
            for (int i = 0; i<dd.size(); i++) {
                String a="";
                if(String.valueOf(month).length()==1){
                    a="0"+month;
                }else{
                    a=String.valueOf(month);
                }
                if ((year + "-" + a + "-" + date).equals(dd.get(i))){
                    Log.e("tt",year + "-" + a + "-" + date);
                    Log.e("tt",dd.get(i));
                    for (int j = 0; j < 19; j++) {
                        reservButton[j].setTextColor(Color.rgb(149, 149, 149));
                        reservButton[j].setBackgroundColor(Color.rgb(236, 236, 236));
                        reservButton[j].setEnabled(false);
                    }
                }
            }
        }
    }
}
