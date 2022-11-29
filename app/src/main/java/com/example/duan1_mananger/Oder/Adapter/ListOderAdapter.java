package com.example.duan1_mananger.Oder.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.databinding.LayoutItemReceiptHorizontalBinding;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.Receipt;
import com.example.duan1_mananger.model.Table;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListOderAdapter extends RecyclerView.Adapter<ListOderAdapter.ViewHolderListOder> {
    private ArrayList<Receipt> listReceipt;
    private Table table;

    public ListOderAdapter(ArrayList<Receipt> listReceipt) {
        this.listReceipt = listReceipt;
    }
    public void setFilterList(ArrayList<Receipt> filterList) {
        this.listReceipt = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderListOder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderListOder(LayoutItemReceiptHorizontalBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderListOder holder, int position) {
        Receipt receipt = listReceipt.get(position);
        if(receipt==null){
            return;
        }else {
            holder.initData(receipt);
        }
    }

    @Override
    public int getItemCount() {
        return listReceipt.size();
    }

    public  class ViewHolderListOder extends RecyclerView.ViewHolder {
        TextView tvNameBill,tvTotalMoney,tvTimeOder;

        public ViewHolderListOder(LayoutItemReceiptHorizontalBinding binding) {
            super(binding.getRoot());
            tvNameBill=binding.tvNameBill;
            tvTotalMoney=binding.tvTotalMoney;
            tvTimeOder=binding.tvTimeOder;
        }
        void initData(Receipt receipt){
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
            mRef.child("tables").child(receipt.getIdTable()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    table= snapshot.getValue(Table.class);
                    if(table==null){
                        return;
                    }else {
                        Locale locale = new Locale("en","EN");
                        NumberFormat numberFormat = NumberFormat.getInstance(locale);
                        tvNameBill.setText("POLY000"+receipt.getIdReceipt().substring(16,20));
                        tvTimeOder.setText(receipt.getTimeOder());
                        Double Money =receipt.getMoney();
                        String strMoney = numberFormat.format(Money);
                        tvTotalMoney.setText(strMoney);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
