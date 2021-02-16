package com.jay.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    String tab_names[]={"CHATS","USERS"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        tabLayout=findViewById(R.id.chat_tablayout);
        viewPager=findViewById(R.id.chat_pageviewer);
        ViewpageAdapter adapter=new ViewpageAdapter(getSupportFragmentManager(),2);
        adapter.addFragment(new chatFragment());
        adapter.addFragment(new UsersFragment());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chat App");
        setSupportActionBar(toolbar);

        for(int i=0;i<tab_names.length;i++) {
//            tabLayout.getTabAt(i).setIcon(icons[i]);
            tabLayout.getTabAt(i).setText(tab_names[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(Dashboard.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}