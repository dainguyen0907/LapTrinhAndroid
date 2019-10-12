package com.example.danhba;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public CustomListAdapter(List<DanhBa> listdata, int layout, Context context) {
        this.listdata = listdata;
        this.layout = layout;
        this.context = context;
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
}
