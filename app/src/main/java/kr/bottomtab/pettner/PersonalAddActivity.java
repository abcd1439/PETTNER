package kr.bottomtab.pettner;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by user on 2017-06-11.
 */

public class PersonalAddActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView woman, man;
    CircleImageView memberImage, changeImage;
    private static final int PICK_FROM_GALLERY = 100;
    Uri returedImgURI;
    PersonalObject entityObject;
    EditText dogname, dogtype, dogage;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personaladd);

        if (entityObject == null) {
            entityObject = new PersonalObject();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        dogname = (EditText) findViewById(R.id.dogname);
        dogtype = (EditText) findViewById(R.id.dogtype);
        dogage = (EditText) findViewById(R.id.dogage);
        memberImage = (CircleImageView) findViewById(R.id.memberImage);
        changeImage = (CircleImageView) findViewById(R.id.change_image);
        changeImage.setOnClickListener(new View.OnClickListener() {
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


        woman = (ImageView) findViewById(R.id.woman);
        man = (ImageView) findViewById(R.id.man);

        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woman.setImageResource(R.drawable.icon_awoman);
                man.setImageResource(R.drawable.icon_man);
                flag = 0;
            }
        });
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woman.setImageResource(R.drawable.icon_woman);
                man.setImageResource(R.drawable.icon_aman);
                flag = 1;

            }
        });


        ImageView savebtn = (ImageView) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entityObject.name = dogname.getText().toString();
                entityObject.type = dogtype.getText().toString();
                entityObject.age = dogage.getText().toString()+"살";

                if (flag == 0) {
                    entityObject.sex = "암컷";
                } else {
                    entityObject.sex = "수컷";
                }
                entityObject.photo = fileLocation;

                if(entityObject.name.length()==0||entityObject.type.length()==0||entityObject.age.length()==0){
                    Toast.makeText(PersonalAddActivity.this,"입력을 정확히 해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    new DogAddInsert().execute(entityObject);

                }

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent i = new Intent(this, ChangeActivity.class);
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
        Intent i = new Intent(this, ChangeActivity.class);
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


    public class DogAddInsert extends AsyncTask<PersonalObject, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(PersonalObject... personalInfo) {
            boolean flag;

            PersonalObject reqParams = personalInfo[0];
            Response response = null;
            OkHttpClient client;


            try {
                client = OkHttpInitSingtonManager.getOkHttpClient();
                //요청 Form세팅
                RequestBody requestBody;
                if(reqParams.photo!=null){
                    requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("name", reqParams.name)
                            .addFormDataPart("sex", reqParams.sex)
                            .addFormDataPart("age", reqParams.age)
                            .addFormDataPart("type", reqParams.type)
                            .addFormDataPart("photo", "dogimage.jpg", RequestBody.create(MEDIA_TYPE_JPG, new File(reqParams.photo)))
                            .build();
                }else{
                    Log.e("tt","image");
                    requestBody = new FormBody.Builder()
                            .add("name", reqParams.name)
                            .add("sex", reqParams.sex)
                            .add("age", reqParams.age)
                            .add("type", reqParams.type)
                            .build();
                }

                //요청 세팅(form(Query String) 방식의 포스트)
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_PET_ADD)
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
            Intent i = new Intent(PersonalAddActivity.this, ChangeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }


}
