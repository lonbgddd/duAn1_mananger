package com.example.duan1_mananger.product.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.databinding.LayoutItemProductBinding;
import com.example.duan1_mananger.databinding.LayoutItemTableBinding;
import com.example.duan1_mananger.databinding.LayoutItemTypeProductBinding;
import com.example.duan1_mananger.databinding.LayoutProductTypeBinding;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.Table;
import com.example.duan1_mananger.model.TypeProduct;

import java.util.ArrayList;

public class TypeProductAdapter extends RecyclerView.Adapter<TypeProductAdapter.ViewHolderTypeProduct> {
    private ArrayList<TypeProduct> listType;
    private Context context;


    public TypeProductAdapter(ArrayList<TypeProduct> listType) {
        this.listType = listType;
    }



    public  void setFilterListType(ArrayList<TypeProduct> filterList ){
        this.listType = filterList;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public ViewHolderTypeProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TypeProductAdapter.ViewHolderTypeProduct(LayoutItemTypeProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTypeProduct holder, int position) {
        TypeProduct typeProduct = listType.get(position);
        if (typeProduct == null) {
            return;
        } else {
            holder.initData(typeProduct);
        }

    }

    @Override
    public int getItemCount() {
        if(listType!= null){
            return listType.size();
        }
        return 0;
    }

    class ViewHolderTypeProduct extends RecyclerView.ViewHolder {
        private TextView tvNameType;
        private ImageView icCheck;
        private ConstraintLayout layoutItem;

        public ViewHolderTypeProduct(LayoutItemTypeProductBinding binding) {
            super(binding.getRoot());
            tvNameType = binding.tvNameType;
            icCheck = binding.icCheckTypeProduct;
            layoutItem = binding.layoutItemType;
        }

        void initData(TypeProduct typeProduct){
            tvNameType.setText(typeProduct.getNameType());
            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(icCheck.getVisibility()==View.GONE){
                        icCheck.setVisibility(View.VISIBLE);
                    }else if(icCheck.getVisibility()==View.INVISIBLE){

                        icCheck.setVisibility(View.VISIBLE);
                    }else {
                        icCheck.setVisibility(View.INVISIBLE);

                    }

                }
            });
        }
    }




}
