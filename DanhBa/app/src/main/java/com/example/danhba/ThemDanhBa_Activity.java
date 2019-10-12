package com.example.danhba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import javax.xml.transform.Result;

import info.androidhive.fontawesome.FontDrawable;

public class ThemDanhBa_Activity extends AppCompatActivity {

    Toolbar toolbar;
    EditText ngaysinh, ten, sodienthoai, diachi, email, mxh;
    ImageButton avatar;
    DatePickerDialog.OnDateSetListener datePickerDialog;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_PICTURE = 456;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_danh_ba_);

        AnhXa();
        ActionBar();
        DesignNgaySinhBox();

        registerForContextMenu(avatar);
    }
    private void AnhXa()
    {
        toolbar=findViewById(R.id.toolbarMain);
        ngaysinh=findViewById(R.id.txt_ngaysinh);
        ten=findViewById(R.id.txt_ten);
        sodienthoai=findViewById(R.id.txt_dienthoai);
        diachi=findViewById(R.id.txt_diachi);
        email=findViewById(R.id.txt_email);
        mxh=findViewById(R.id.txt_mxh);
        avatar=findViewById(R.id.imageAvatar);

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
                if(TextUtils.isEmpty(ten.getText().toString().trim()))
                {
                    Toast.makeText(ThemDanhBa_Activity.this, "Chưa nhập tên", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(sodienthoai.getText().toString().trim()))
                {
                    Toast.makeText(ThemDanhBa_Activity.this, "Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
                else {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) avatar.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] hinh = byteArrayOutputStream.toByteArray();

                    long sdt = Long.parseLong(sodienthoai.getText().toString().trim());
                    MainActivity.database.insertData(
                            ten.getText().toString().trim(),
                            sdt,
                            hinh,
                            email.getText().toString().trim(),
                            diachi.getText().toString().trim(),
                            ngaysinh.getText().toString().trim(),
                            mxh.getText().toString().trim());
                    Toast.makeText(ThemDanhBa_Activity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu_add,menu);
        return true;
    }
    public void DesignNgaySinhBox()
    {
        ngaysinh.setShowSoftInputOnFocus(false);
        ngaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(ThemDanhBa_Activity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,datePickerDialog,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        datePickerDialog=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;

                String date = month + "/" + dayOfMonth + "/" + year;
                ngaysinh.setText(date);
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.image_menucontext,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_camera:
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CODE_CAMERA);
                break;
            case R.id.menu_picture:
                Intent intent1=new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,REQUEST_CODE_PICTURE);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_CAMERA && resultCode== RESULT_OK && data!=null)
        {
            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            avatar.setImageBitmap(bitmap);
        }
        if(requestCode==REQUEST_CODE_PICTURE && resultCode==RESULT_OK && data!=null)
        {
            Uri uri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                avatar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
