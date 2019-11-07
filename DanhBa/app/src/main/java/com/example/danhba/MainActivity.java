package com.example.danhba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import info.androidhive.fontawesome.FontDrawable;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    public static Database database;
    ViewPager viewPager;
    TabLayout tabLayout;
    MaterialSearchView searchView;
    GridView search_gridview;
    LinearLayout tablayoutcontainer;

    final int REQUEST_READ_CONTACT=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        CreateDatabase();
        intDrawerLayout();
        ActionBar();
        InitTablayout();



    }

    private void InitTablayout() {
        viewPager.setAdapter(new AdapterFragment(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

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
                switch (menuItem.getItemId())
                {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_user:
                        break;
                    case R.id.nav_exit:
                        finish();
                        break;
                }
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
        tabLayout=findViewById(R.id.tablayout_main);
        viewPager=findViewById(R.id.vp_main);
        searchView=findViewById(R.id.search_view);
        search_gridview=findViewById(R.id.gridview_search);
        tablayoutcontainer=findViewById(R.id.tablayout_container);
    }

    //Gọi menubar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu,menu);
        FontDrawable iconMenu=new FontDrawable(this,R.string.fa_ellipsis_v_solid,true,false);
        iconMenu.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        iconMenu.setTextSize(22);
        toolbar.setOverflowIcon(iconMenu);

        MenuItem search= menu.findItem(R.id.find);
        searchView.setMenuItem(search);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

                search_gridview.setVisibility(View.VISIBLE);
                tablayoutcontainer.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSearchViewClosed() {

                search_gridview.setVisibility(View.INVISIBLE);
                tablayoutcontainer.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query(newText);
                return false;
            }
        });

        return true;
    }

    private void query(String query) {
        ArrayList<DanhBa>arrayList=new ArrayList<>();
        CustomListAdapter customListAdapter =new CustomListAdapter(arrayList,R.layout.itemgridviewdanhba,this);
        search_gridview.setAdapter(customListAdapter);

        Cursor cursor= MainActivity.database.GetData("SELECT * FROM DanhBa WHERE User=0 AND (Ten LIKE '%"+query+"%' OR SoDienThoai LIKE '%"+query+"%') ORDER BY Ten ASC");
        while(cursor.moveToNext()){
            arrayList.add(new DanhBa(
                    cursor.getString(1),
                    cursor.getInt(0),
                    cursor.getBlob(3)
            ));

        }

        customListAdapter.notifyDataSetChanged();

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
            {
            }
                break;
            case R.id.add_user:
            {
                Intent intent=new Intent(MainActivity.this,ThemDanhBa_Activity.class);
                startActivity(intent);
            }
                break;
            case R.id.add_user_from_sim:
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACT);

                break;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }



    //Tạo SQLite
    public void CreateDatabase()
    {
        // Tạo database
        database=new Database(this,"danhba.sqlite",null,1);
        //Tạo bảng
        database.QueryData("CREATE TABLE IF NOT EXISTS DanhBa(Id INTEGER PRIMARY KEY AUTOINCREMENT, Ten VARCHAR(10), SoDienThoai TEXT,HinhAnh BLOB, NgaySinh DATE, Email VARCHAR(100), DiaChi VARCHAR(200), MangXaHoi VARCHAR(100) ,YeuThich BOOLEAN, User BOOLEAN) ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_READ_CONTACT:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    ContentResolver contentResolver=getContentResolver();
                    Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                    while (cursor.moveToNext())
                    {
                        String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        int hasPhoneNumber=Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                        String sodienthoai="";
                        if(hasPhoneNumber==1)
                        {
                            Cursor Phone=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            Phone.moveToFirst();
                            sodienthoai=Phone.getString(Phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        Cursor cursorCheck=database.GetData("SELECT * FROM DanhBa WHERE Ten='"+cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+"' AND SoDienThoai='"+sodienthoai+"' AND User=0");
                        if(cursorCheck.moveToFirst()==false)
                        {
                            database.insertData(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                                    sodienthoai,
                                    new byte[]{},"","","","");
                        }
                    }
                    Toast.makeText(this,"Cập nhật thành công ",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this,"Chưa cấp quyền truy cập danh bạ ",Toast.LENGTH_SHORT).show();
            }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
