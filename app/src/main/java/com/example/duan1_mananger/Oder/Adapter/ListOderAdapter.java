package com.example.duan1_mananger.Oder.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private int typeLayout;
    public static final int TYPE_HORIZONTAL = 0;
    public static final int TYPE_VERTICAL = 1;

    public interface OnClickListener{
        void onClickListener(Receipt receipt);
    }

    public ListOderAdapter(ArrayList<Receipt> listReceipt, OnClickListener mOnClickListener, int typeLayout) {
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
        if(TYPE_HORIZONTAL == viewType){
            return new ViewHolderListOderHorizontal(LayoutItemReceiptHorizontalBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }else{
            return new ViewHolderListOderVertical(LayoutItemReceiptVerticalBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Receipt receipt = listReceipt.get(position);
        if(receipt==null) {
            return;
        }else {
            if(TYPE_HORIZONTAL == holder.getItemViewType()){
                ((ViewHolderListOderHorizontal) holder).initData(receipt);
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
        if(typeLayout == 0){
            return TYPE_HORIZONTAL;
        }else{
            return TYPE_VERTICAL;
        }

    }

    public  class ViewHolderListOderHorizontal extends RecyclerView.ViewHolder {
        TextView tvNameBill,tvTotalMoney,tvTimeOder;
        ConstraintLayout layoutItem;

        public ViewHolderListOderHorizontal(LayoutItemReceiptHorizontalBinding binding) {
            super(binding.getRoot());
            tvNameBill=binding.tvNameBill;
            tvTotalMoney=binding.tvTotalMoney;
            tvTimeOder=binding.tvTimeOder;
            layoutItem= binding.layoutItem;
        }
        void initData(Receipt receipt){
            Locale locale = new Locale("en","EN");
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
            tvNameBill.setText("POLY000"+receipt.getIdReceipt().substring(16,20));
            tvTimeOder.setText(receipt.getTimeOder());
            Double Money =receipt.getMoney();
            String strMoney = numberFormat.format(Money);
            tvTotalMoney.setText(strMoney);

            layoutItem.setOnClickListener(v->{
                mOnClickListener.onClickListener(receipt);
            });
        }
    }
    public  class ViewHolderListOderVertical extends RecyclerView.ViewHolder {
        TextView tvNameBill,tvTotalMoney,tvTimeOder,tvNoteBill, tvStatus;
        CardView layoutItem;
        LinearLayout layoutOderPrint;

        public ViewHolderListOderVertical(LayoutItemReceiptVerticalBinding binding) {
            super(binding.getRoot());
            tvNameBill=binding.tvNameBill;
            tvTotalMoney=binding.tvTotalMoney;
            tvTimeOder=binding.tvTimeOder;
            tvNoteBill= binding.tvNoteBill;
            tvStatus= binding.tvStatus;
            layoutItem= binding.layoutItem;
            layoutOderPrint = binding.layoutOderPrint;
        }
        void initData(Receipt receipt){
            Locale locale = new Locale("en","EN");
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
            tvNameBill.setText("POLY000"+receipt.getIdReceipt().substring(16,20));
            tvStatus.setText("Đã thanh toán");
            tvTimeOder.setText(receipt.getTimeOder());
            Double Money =receipt.getMoney();
            String strMoney = numberFormat.format(Money);
            tvTotalMoney.setText(strMoney);
            if(!receipt.getNoteOder().equals("")){
                tvNoteBill.setText(receipt.getNoteOder());
            }
            layoutOderPrint.setOnClickListener(layout ->{
                Toast.makeText(itemView.getContext(), "Chưa thiết lập máy in!", Toast.LENGTH_SHORT).show();
            });

            layoutItem.setOnClickListener(v->{
                mOnClickListener.onClickListener(receipt);
            });
        }
    }

}