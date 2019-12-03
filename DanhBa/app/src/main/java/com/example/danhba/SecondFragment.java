package com.example.danhba;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

        Cursor cursor= MainActivity.database.GetData("SELECT * FROM DanhBa  ORDER BY Ten ASC");
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
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
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
                        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        builder1.setMessage("Bạn muốn xóa danh bạ này");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Có",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                            SparseBooleanArray selected = customListAdapter.getSelectedIds();
                                            // Captures all selected ids with a loop
                                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                                if (selected.valueAt(i)) {
                                                    DanhBa selecteditem = (DanhBa) customListAdapter.getItem(selected.keyAt(i));
                                                    MainActivity.database.QueryData("DELETE FROM DanhBa WHERE Id=" + selecteditem.getId());
                                                    customListAdapter.remove(selecteditem);
                                                }
                                            }
                                            Toast.makeText(getActivity(), "Xóa danh bạ thành công", Toast.LENGTH_SHORT).show();
                                            mode.finish();
                                    }
                                });

                        builder1.setNegativeButton(
                                "Không",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        mode.finish();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
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
