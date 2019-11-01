package com.example.danhba;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AnalogClock;
import android.widget.GridView;
import android.widget.Toast;

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
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkCount= gridView.getCheckedItemCount();
                mode.setTitle(checkCount+" số liên lạc");
                customListAdapter.toogleSelection(position);

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.multichoicemenu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_cancel:
                    {
                        for(int i=0;i<gridView.getCount();i++)
                        {
                            if(gridView.isItemChecked(i))
                                gridView.setItemChecked(i,false);
                        }
                        customListAdapter.removeSelection();
                        return true;
                    }
                    case  R.id.menu_delete:
                    {
                        SparseBooleanArray selected = customListAdapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                DanhBa selecteditem = (DanhBa) customListAdapter.getItem(selected.keyAt(i));
                                MainActivity.database.QueryData("DELETE FROM DanhBa WHERE Id="+selecteditem.getId());
                                customListAdapter.remove(selecteditem);
                            }
                        }
                        Toast.makeText(getActivity(),"Xóa danh bạ thành công",Toast.LENGTH_SHORT).show();
                        mode.finish();
                        return true;
                    }
                    default: return false;
                }

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                customListAdapter.removeSelection();
            }
        });

    }



}
