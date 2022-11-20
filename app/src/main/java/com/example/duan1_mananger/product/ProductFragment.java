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

    private void add_product(){
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference mRef= data.getReference("list_product");
        ArrayList<Product> list_product = new ArrayList<>();
        listType = new ArrayList<>();
        TypePoduct Coffee = new TypePoduct("Coffee");
        TypePoduct  Sinhto = new TypePoduct("Sinh tố");
        TypePoduct Banh = new TypePoduct("Bánh ngọt");
        list_product.add( new Product(0,"Campuchino",35000,"Coffee nóng có lớp bọt sữa",Coffee,R.drawable.img_capuchino));
        list_product.add( new Product(1,"Capuchino Vienese",20000,"Coffee nóng có lớp kem",Coffee,R.drawable.img_capuchino_viennese));
        list_product.add( new Product(2,"Late machiato",25000,"Coffee lạnh có lớp bọt sữa",Coffee,R.drawable.img_latte_machiato));
        list_product.add( new Product(3,"Coffee Hot",20000,"Coffee nóng",Coffee,R.drawable.img_coffee_hot));
        list_product.add( new Product(4,"Espresson Con Panna",20000,"Coffee thường có lớp kem",Coffee,R.drawable.img_espresso_con_panna));
        list_product.add( new Product(5,"Mocha Matcha",20000,"Lớp kem vị trà xanh",Coffee,R.drawable.img_mocha_matcha));
        list_product.add(new Product(6,"Bạc xỉu",20000,"Coffee có nhiều sữa",Coffee,R.drawable.img_bacxiu));
        list_product.add(new Product(7,"Coffee Milk",20000,"Coffee sữa ",Coffee,R.drawable.img_cofffe_sua));
        list_product.add(new Product(8,"Nước Cam",25000,"cam ép tự nhiên",Sinhto,R.drawable.img_orange_juice));
        list_product.add(new Product(9,"Nước Lựu",30000,"lựu ép tự nhiên",Sinhto,R.drawable.img_pomegranate_juice));
        list_product.add(new Product(10,"Nước Táo",20000,"táo ép tự nhiên",Sinhto,R.drawable.img_apple_juice));
        list_product.add(new Product(11,"Nước Nho",30000,"táo ép tự nhiên",Sinhto,R.drawable.img_grape_juice));
        list_product.add(new Product(12,"Sinh Tố Bơ",30000,"quả bơ say với sữa,đá",Sinhto,R.drawable.img_avocado_juice));
        list_product.add(new Product(13,"Sinh Tố Cam Dứa",30000,"nước ép cam với dứa",Sinhto,R.drawable.img_pineapple_juice));
        list_product.add(new Product(14,"Sinh Tố Dưa Hấu",30000,"nước ép dưa với táo ",Sinhto,R.drawable.img_watermelon));
        list_product.add(new Product(15,"Sinh Tố Dưa Hấu",30000,"nước ép xoài với sữa ",Sinhto,R.drawable.img_mangoes_juice));
        list_product.add(new Product(16,"Bánh Donut",15000,"bánh nương",Banh,R.drawable.img_donut));
        list_product.add(new Product(17,"Bánh Cupcake",25000,"bánh nướng ",Banh,R.drawable.img_cupcake));
        list_product.add(new Product(18,"Bánh Cupcake Dâu",25000,"bánh nướng",Banh,R.drawable.img_cupcake_dau));
        list_product.add(new Product(19,"Bánh Mochi",10000,"bánh bột gạo",Banh,R.drawable.img_mochi));
        list_product.add(new Product(20,"Bánh Muffin Socola",15000,"bánh nướng vị socola",Banh,R.drawable.img_muffin));
        list_product.add(new Product(21,"Bánh Muffin ",15000,"bánh nướng",Banh,R.drawable.muffin_defaut));
        list_product.add(new Product(22,"Bánh Pudding",50000,"thạch",Banh,R.drawable.img_pudding));
       mRef.setValue(list_product, new DatabaseReference.CompletionListener() {
           @Override
           public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
           }
       });
    }
    private  void get_Product(){
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference mRef = data.getReference("list_product");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for(DataSnapshot datasnapshot : snapshot.getChildren()){
                    Product product = datasnapshot.getValue(Product.class);
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