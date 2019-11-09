package com.example.danhba;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;

public class FirstFragment extends Fragment {
    private View rootView;
    ListView listView;
    ArrayList<CuocGoi> arrayList;
    CallLogAdapter callLogAdapter;
    final int REQUEST_READ_LOG_CALL=111;
    final int REQUEST_CALL=222;
    final int REQUEST_MESSEAAGE=333;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_first,container,false);
        listView=rootView.findViewById(R.id.list_call_log);
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG )!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_LOG_CALL);
        }
        return rootView;
    }

    @Override
    public void onResume() {

        super.onResume();
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG )==PackageManager.PERMISSION_GRANTED)
        {
            arrayList=new ArrayList<>();
            callLogAdapter=new CallLogAdapter(arrayList,R.layout.itemlistviewcuocgoi,getActivity());
            listView.setAdapter(callLogAdapter);

            Cursor cursor=getActivity().managedQuery(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE + " DESC");
            while (cursor.moveToNext())
            {
                String number=cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String calltype=cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                Date Strdate=new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
                String date=(Strdate.getDay()>=10? Strdate.getDay():"0"+Strdate.getDay())+"/"+
                        (Strdate.getMonth()+1>=10? (Strdate.getMonth()+1):"0"+(Strdate.getMonth()+1))+"/"+
                        (Strdate.getYear()+1900)+", "+
                        (Strdate.getHours()>=10? Strdate.getHours():"0"+Strdate.getHours())+":"+
                        (Strdate.getMinutes()>=10? Strdate.getMinutes():"0"+Strdate.getMinutes());

                String Name=number.toString();
                String icon="@string/fa_times_solid";
                byte[] Avatar={};

                Cursor search=MainActivity.database.GetData("SELECT Ten,HinhAnh FROM DanhBa Where SoDienThoai='"+number+"'");
                if(search.moveToFirst()==true)
                {
                    search.moveToFirst();
                    Name=search.getString(search.getColumnIndex("Ten"));
                    Avatar=search.getBlob(search.getColumnIndex("HinhAnh"));
                }

                switch (Integer.parseInt(calltype))
                {
                    case CallLog.Calls.OUTGOING_TYPE:
                        icon="\uf061";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        icon="\uf060";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        icon="\uf00d";
                        break;
                }
                arrayList.add(new CuocGoi(Avatar,Name,number,icon,date));
            }
            callLogAdapter.notifyDataSetChanged();
            registerForContextMenu(listView);

        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.callog_menucontext,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        CuocGoi c= arrayList.get(info.position);
        switch (item.getItemId())
        {
            case R.id.menu_call: {
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
                {
                    intentCall(c.getSodienthoai());
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }
                return true;
            }
            case R.id.menu_messege:{
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED)
                {
                    intentSendSMS(c.getSodienthoai());
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_MESSEAAGE);
                }
                return true;
            }
            case R.id.menu_add_user: {
                if(c.getTen()==c.getSodienthoai())
                {
                    Intent intent=new Intent(getActivity(),ThemDanhBa_Activity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("sdt",c.getSodienthoai());
                    bundle.putInt("id",-1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getActivity(),"Đã có người này trong danh bạ",Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.menu_copy: {
                ClipboardManager clipboardManager= (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("copy",c.getSodienthoai());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(),"Đã sao chép số",Toast.LENGTH_SHORT).show();
                return true;
            }
            default: return super.onContextItemSelected(item);
        }

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
}
