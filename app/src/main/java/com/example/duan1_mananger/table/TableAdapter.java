package com.example.duan1_mananger.table;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.databinding.LayoutItemTableBinding;
import com.example.duan1_mananger.model.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolderTable> {
    private List<Table> list;

    public TableAdapter(List<Table> list) {
        this.list = list;
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
        private TextView tv_name;
        public ViewHolderTable(LayoutItemTableBinding binding) {
            super(binding.getRoot());
            tv_name = binding.tvNameTable;

        }

        void initData(Table table){
            tv_name.setText(table.getName_table());
        }
    }
}
