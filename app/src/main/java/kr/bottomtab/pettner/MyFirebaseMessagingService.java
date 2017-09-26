package kr.bottomtab.pettner;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by user on 2017-06-19.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    int badge_count;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Log.e("tt","start");
        set_alarm_badge();
    }



    public void set_alarm_badge() {
        Log.e("tt", "notify receive");

        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

        //배지의 카운트를 공유저장소로부터 가져온다.//
        badge_count = MySharedPreferencesManager.getInstance().getBadgecount();
        badge_count++; //0으로 되어있기에 1로 만들어준다.//
        //패키지 이름과 클래그 이름설정.//

        intent.putExtra("badge_count", badge_count);
        Log.e("tt", String.valueOf(badge_count));
        //문자열로 대입 가능//
        intent.putExtra("badge_count_package_name", getApplicationContext().getPackageName()); //패키지 이름//
        //배지의 적용은 맨 처음 띄우는 화면을 기준으로 한다.//
        intent.putExtra("badge_count_class_name", SplashActivity.class.getName()); //맨 처음 띄우는 화면 이름//

        //변경된 값으로 다시 공유 저장소 값 초기화.//
        MySharedPreferencesManager.getInstance().setBadgecount(badge_count);

        sendBroadcast(intent);
    }
}