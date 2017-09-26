package kr.bottomtab.pettner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
/*
   BottomNavigationView를 이용해 각 하단탭을 클릭(터치)했을때  탭별
   액티비티를 구동하는 기반 클래스.
   하단탭을 이용해 액티비티를 구현할 시에는 반드시 본 클래스를 상속받아 구현하여야 하는 추상 클래스

 * Created(Modified) by pyoinsoo on 2017-05-30.
 * insoo.pyo@gmail.com
 */

public abstract class BottomNavigationParentActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener{

    //모든 액티비티를 반드시 본 하단탭을 include(여기선 시켜야 한다.bottom_navigation_layout)
    public BottomNavigationView bottomNavi;

    /*
      액티비티들이 구현할 추상 메소드
     */

    //현재 탭에 부착할 액티비티의 실제 레이아웃(BottomNavigationView include한 레이아웃리소스값)
    public abstract int getCurrentActivityLayoutName();

    //현재 사용자가 클릭(터치)한 메뉴객체
    public abstract int getCurrentSelectedBottomMenuItemID();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getCurrentActivityLayoutName());

        bottomNavi = (BottomNavigationView)findViewById(R.id.navigation);

        //하단탭을 터치했을때 애니메이션(디폴트) 효과를 제거한다
        BottomNavigationViewHelper.disableShiftMode(bottomNavi);

        //하단탭을 눌렀을때의 발생하는 이벤트
        bottomNavi.setOnNavigationItemSelectedListener(this);
    }
    /*
       NavigationItemSelectedListener 구현
       해당 액티비티로 이동
     */
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        int currentID = item.getItemId();
        Intent menuIntent = null;
        switch(currentID){
            case R.id.first_tab :
                menuIntent = new Intent(BottomNavigationParentActivity.this, FirstBottomTabActivity.class);
                break;
            case R.id.second_tab :
                menuIntent = new Intent(BottomNavigationParentActivity.this, SecondBottomTabActivity.class);
                break;
            case R.id.third_tab :
                menuIntent =  new Intent(BottomNavigationParentActivity.this, ThirdBottomTabActivity.class);
                break;
        }
        startActivity(menuIntent);
        finish();
        return true;
    }
    /*
      Activity Life Cycle를 이용해 해당 눌려진  하단탭  icon을  알아낸다.
     */
    @Override
    protected void onStart() {
        super.onStart();
        //현재 사용자가 선택한 하단탭을 찾아 효과(selector)를 준다
        setCurrenttSelectedNavigationBarItem(getCurrentSelectedBottomMenuItemID());
    }
    /*
       Activity들의 in/out에 애니메니션 효과를 코드에서 구현
       (styles.xml에서도 구현가능..이땐 모든 액티비티에 공통적용(심지어 탭에 없는 액티비티도 포함))
       중복사용하면 안됨(코드 또는 styles.xml에 선언 중 하나만 사용)
     */
    @Override
    protected void onPause() {
        super.onPause();
        // overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    //현재 선언된 액티비티를 찾는 메소드
    public void setCurrenttSelectedNavigationBarItem(int itemId) {
        Menu menu = bottomNavi.getMenu();
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                //이 메소드가 호출시 메뉴아이템에 selector(우린item_back_selector.xml)효과가 나타남
                item.setChecked(true);
                item.setEnabled(false);
                break;
            }
        }
    }



}
