package com.example.duan1_mananger.product.Adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.databinding.LayoutItemProductBinding;
import com.example.duan1_mananger.model.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolderProduct> {
    private ArrayList<Product> listProduct;
    private String text;
    OnClickItemListener mOnClickItemListener;

    public interface OnClickItemListener{
        public void onClickItemProduct(Product product);
    }


    public ProductAdapter(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
    }

    public ProductAdapter(ArrayList<Product> listProduct, OnClickItemListener mOnClickItemListener) {
        this.listProduct = listProduct;
        this.mOnClickItemListener = mOnClickItemListener;
    }

    public void setFilterList(ArrayList<Product> filterList) {
        this.listProduct = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderProduct(LayoutItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProduct holder, int position) {
        Product product = listProduct.get(position);
        if (product == null) {
            return;
        } else {
            holder.initData(product);
        }
    }

    @Override
    public int getItemCount() {
        return listProduct.size();

    }

    public class ViewHolderProduct extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvDescribe, tvPrice;
        CardView layoutItem;

        public ViewHolderProduct(LayoutItemProductBinding binding) {
            super(binding.getRoot());
            imgProduct = binding.imgProduct;
            tvName = binding.tvNameProduct;
            tvDescribe = binding.tvDescribeProduct;
            tvPrice = binding.tvPriceProduct;
            layoutItem = binding.layoutItem;
        }

        void initData(Product product) {
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("imgProducts");
            reference.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference files: listResult.getItems()){
                    if(files.getName().equals(product.getId())){
                        files.getDownloadUrl().addOnSuccessListener(uri -> {
                            Glide.with(itemView).load(uri).into(imgProduct);
                        });
                    }
                }
            });
            tvName.setText(product.getNameProduct());

            Locale locale = new Locale("en","EN");
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
            Double price = product.getPrice();
            String strPrice = numberFormat.format(price);
            tvPrice.setText(strPrice +"đ");

            tvDescribe.setText(product.getDescribe());
            layoutItem.setOnClickListener(v -> {
                mOnClickItemListener.onClickItemProduct(product);
            });
        }
    }
}
