package com.example.duan1_mananger.table;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.databinding.ItemOderMenuBinding;
import com.example.duan1_mananger.model.Product;

import java.util.List;

public class OderAdapter extends RecyclerView.Adapter<OderAdapter.ViewHolder>{
    List<Product> productList;

    public OderAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemOderMenuBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null){
            return;
        } else
            holder.initData(product);
    }

    @Override
    public int getItemCount() {
        if (productList != null){
            return productList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvPrice;
        public ViewHolder(@NonNull ItemOderMenuBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvNameProductOder;
            tvPrice = binding.tvMoneyProductOder;
        }
        void initData(Product product){
            tvName.setText(product.getNameProduct());
            tvPrice.setText(product.getPrice()+"");
        }
    }
}
