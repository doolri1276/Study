package com.snownaul.study.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.snownaul.study.R;
import com.snownaul.study.adapters.FragAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    BottomNavigationView bottomNavigationView;
    FragAdapter fragAdapter;
    MenuItem prevMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager=findViewById(R.id.pager);
        fragAdapter=new FragAdapter(getSupportFragmentManager());
        pager.setAdapter(fragAdapter);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.f1_home:
                        pager.setCurrentItem(0);
                        break;
                    case R.id.f2_feed:
                        pager.setCurrentItem(1);
                        break;
                    case R.id.f3_study:
                        pager.setCurrentItem(2);
                        break;
                    case R.id.f4_report:
                        pager.setCurrentItem(3);
                        break;
                    case R.id.f5_setting:
                        pager.setCurrentItem(4);
                        break;
                }

                return false;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(prevMenuItem!=null){
                    prevMenuItem.setChecked(false);
                }else{
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem=bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
}
