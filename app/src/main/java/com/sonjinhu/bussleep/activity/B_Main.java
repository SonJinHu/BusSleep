package com.sonjinhu.bussleep.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sonjinhu.bussleep.R;
import com.sonjinhu.bussleep.adapter.B_ViewPagerAdapter;
import com.sonjinhu.bussleep.fragment.Ba_FRG_Home;
import com.sonjinhu.bussleep.fragment.Bd_FRG_Setting;
import com.sonjinhu.bussleep.fragment.Bc_FRG_Favorite;
import com.sonjinhu.bussleep.fragment.Bb_FRG_History;

public class B_Main extends AppCompatActivity implements View.OnClickListener {

    long lastTimeBackPressed;
    ViewPager viewPager;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_edit:
                startActivity(new Intent(getApplicationContext(), C_Search.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.d_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        EditText edit = (EditText) findViewById(R.id.b_edit);
        edit.setOnClickListener(this);

        initViewPager();
        initTab(viewPager);
    }

    void initViewPager() {
        B_ViewPagerAdapter adapter = new B_ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Ba_FRG_Home());
        adapter.addFragment(new Bb_FRG_History());
        adapter.addFragment(new Bc_FRG_Favorite());
        adapter.addFragment(new Bd_FRG_Setting());

        viewPager = (ViewPager) findViewById(R.id.b_viewpager);
        viewPager.setAdapter(adapter);
    }

    void initTab(ViewPager viewPager) {
        int[] tabIcons = {
                R.drawable.ic_home_gray_24dp,
                R.drawable.ic_hourglass_full_gray_24dp,
                R.drawable.ic_star_full_gray_24dp,
                R.drawable.ic_settings_gray_24dp
        };

        TabLayout tabLayout = (TabLayout) findViewById(R.id.b_tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int color = ContextCompat.getColor(getBaseContext(), R.color.identity);
                //noinspection ConstantConditions
                tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int color = ContextCompat.getColor(getBaseContext(), R.color.gray);
                //noinspection ConstantConditions
                tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < tabIcons.length; i++)
            //noinspection ConstantConditions
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);

        int color = ContextCompat.getColor(getBaseContext(), R.color.identity);
        //noinspection ConstantConditions
        tabLayout.getTabAt(0).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_b, menu);
        return super.onCreateOptionsMenu(menu);
    }
}