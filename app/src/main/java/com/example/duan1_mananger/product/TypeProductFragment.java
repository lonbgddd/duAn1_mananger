package com.example.duan1_mananger.product;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duan1_mananger.MainActivity;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.databinding.FragmentTypeProductBinding;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.TypePoduct;
import com.example.duan1_mananger.product.Adapter.TypeProductAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TypeProductFragment extends Fragment {
    private FragmentTypeProductBinding binding = null;
    public ArrayList<TypePoduct> listType;
    private TypeProductAdapter typeAdapter;
    private MainActivity mainActivity;

    public TypeProductFragment() {
        // Required empty public constru
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
        mainActivity = (MainActivity) getActivity();
       binding = FragmentTypeProductBinding.inflate(inflater,container,false);
       return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.searchViewTypeProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Filterlist_type(newText);
                return false;
            }
        });
        //addProduct_type();
        // đã thêm cứng dữ liệu nên dào lại
        getProduct_type();
        listType = new ArrayList<>();
        Log.e("TAG", "onViewCreated: 4" );
        typeAdapter = new TypeProductAdapter(listType, new TypeProductAdapter.IClickItemListener() {
            @Override
            public void onClickItemType(TypePoduct typePoduct) {
               mainActivity.ReplaceProductFragment(typePoduct);

            }
        });
        binding.listsTypeProduct.setAdapter(typeAdapter);
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
        });
        binding.icAddType.setOnClickListener(ic ->{
            dialogAddTypeProduct(getContext(),listType);
        });
        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceProductFragment();
            }
        });
    }
    public void dialogAddTypeProduct(Context context ,ArrayList<TypePoduct>list) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_type_product);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        EditText edNameType =dialog.findViewById(R.id.edNameType);
        tvCancel.setOnClickListener(tv ->{
            dialog.dismiss();
        });
        TextView tvAdd = dialog.findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(add->{
            FirebaseDatabase data = FirebaseDatabase.getInstance();
            DatabaseReference mRef = data.getReference("list_type_product");
            list.add(new TypePoduct(edNameType.getText().toString().trim()));
            mRef.setValue(list, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                }
            });
            dialog.dismiss();
        });
        dialog.show();
    }
    private void addProduct_type(){
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference mRef = data.getReference("list_type_product");
        ArrayList<TypePoduct> list_type = new ArrayList<>();
        list_type.add(new TypePoduct(0,"Coffee"));
        list_type.add(new TypePoduct(1,"Sinh tố"));
        list_type.add(new TypePoduct(2,"Bánh ngọt"));
        mRef.setValue(list_type, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });
    }
    private  void getProduct_type(){
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference mRef = data.getReference("list_type_product");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listType.clear();
                for(DataSnapshot datasnapshot : snapshot.getChildren()){
                    TypePoduct type = datasnapshot.getValue(TypePoduct.class);
                    listType.add(type);
                }
                typeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void Filterlist_type(String text) {
        ArrayList<TypePoduct> filterlisttype =new ArrayList<>();
        for (TypePoduct type_poduct : listType) {
            if(type_poduct.getName_type().toLowerCase().contains(text.toLowerCase())){
                filterlisttype.add(type_poduct);
            }
        }
        if(filterlisttype.isEmpty()){
        }else{
            typeAdapter.setFilterListType(filterlisttype);
        }
    }
    private void ReplaceProductFragment(){
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fade_control, ProductFragment.newInstance()).commit();
        mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = mainActivity.getWindow();
        window.setStatusBarColor(mainActivity.getColor(R.color.white));
    }

}