package com.example.duan1_mananger.product;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import com.example.duan1_mananger.MainActivity;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentProductBinding;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.TypePoduct;
import com.example.duan1_mananger.product.Adapter.ProductApdater;
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
    public ProductApdater productAdapter;
    ArrayList<TypePoduct> listType;
    String NameType="";
    MainActivity mMainActivity;
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
        // Inflate the layout for this fragment
        mMainActivity = (MainActivity) getActivity();
        bindProduct = FragmentProductBinding.inflate(inflater,container,false);
        return bindProduct.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            TypePoduct typePoduct = (TypePoduct) bundle.get("objType");
            if(typePoduct!=null){
                NameType = typePoduct.getName_type();
                Log.e("aa", "onViewCreated: "+typePoduct.getName_type());
            }
        }

        get_Product();
        listProduct = new ArrayList<>();
        productAdapter = new ProductApdater(listProduct,NameType);
        bindProduct.listProduct.setAdapter(productAdapter);


        //add_product();
        // đã thêm cứng dữ liệu nên dào lại
        Log.e("aa", "onViewCreated: 2" );
        bindProduct.layoutType.setOnClickListener(layout ->{
            ReplaceTypeFragment();
            Log.e("aa", "onViewCreated: 3" );
        });

        bindProduct.searchViewProduct.clearFocus();
        bindProduct.searchViewProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

               Filterlist(newText,listProduct);
                return false;
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void listening() {

    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {


    }

    private  void get_Product(){
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference mRef = data.getReference("list_product");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for(DataSnapshot datasnapshot : snapshot.getChildren()){

                    Product product =  datasnapshot.getValue(Product.class);
                    Log.e(TAG, "onDataChange: " + product.toString());

                  //  Product product = datasnapshot.getValue(Product.class);
                    listProduct.add(product);
                }
                    bindProduct.tvCountProduct.setText("Có "+listProduct.size()+" sản phẩm");
                    productAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void Filterlist(String text,ArrayList<Product> listProduct) {
        ArrayList<Product> filterlist =new ArrayList<>();
        for (Product product: listProduct) {
            if(product.getName_product().toLowerCase().contains(text.toLowerCase())||product.getType_product().getName_type().toLowerCase().contains(text.toLowerCase())){
                filterlist.add(product);
            }
        }
        if(filterlist.isEmpty()){
        }else{
            productAdapter.setFilterList(filterlist);
            bindProduct.tvCountProduct.setText(filterlist.size()+" sản phẩm");
        }
    }
    private void ReplaceTypeFragment(){
        Window window = mMainActivity.getWindow();
        mMainActivity. getSupportFragmentManager().beginTransaction().add(R.id.fade_control, TypeProductFragment.newInstance()).commit();
        mMainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(mMainActivity.getColor(R.color.white));

    }


}