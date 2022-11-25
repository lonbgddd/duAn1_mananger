package com.example.duan1_mananger.product;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.DialogAddTypeProductBinding;
import com.example.duan1_mananger.databinding.FragmentTypeProductBinding;
import com.example.duan1_mananger.model.TypeProduct;
import com.example.duan1_mananger.product.Adapter.TypeProductAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TypeProductFragment extends BaseFragment {
    private FragmentTypeProductBinding binding = null;
    public ArrayList<TypeProduct> listType;
    private TypeProductAdapter typeAdapter;

    public TypeProductFragment() {
        // Required empty public constructor
    }
    public static TypeProductFragment newInstance() {
        TypeProductFragment fragment = new TypeProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentTypeProductBinding.inflate(inflater,container,false);
       return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listType = new ArrayList<>();
        typeAdapter = new TypeProductAdapter(listType);
        binding.listsTypeProduct.setAdapter(typeAdapter);
        listening();
        loadData();


    }

    @Override
    public void loadData() {
        getTypeProduct();

    }

    @Override
    public void listening() {
        binding.icShowSearch.setOnClickListener(ic ->{
            binding.tvTitle.setVisibility(View.GONE);
            binding.icShowSearch.setVisibility(View.GONE);
            binding.icAddType.setVisibility(View.GONE);
            binding.tvCloseSearchView.setVisibility(View.VISIBLE);
            binding.searchViewTypeProduct.setVisibility(View.VISIBLE);

        });
        binding.tvCloseSearchView.setOnClickListener(ic ->{
            binding.tvTitle.setVisibility(View.VISIBLE);
            binding.icShowSearch.setVisibility(View.VISIBLE);
            binding.icAddType.setVisibility(View.VISIBLE);
            binding.tvCloseSearchView.setVisibility(View.GONE);
            binding.searchViewTypeProduct.setVisibility(View.GONE);
            binding.tvAllProduct.setVisibility(View.VISIBLE);
        });
        binding.searchViewTypeProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterListType(newText);
                binding.tvAllProduct.setVisibility(View.GONE);
                return false;
            }
        });

        binding.tvAllProduct.setOnClickListener(tv ->{
            replaceFragment(new ProductFragment().newInstance());
        });
        binding.icAddType.setOnClickListener(ic ->{
            dialogAddTypeProduct(getContext());
        });
        binding.icBack.setOnClickListener(ic ->{
              backStack();
        });
    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }


    public void dialogAddTypeProduct(Context context) {
        final Dialog dialog = new Dialog(context);
        DialogAddTypeProductBinding binding = DialogAddTypeProductBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.tvCancel.setOnClickListener(tv ->{
            dialog.dismiss();
        });

        binding.tvAdd.setOnClickListener(add->{
            FirebaseDatabase data = FirebaseDatabase.getInstance();
            DatabaseReference mRef = data.getReference("list_type_product");
            String key = mRef.push().getKey();
            if(TextUtils.isEmpty(binding.edNameType.getText().toString())){
                Toast.makeText(context, "Hãy nhập tên loại !"  , Toast.LENGTH_SHORT).show();
            }else {
                TypeProduct typeProduct = new TypeProduct(key,binding.edNameType.getText().toString().trim());
                mRef.child(key).setValue(typeProduct).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Thêm loại thành công", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void getTypeProduct(){
        DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference("list_type_product");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listType.clear();
                for(DataSnapshot datasnapshot : snapshot.getChildren()){
                    TypeProduct type = datasnapshot.getValue(TypeProduct.class);
                    listType.add(type);
                }
                typeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        typeAdapter = new TypeProductAdapter(listType, new TypeProductAdapter.OnClickItemListener() {
            @Override
            public void onClickItemProduct(TypeProduct typeProduct) {
                replaceFragment(new ProductFragment(typeProduct));
            }
        });
        binding.listsTypeProduct.setAdapter(typeAdapter);


    }


    private void filterListType(String text) {
        ArrayList<TypeProduct> filterListType =new ArrayList<>();
        for (TypeProduct type : listType) {
            if(type.getNameType().toLowerCase().contains(text.toLowerCase())){
                filterListType.add(type);
            }
        }
        if(filterListType.isEmpty()){
        }else{
            typeAdapter.setFilterListType(filterListType);
        }
    }




}