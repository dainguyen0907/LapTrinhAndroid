package com.example.danhba;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_first,container,false);
        listView=rootView.findViewById(R.id.list_call_log);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_LOG_CALL);
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

                Cursor search=MainActivity.database.GetData("SELECT Ten,HinhAnh FROM DanhBa Where SoDienThoai="+number);
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
                arrayList.add(new CuocGoi(Avatar,Name,icon,date));
            }
            callLogAdapter.notifyDataSetChanged();
        }
    }
}
