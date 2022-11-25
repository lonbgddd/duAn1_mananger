package com.example.duan1_mananger.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentDetailsProductBinding;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.TypeProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DetailProductFragment extends BaseFragment {

    private FragmentDetailsProductBinding binding = null;
    private Product dataProduct = null;

    private String nameTypeProduct = "";

    public DetailProductFragment(Product product, String nameTypeProduct) {
        this.nameTypeProduct = nameTypeProduct;
        this.dataProduct = product;
    }

    public DetailProductFragment newInstance() {
        return new DetailProductFragment(dataProduct, nameTypeProduct);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailsProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.d("TAG", "newInstance222: "+dataProduct.getId());
        listening();
        initObSever();
    }

    @Override
    public void loadData() {

    }

    @Override
    public void listening() {
        binding.btnDeleteProduct.setOnClickListener(v -> {
            dialogConfirmUpdate(getContext());
        });
    }

    @Override
    public void initObSever() {
        showDetailProduct();
    }

    @Override
    public void initView() {

    }

    private void dialogConfirmUpdate(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cập nhật thông tin cá nhân");
        builder.setIcon(context.getDrawable(R.drawable.icon_save));
        builder.setMessage("Bạn chắc chắn muốn thay đổi thông tin cá nhân");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("list_product");
                replaceFragment(new ProductFragment().newInstance());
                reference.child(dataProduct.getId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(context, "Đã xóa ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context,"Đã hủy !",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        AlertDialog sh = builder.create();
        sh.show();
    }

    private void getTypeProduct(){

    }

    private void showDetailProduct(){
        TypeProduct typeProduct = dataProduct.getTypeProduct();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("imgProducts");
        reference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference files: listResult.getItems()){
                if(files.getName().equals(dataProduct.getNameProduct())){
                    files.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(getView()).load(uri).into(binding.imgProduct);
                    });
                }
            }
        });

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        reference2.child("list_product").child(dataProduct.getId()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataProduct = snapshot.getValue(Product.class);
                if (dataProduct == null) {
                    return;
                }
                binding.tvNameProduct.setText(dataProduct.getNameProduct());
                binding.tvDescribe.setText(dataProduct.getDescribe());
                // cái này thì không biết lấy name type product kiểu gì luôn r
                binding.tvTypeProduct.setText(String.valueOf(typeProduct.getNameType()));
                binding.tvPriceProduct.setText(String.valueOf(dataProduct.getPrice()));
                binding.tvNoteProduct.setText(dataProduct.getNote());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}