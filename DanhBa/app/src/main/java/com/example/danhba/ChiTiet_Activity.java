package com.example.danhba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import info.androidhive.fontawesome.FontDrawable;

public class ChiTiet_Activity extends AppCompatActivity {

    TextView ten,sdt,diachi,ngaysinh,email,mxh;
    ImageView avatar;
    ImageButton like;
    Button goi,nhantin;
    Toolbar toolbar;
    String sodienthoai;
    final int REQUEST_CALL=222;
    final int REQUEST_MESSEAAGE=333;
    int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_);
        ten=findViewById(R.id.txt_ten_db);
        sdt=findViewById(R.id.txt_sdt_db);
        diachi=findViewById(R.id.txt_diachi_db);
        ngaysinh=findViewById(R.id.txt_ngaysinh_db);
        email=findViewById(R.id.txt_mail_db);
        mxh=findViewById(R.id.txt_mxh_db);
        like=findViewById(R.id.btn_like);
        avatar=findViewById(R.id.avatar_chitiet);
        toolbar=findViewById(R.id.toolbarChiTiet);
        goi=findViewById(R.id.btn_goi);
        nhantin=findViewById(R.id.btn_nhantin);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FontDrawable drawable=new FontDrawable(this,R.string.fa_arrow_left_solid,true,false);
        drawable.setTextColor(ContextCompat.getColor(this,android.R.color.white));
        drawable.setTextSize(20);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _id=getIntent().getExtras().getInt("id");
        Cursor cursor=MainActivity.database.GetData("select * from DanhBa where id="+_id);

        if(cursor.moveToFirst())
        {
            String name=cursor.getString(cursor.getColumnIndex("Ten"));
            sodienthoai=cursor.getString(cursor.getColumnIndex("SoDienThoai"));
            String nsinh=cursor.getString(cursor.getColumnIndex("NgaySinh"));
            String dchi=cursor.getString(cursor.getColumnIndex("DiaChi"));
            String mail=cursor.getString(cursor.getColumnIndex("Email"));
            String web=cursor.getString(cursor.getColumnIndex("MangXaHoi"));
            byte[]hinh=cursor.getBlob(cursor.getColumnIndex("HinhAnh"));
            Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
            avatar.setImageBitmap(bitmap);
            int favor=cursor.getInt(cursor.getColumnIndex("YeuThich"));

            ten.setText(name);
            sdt.setText(sodienthoai);
            if(favor==0)
            {
                like.setBackgroundColor(Color.LTGRAY);
                like.setTag("unlike");
            }
            else
            {
                like.setBackgroundColor(Color.BLUE);
                like.setTag("like");
            }
            if(!nsinh.isEmpty()) {
                ngaysinh.setText(nsinh);
            }
            if(!dchi.isEmpty()){
            diachi.setText(dchi);
            }
            if(!mail.isEmpty()) {
                email.setText(mail);
            }
            if(!web.isEmpty()) {
                mxh.setText(web);
            }
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(like.getTag()=="unlike")
                {
                    MainActivity.database.QueryData("UPDATE DanhBa SET YeuThich=1 WHERE id="+_id);
                    like.setBackgroundColor(Color.BLUE);
                    like.setTag("like");
                    Toast.makeText(ChiTiet_Activity.this,"Đã yêu thích",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    MainActivity.database.QueryData("UPDATE DanhBa SET YeuThich=0 WHERE id="+_id);
                    like.setBackgroundColor(Color.LTGRAY);
                    like.setTag("unlike");
                    Toast.makeText(ChiTiet_Activity.this,"Hủy yêu thích",Toast.LENGTH_SHORT).show();
                }
            }
        });

        goi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ChiTiet_Activity.this,Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
                {
                    intentCall(sodienthoai);
                }
                else {
                    ActivityCompat.requestPermissions(ChiTiet_Activity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }
            }
        });
        nhantin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ChiTiet_Activity.this,Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED)
                {
                    intentSendSMS(sodienthoai);
                }
                else {
                    ActivityCompat.requestPermissions(ChiTiet_Activity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_MESSEAAGE);
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_sua_ct:
            {
                Intent intentchange=new Intent(ChiTiet_Activity.this,ThemDanhBa_Activity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("id",_id);
                intentchange.putExtras(bundle);
                startActivity(intentchange);
                finish();
            }
                return true;
            case R.id.menu_xoa_ct:
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Bạn muốn xóa danh bạ này");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Có",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.database.QueryData("DELETE FROM DanhBa WHERE id="+_id);
                                Toast.makeText(ChiTiet_Activity.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        });

                builder1.setNegativeButton(
                        "Không",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void intentCall(String number)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    private  void intentSendSMS(String number)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:"+number));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chitiet_actionbarmenu,menu);
        FontDrawable iconMenu=new FontDrawable(this,R.string.fa_ellipsis_v_solid,true,false);
        iconMenu.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        iconMenu.setTextSize(22);
        toolbar.setOverflowIcon(iconMenu);
        return true;
    }
}
