package com.example.duan1_mananger.Oder.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.databinding.LayoutItemReceiptHorizontalBinding;
import com.example.duan1_mananger.databinding.LayoutItemReceiptVerticalBinding;
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

public class ListOderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Receipt> listReceipt;
    OnClickListener mOnClickListener;
    private int typeLayout ;
    private Table table;
    public static final int typeHorizoltal =1;
    public static final int typeVertical =2;


    public interface OnClickListener{
        void onClickListener(Receipt receipt);
    }

    public ListOderAdapter(ArrayList<Receipt> listReceipt, OnClickListener mOnClickListener,int typeLayout) {
        this.listReceipt = listReceipt;
        this.mOnClickListener = mOnClickListener;
        this.typeLayout= typeLayout;
    }

    public void setFilterList(ArrayList<Receipt> filterList) {
        this.listReceipt = filterList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(typeHorizoltal==viewType){
            return new ViewHolderListOder(LayoutItemReceiptHorizontalBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }else if(typeVertical==viewType){
            return new ViewHolderListOderVertical(LayoutItemReceiptVerticalBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Receipt receipt = listReceipt.get(position);
        if(typeHorizoltal==holder.getItemViewType()){
            ViewHolderListOder viewHolderListOder = (ViewHolderListOder) holder;

            if(receipt==null){
                return;
            }else {
                ((ViewHolderListOder) holder).initData(receipt);
            }
        }else  if(typeVertical==holder.getItemViewType()){
            ViewHolderListOderVertical viewHolderListOderVertucal = (ViewHolderListOderVertical) holder;
            if(receipt==null){
                return;
            }else {
                ((ViewHolderListOderVertical) holder).initData(receipt);
            }
        }

    }

    @Override
    public int getItemCount() {
        return listReceipt.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(typeLayout==2){
            return typeVertical;
        }else {
            return  typeHorizoltal;
        }
    }
    //

    public  class ViewHolderListOder extends RecyclerView.ViewHolder {
        TextView tvNameBill,tvTotalMoney,tvTimeOder;
        ConstraintLayout layoutItem;

        public ViewHolderListOder(LayoutItemReceiptHorizontalBinding binding) {
            super(binding.getRoot());
            tvNameBill=binding.tvNameBill;
            tvTotalMoney=binding.tvTotalMoney;
            tvTimeOder=binding.tvTimeOder;
            layoutItem= binding.layoutItem;
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
            layoutItem.setOnClickListener(v->{
                mOnClickListener.onClickListener(receipt);
            });
        }
    }
    public  class ViewHolderListOderVertical extends RecyclerView.ViewHolder {
        TextView tvNameBill,tvTotalMoney,tvTimeOder,tvNoteBill;
        CardView layoutItem;

        public ViewHolderListOderVertical(LayoutItemReceiptVerticalBinding binding) {
            super(binding.getRoot());
            tvNameBill=binding.tvNameBill;
            tvTotalMoney=binding.tvTotalMoney;
            tvTimeOder=binding.tvTimeOder;
            tvNoteBill= binding.tvNoteBill;
            layoutItem= binding.layoutItem;
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
                        if(!receipt.getNoteOder().equals("")){
                            tvNoteBill.setText(receipt.getNoteOder());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            layoutItem.setOnClickListener(v->{
                mOnClickListener.onClickListener(receipt);
            });
        }
    }

}
