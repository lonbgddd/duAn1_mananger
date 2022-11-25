package com.example.duan1_mananger.product;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentProductBinding;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.TypeProduct;
import com.example.duan1_mananger.product.Adapter.ProductAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductFragment extends BaseFragment {
    public static final String TAG = ProductFragment.class.getName();
    private FragmentProductBinding bindProduct = null;
    private ArrayList<Product> listProduct;
    public ProductAdapter productAdapter = null;
    FirebaseDatabase database;
    ArrayList<TypeProduct> listType;
    String NameType="";


    public ProductFragment() {
        // Required empty public constructor
    }
    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
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
        bindProduct = FragmentProductBinding.inflate(inflater,container,false);
        return bindProduct.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            TypeProduct typeProduct = (TypeProduct) bundle.get("objType");
            if(typeProduct!=null){
                NameType = typeProduct.getNameType();
            }
        }
//        Log.d("TAG", "onViewCreated: "+NameType);
        listProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(listProduct,NameType);
        bindProduct.listProduct.setAdapter(productAdapter);

        listening();
        initObSever();
    }

    @Override
    public void loadData() {
        getProduct();
    }

    @Override
    public void listening() {
        bindProduct.fabAddProduct.setOnClickListener(v -> {

            replaceFragment(new AddProductFragment().newInstance());
        });
        bindProduct.layoutType.setOnClickListener(layout ->{
            replaceFragment(new TypeProductFragment().newInstance());
        });

        bindProduct.searchViewProduct.clearFocus();
        bindProduct.searchViewProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText,listProduct);
                return false;
            }
        });
    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {


    }

    private void getProduct(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("list_product");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for(DataSnapshot datasnapshot : snapshot.getChildren()){
                    Product product =  datasnapshot.getValue(Product.class);
                    listProduct.add(product);
                }
                    bindProduct.tvCountProduct.setText("Có "+listProduct.size()+" sản phẩm");
                    productAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // chưa tìm được cách khác nên thôi dùng cashc này tuy hơi cồng kềnh.
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if(product == null || listProduct == null || listProduct.isEmpty()){
                    return;
                }
                for(int i = 0; i < listProduct.size(); i++){
                    if(product.getId() == listProduct.get(i).getId()){
                        listProduct.remove(listProduct.get(i));
                        break;
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productAdapter = new ProductAdapter(listProduct, new ProductAdapter.OnClickItemListener() {
            @Override
            public void onClickItempProduct(Product product) {

                replaceFragment(new DetailProductFragment(product, NameType));

            }
        });
        bindProduct.listProduct.setAdapter(productAdapter);

    }


    private void filterList(String text,ArrayList<Product> listProduct) {
        ArrayList<Product> filterLists =new ArrayList<>();
        for (Product product: listProduct) {
            if(product.getNameProduct().toLowerCase().contains(text.toLowerCase())){
                filterLists.add(product);
            }
        }
        if(filterLists.isEmpty()){

        }else{
            productAdapter.setFilterList(filterLists);
            bindProduct.tvCountProduct.setText(filterLists.size()+" sản phẩm.");
        }
    }


}