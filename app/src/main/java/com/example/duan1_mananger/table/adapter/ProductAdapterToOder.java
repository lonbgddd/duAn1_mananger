package com.example.duan1_mananger.table.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.databinding.LayoutItemProductBinding;
import com.example.duan1_mananger.model.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapterToOder extends RecyclerView.Adapter<ProductAdapterToOder.ViewHolderProduct> {
    private ArrayList<Product> listProduct;
    private Context context;

    public ProductAdapterToOder(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
    }

    public ProductAdapterToOder(ArrayList<Product> listProduct, Context context) {
        this.listProduct = listProduct;
        this.context = context;



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
            holder.initData(product,context);
        }

    }

    @Override
    public int getItemCount() {
        return listProduct.size();

    }

    public class ViewHolderProduct extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvDescribe, tvPrice;
        ConstraintLayout layoutItem;


        public ViewHolderProduct(LayoutItemProductBinding binding) {
            super(binding.getRoot());
            imgProduct = binding.imgProduct;
            tvName = binding.tvNameProduct;
            tvDescribe = binding.tvDescribeProduct;
            tvPrice = binding.tvPriceProduct;
            layoutItem = binding.layoutItem;
        }

       public void initData(Product product, Context context ) {
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
           layoutItem.setBackgroundColor(product.isSelected() ? context.getColor(R.color.brown_70) : context.getColor(R.color.white));
           layoutItem.setOnClickListener(v ->{
               product.setSelected(!product.isSelected());
               layoutItem.setBackgroundColor(product.isSelected() ? context.getColor(R.color.brown_70) : context.getColor(R.color.white));
           });

        }




    }
}
