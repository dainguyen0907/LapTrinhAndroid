package com.example.danhba;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SecondFragment extends Fragment {
    private View rootView;
    GridView gridView;
    private ArrayList<DanhBa> arrayList;
    CustomListAdapter customListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_second,container,false);
        gridView=rootView.findViewById(R.id.gv_danhba);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList=new ArrayList<>();
        customListAdapter =new CustomListAdapter(arrayList,R.layout.itemgridviewdanhba,getActivity());
        gridView.setAdapter(customListAdapter);

        Cursor cursor= MainActivity.database.GetData("SELECT * FROM DanhBa WHERE User=0 ORDER BY Ten ASC");
        while(cursor.moveToNext()){
            arrayList.add(new DanhBa(
                    cursor.getString(1),
                    cursor.getInt(0),
                    cursor.getBlob(3)
            ));

        }

        customListAdapter.notifyDataSetChanged();

    }


}
