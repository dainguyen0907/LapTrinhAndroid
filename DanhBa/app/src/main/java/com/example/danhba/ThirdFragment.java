package com.example.danhba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ThirdFragment extends Fragment {
    private View rootView;
    GridView gridView;
    private ArrayList<DanhBa> arrayList;
    CustomListAdapter customListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_third,container,false);
        gridView=rootView.findViewById(R.id.gv_danhba_YeuThich);
        return rootView;
    }

    public void onResume() {
        super.onResume();
        arrayList=new ArrayList<>();
        customListAdapter =new CustomListAdapter(arrayList,R.layout.itemgridviewdanhba,getActivity());
        gridView.setAdapter(customListAdapter);

        Cursor cursor= MainActivity.database.GetData("SELECT * FROM DanhBa WHERE YeuThich=1 ORDER BY Ten ASC");
        while(cursor.moveToNext()){
            arrayList.add(new DanhBa(
                    cursor.getString(1),
                    cursor.getInt(0),
                    cursor.getBlob(3)
            ));

        }

        customListAdapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DanhBa d=arrayList.get(position);
                Intent intent=new Intent(getActivity(),ChiTiet_Activity.class);
                intent.putExtra("id",d.getId());
                startActivity(intent);
            }
        });

    }
}
