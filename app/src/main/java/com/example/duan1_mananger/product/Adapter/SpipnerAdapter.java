package com.example.duan1_mananger.product.Adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.model.TypePoduct;

import java.util.ArrayList;

public class SpipnerAdapter extends BaseAdapter {
    ArrayList<TypePoduct> listType;

    public SpipnerAdapter(ArrayList<TypePoduct> listType) {
        this.listType = listType;
    }

    @Override
    public int getCount() {
        return listType.size();
    }

    @Override
    public Object getItem(int position) {
        TypePoduct objType = listType.get(position);
        return objType;
    }

    @Override
    public long getItemId(int position) {
        TypePoduct objType = listType.get(position);
        return objType.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        if(convertView==null){
            row= View.inflate(parent.getContext(), R.layout.layout_spiner_type_product,null);
        }
        else {
            row = convertView;
        }
        TextView tv_name=row.findViewById(R.id.tvSpinerType);
        TypePoduct objType = listType.get(position);
        tv_name.setText(objType.getName_type());
        return row;
    }
}
