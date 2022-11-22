package com.example.duan1_mananger.product.Adapter;


import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.databinding.LayoutItemProductBinding;
import com.example.duan1_mananger.databinding.LayoutItemTableBinding;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.table.TableAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductApdater extends RecyclerView.Adapter<ProductApdater.ViewHolderProduct> {
    private ArrayList<Product> listProduct;
    private String text;

    public ProductApdater(ArrayList<Product> listProduct, String text) {
        this.listProduct = listProduct;
        this.text = text;
    }

    public ProductApdater(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
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
//            if (text.equalsIgnoreCase("")) {
//            } else {
//                if (product.getTypePoduct().getName_type().equalsIgnoreCase(text)) {
//
//                } else {
//                    listProduct.remove(position);
//                }
//            }
            holder.initData(product);
        }
    }

    @Override
    public int getItemCount() {
        return listProduct.size();

    }

    public class ViewHolderProduct extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvNote, tvPrice;

        public ViewHolderProduct(LayoutItemProductBinding binding) {
            super(binding.getRoot());
            imgProduct = binding.imgProduct;
            tvName = binding.tvNameProduct;
            tvNote = binding.tvDescribeProduct;
            tvPrice = binding.tvPriceProduct;
        }

        void initData(Product product) {
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("imgProducts");
            reference.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference files: listResult.getItems()){
                    if(files.getName().equals(product.getName_product())){
                        Log.d("TAG", "initData: "+product.getName_product());
                        Log.d("TAG", "initData: "+files.getName());

                        files.getDownloadUrl().addOnSuccessListener(uri -> {
                            Log.d("TAG", "initData: "+uri);
                            Glide.with(itemView).load(uri).into(imgProduct);
                        });
                    }
                }
            });
                tvName.setText(product.getName_product());
                tvPrice.setText(String.valueOf(product.getPrice()));
                tvNote.setText(product.getNote());
        }
    }
}
