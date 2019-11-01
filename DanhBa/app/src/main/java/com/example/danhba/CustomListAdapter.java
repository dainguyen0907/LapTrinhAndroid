package com.example.danhba;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private List<DanhBa> listdata;
    private int layout;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;

    public CustomListAdapter(List<DanhBa> listdata, int layout, Context context) {
        this.listdata = listdata;
        this.layout = layout;
        this.context = context;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =layoutInflater.inflate(layout,null);
            holder=new ViewHolder();
            holder.Name=convertView.findViewById(R.id.txt_name);
            holder.Avatar=convertView.findViewById(R.id.imageItem);
            convertView.setTag(holder);
        }else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        DanhBa danhBa=listdata.get(position);

        holder.Name.setText(danhBa.getName());

        byte[] hinh=danhBa.getAvatar();
        Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
        holder.Avatar.setImageBitmap(bitmap);

        return convertView;
    }

    static class ViewHolder{
        ImageView Avatar;
        TextView Name;
    }

    public void toogleSelection(int position)
    {
        selectView(position,!mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public void remove(DanhBa object)
    {
        listdata.remove(object);
        notifyDataSetChanged();
    }

}
