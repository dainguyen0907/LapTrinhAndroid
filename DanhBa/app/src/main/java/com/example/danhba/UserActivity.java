package com.example.danhba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import info.androidhive.fontawesome.FontDrawable;

public class UserActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    EditText name,phone,date,email,address,web;

    DatePickerDialog.OnDateSetListener datePickerDialog;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_PICTURE = 456;

    int _id=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        AnhXa();
        ActionBar();
        DesignNgaySinhBox();
        registerForContextMenu(imageView);

        LoadID();
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FontDrawable drawable=new FontDrawable(this,R.string.fa_arrow_left_solid,true,false);
        drawable.setTextColor(ContextCompat.getColor(this,android.R.color.white));
        drawable.setTextSize(20);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
    }

    private void AnhXa() {
        toolbar=findViewById(R.id.toolbarUser);
        imageView=findViewById(R.id.img_avatar_user);
        name=findViewById(R.id.txt_name_user);
        phone=findViewById(R.id.txt_phone_user);
        date=findViewById(R.id.txt_birthday_user);
        email=findViewById(R.id.txt_email_user);
        address=findViewById(R.id.txt_address_user);
        web=findViewById(R.id.txt_web_user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu_add,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)  {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add:
            {
                if(TextUtils.isEmpty(name.getText().toString().trim()))
                {
                    Toast.makeText(UserActivity.this, "Chưa nhập tên", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(phone.getText().toString().trim()))
                {
                    Toast.makeText(UserActivity.this, "Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
                else if(_id==-1){
                }
                else{
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 5, byteArrayOutputStream);
                    byte[] hinh = byteArrayOutputStream.toByteArray();

                    String sdt = phone.getText().toString().trim();
                    MainActivity.database.updateNguoiDung(name.getText().toString().trim(),sdt,hinh,email.getText().toString().trim(),
                            address.getText().toString().trim(),
                            date.getText().toString().trim(),
                            web.getText().toString().trim(),
                            _id);
                    Toast.makeText(UserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void DesignNgaySinhBox()
    {
        date.setShowSoftInputOnFocus(false);
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(UserActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,datePickerDialog,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(UserActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,datePickerDialog,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        datePickerDialog=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;

                String day = month + "/" + dayOfMonth + "/" + year;
                date.setText(day);
            }
        };
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
            imageView.setImageBitmap(bitmap);
        }
        if(requestCode==REQUEST_CODE_PICTURE && resultCode==RESULT_OK && data!=null)
        {
            Uri uri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void LoadID()
    {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            _id=bundle.getInt("id");
        }
        if(_id!=-1)
        {
            Cursor cursor=MainActivity.database.GetData("SELECT * FROM NguoiDung WHERE id="+_id);
            if(cursor.moveToFirst())
            {
                name.setText(cursor.getString(cursor.getColumnIndex("Ten")));
                phone.setText(cursor.getString(cursor.getColumnIndex("SoDienThoai")));
                date.setText(cursor.getString(cursor.getColumnIndex("NgaySinh")));
                email.setText(cursor.getString(cursor.getColumnIndex("Email")));
                web.setText(cursor.getString(cursor.getColumnIndex("MangXaHoi")));
                address.setText(cursor.getString(cursor.getColumnIndex("DiaChi")));

                byte[]hinhanh=cursor.getBlob(cursor.getColumnIndex("HinhAnh"));
                if(hinhanh.length>0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh, 0, hinhanh.length);
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

}
