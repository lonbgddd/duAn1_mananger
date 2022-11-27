package com.example.duan1_mananger.table;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.base.OnclickOptionMenu;
import com.example.duan1_mananger.databinding.LayoutItemTableBinding;
import com.example.duan1_mananger.model.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolderTable> {
    private List<Table> list;
    private OnclickOptionMenu onclickOpntionMenu;
    public TableAdapter(List<Table> list, OnclickOptionMenu onclickOpntionMenu) {
        this.list = list;
        this.onclickOpntionMenu = onclickOpntionMenu;
    }

    @NonNull
    @Override
    public ViewHolderTable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTable(LayoutItemTableBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTable holder, int position) {
        Table table = list.get(position);
        if (table == null){
            return;
        } else {
            holder.initData(table);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderTable extends RecyclerView.ViewHolder {
        private TextView tv_name, tvStatusOff, tvStatusOn;
        private ImageView optionMenu;
        private ImageView logo;

        public ViewHolderTable(LayoutItemTableBinding binding) {
            super(binding.getRoot());
            tv_name = binding.tvNameTable;
            optionMenu = binding.icMenuChooser;
            logo = binding.icLogoTable;
            tvStatusOff = binding.tvStatusEmpty;
            tvStatusOn = binding.tvLogoTable;
        }

        void initData(Table table){
            tv_name.setText(table.getName_table());
            itemView.setOnClickListener(v -> {
                onclickOpntionMenu.onClick(table);
            });
            if (table.getStatus().equals("true")){
                logo.setVisibility(View.VISIBLE);
                tvStatusOff.setVisibility(View.GONE);
                tvStatusOn.setVisibility(View.VISIBLE);
            } else {
                logo.setVisibility(View.GONE);
                tvStatusOff.setVisibility(View.VISIBLE);
                tvStatusOn.setVisibility(View.GONE);
            }


        }
    }
}
