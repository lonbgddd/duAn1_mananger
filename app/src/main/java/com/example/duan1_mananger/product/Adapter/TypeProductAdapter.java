package com.example.duan1_mananger.product.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.model.TypePoduct;

import java.util.ArrayList;

public class TypeProductAdapter extends RecyclerView.Adapter<TypeProductAdapter.Type_product_ViewHolder> {
    private ArrayList<TypePoduct> list_type;
    private Context context;
    private IClickItemListener iClickItemListener;
    public interface IClickItemListener{
        void onClickItemType(TypePoduct typePoduct);
    }

    public TypeProductAdapter(ArrayList<TypePoduct> list_type,IClickItemListener iClickItemListener) {
        this.iClickItemListener = iClickItemListener;
        this.list_type = list_type;
    }

    public  void setFilterListType(ArrayList<TypePoduct> filterList ){
        this.list_type = filterList;
        notifyDataSetChanged();

    }



    @NonNull
    @Override
    public Type_product_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.layout_item_type_product,parent,false);
        Type_product_ViewHolder viewHolder = new Type_product_ViewHolder(row);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull Type_product_ViewHolder holder, int position) {
        final  TypePoduct type_poduct = list_type.get(position);
        String text = type_poduct.getName_type();
        if(type_poduct ==null){
            return;
        }
        holder.tvNameType.setText(type_poduct.getName_type());
        holder.layout_item_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.icCheckType.getVisibility()==View.GONE){
                    holder.icCheckType.setVisibility(View.VISIBLE);
                }else if(holder.icCheckType.getVisibility()==View.INVISIBLE){
                    iClickItemListener.onClickItemType(type_poduct);
                    holder.icCheckType.setVisibility(View.VISIBLE);
                }else {
                    holder.icCheckType.setVisibility(View.INVISIBLE);
                    iClickItemListener.onClickItemType(null);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if(list_type!= null){
            return list_type.size();
        }
        return 0;
    }

    public class Type_product_ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameType;
        ConstraintLayout layout_item_type;
        ImageView icCheckType;

        public Type_product_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameType = itemView.findViewById(R.id.tvNameType);
            layout_item_type= itemView.findViewById(R.id.layout_item_type);
            icCheckType=itemView.findViewById(R.id.icCheckTypeProduct);
        }
    }



}
