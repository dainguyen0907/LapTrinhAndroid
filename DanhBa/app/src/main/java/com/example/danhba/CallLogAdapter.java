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

import androidx.core.content.ContextCompat;

import java.util.List;

import info.androidhive.fontawesome.FontTextView;

public class CallLogAdapter extends BaseAdapter {


    private List<CuocGoi> listdata;
    private int layout;
    private Context context;

    public CallLogAdapter(List<CuocGoi> listdata, int layout, Context context) {
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
        CallLogAdapter.ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =layoutInflater.inflate(layout,null);
            holder=new CallLogAdapter.ViewHolder();
            holder.Name=convertView.findViewById(R.id.txt_name_call_log);
            holder.Avatar=convertView.findViewById(R.id.imgCall);
            holder.icon=convertView.findViewById(R.id.txt_icon_call_log);
            holder.date=convertView.findViewById(R.id.txt_date_call_log);
            convertView.setTag(holder);
        }else
        {
            holder=(CallLogAdapter.ViewHolder)convertView.getTag();
        }


        CuocGoi cuocGoi=listdata.get(position);
        holder.Name.setText(cuocGoi.getTen());
        holder.icon.setText(cuocGoi.getIcon());
        holder.date.setText(cuocGoi.getNgaygoi());

        if (holder.icon.getText()=="\uf061")
            holder.icon.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        else if(holder.icon.getText()=="\uf00d")
            holder.icon.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        else if(holder.icon.getText()=="\uf060")
            holder.icon.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));

        byte[] hinh=cuocGoi.getHinh();
        Bitmap bitmap= BitmapFactory.decodeByteArray(hinh,0,hinh.length);
        holder.Avatar.setImageBitmap(bitmap);

        return convertView;
    }

    static class ViewHolder{
        ImageView Avatar;
        TextView Name;
        FontTextView icon;
        TextView date;
    }
}
