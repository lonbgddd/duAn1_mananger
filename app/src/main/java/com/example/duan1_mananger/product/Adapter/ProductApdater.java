package com.example.duan1_mananger.product.Adapter;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductApdater extends RecyclerView.Adapter<ProductApdater.Product_ViewHolder>{

    private ArrayList<Product> list_product;
    private String text;

    public ProductApdater(ArrayList<Product> list_product,String text) {
        this.list_product = list_product;
        this.text = text;
    }

    public void setFilterList(ArrayList<Product> filterList){
        this.list_product = filterList ;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Product_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.layout_item_product,parent,false);
        Product_ViewHolder viewHolder = new Product_ViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Product_ViewHolder holder, int position) {
        Product product = list_product.get(position);
        if(text.equalsIgnoreCase("")){
        }else{
            if(product.getType_product().getName_type().equalsIgnoreCase(text)) {

            }else {
                list_product.remove(position);
            }

        }

        Locale locale = new Locale("en","EN");
        NumberFormat en = NumberFormat.getInstance(locale);
        Double price = product.getPrice();
        String strPrice = en.format(price);
        holder.tvNameProduct.setText(product.getName_product());
        holder.tvNoteProduct.setText(product.getMota());
        holder.tvPriceProduct.setText(strPrice+"đ");
        holder.imgProduct.setImageResource(product.getImage_product());

    }

    @Override
    public int getItemCount() {
            return list_product.size();

    }
    public  class Product_ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvNameProduct,tvNoteProduct,tvPriceProduct;



        public Product_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct=itemView.findViewById(R.id.imgProduct);
            tvNameProduct=itemView.findViewById(R.id.tvNameProduct);
            tvNoteProduct=itemView.findViewById(R.id.tvNoteProduct);
            tvPriceProduct=itemView.findViewById(R.id.tvPriceProduct);

        }
    }



}
