package com.example.danhba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import info.androidhive.fontawesome.FontDrawable;

public class ThemDanhBa_Activity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_danh_ba_);

        AnhXa();
        ActionBar();
    }
    private void AnhXa()
    {
        toolbar=findViewById(R.id.toolbarMain);
    }
    private void ActionBar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FontDrawable drawable=new FontDrawable(this,R.string.fa_arrow_left_solid,true,false);
        drawable.setTextColor(ContextCompat.getColor(this,android.R.color.white));
        drawable.setTextSize(20);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add:
            {
                startActivity(new Intent(ThemDanhBa_Activity.this,MainActivity.class));
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu_add,menu);
        return true;
    }
}
