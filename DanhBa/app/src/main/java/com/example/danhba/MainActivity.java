package com.example.danhba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.fontawesome.FontDrawable;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ListView listmenu;
    NavigationView navigationView;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        intDrawerLayout();
        ActionBar();

    }

    //Định dạng cho thanh toolbar
    private void ActionBar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        FontDrawable iconMenu=new FontDrawable(this,R.string.fa_align_justify_solid,true,false);
        iconMenu.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        iconMenu.setTextSize(23);
        toolbar.setNavigationIcon(iconMenu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    //Định dạng lại menu navigation
    private void intDrawerLayout() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        int[] icons = {
                R.string.fa_home_solid, R.string.fa_user_solid, R.string.fa_sign_out_alt_solid};
        renderMenuIcons(navigationView.getMenu(), icons, true, false);
    }

    //Set icons cho menu
    private void renderMenuIcons(Menu menu, int[] icons, boolean isSolid, boolean isBrand) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (!menuItem.hasSubMenu()) {
                FontDrawable drawable = new FontDrawable(this, icons[i], isSolid, isBrand);
                drawable.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
                drawable.setTextSize(22);
                menu.getItem(i).setIcon(drawable);
            }
        }
    }


    //Ánh xạ controler lên main
    private void AnhXa()
    {
        toolbar=findViewById(R.id.toolbarMain);
        drawerLayout=findViewById(R.id.drawerMain);
        navigationView = (NavigationView) findViewById(R.id.navigation_main);
    }

    //Gọi menubar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu,menu);
        FontDrawable iconMenu=new FontDrawable(this,R.string.fa_ellipsis_v_solid,true,false);
        iconMenu.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        iconMenu.setTextSize(22);
        toolbar.setOverflowIcon(iconMenu);
        return true;
    }

    // Sự kiện click vào OptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.find:
                //code xử lý khi bấm menu1
                break;
            case R.id.add_user:
            {
                Intent intent=new Intent(MainActivity.this,ThemDanhBa_Activity.class);
                startActivity(intent);
            }
                break;
            case R.id.menu3:
                //code xử lý khi bấm menu3
                break;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    //Tạo SQLite
    public void CreateDatabase()
    {
        // Tạo database
        database=new Database(this,"danhba.sqlite",null,1);
        //Tạo bảng
        database.QueryData("CREATE TABLE IF NOT EXISTS DanhBa(Id INTEGER PRIMARY KEY AUTOINCREMENT, Ten VARCHAR(10), SoDienThoai NUMBER(11),HinhAnh BLOB, NgaySinh DATE, Email VARCHAR, DiaChi VARCHAR, MangXaHoi VARCHAR ,YeuThich BOOLEAN, User BOOLEAN ");

    }



}
