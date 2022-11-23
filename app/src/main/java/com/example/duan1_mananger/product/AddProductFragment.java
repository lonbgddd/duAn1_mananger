package com.example.duan1_mananger.product;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentAddProductBinding;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.TypePoduct;
import com.example.duan1_mananger.product.Adapter.SpipnerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class AddProductFragment extends BaseFragment {

    private FragmentAddProductBinding binding;
    private Product product = null;
    private ArrayList<Product> listProduct;
    private ArrayList<TypePoduct> listType;
    private static final int PICL_IMAGES_CODE = 1001;
    private TypePoduct typePoduct;
    private Spinner spinner;
    Uri imgProduct;
    FirebaseAuth firebaseAuth;

    public AddProductFragment() {
        // Required empty public constructor
    }


    public static AddProductFragment newInstance() {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        listProduct = new ArrayList<>();
        listType = new ArrayList<>();
        spinner = binding.spinerType;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            ArrayList<Product> list = (ArrayList<Product>) bundle.get("listProduct");
            if(list!=null){
                listProduct = list;
                Log.e("list", "onViewCreated: "+listProduct.size());
            }
        }

        loadData();
        listening();
        initObSever();
    }

    @Override
    public void loadData() {
        getTypeProduct();

    }

    @Override
    public void listening() {
        binding.icClose.setOnClickListener(v -> {
            backStack();
        });

        binding.icSaveProduct.setOnClickListener(v -> {
            dialogConfirmUpdate(getContext());
        });

        binding.btnSaveProduct.setOnClickListener(v -> {
            dialogConfirmUpdate(getContext());
        });

//        binding.icChoosertYPE.setOnClickListener(v -> {
//            replaceFragment(new TypeProductFragment().newInstance());
//        });

        binding.icAddImg.setOnClickListener(v -> {
            requestPermission();
        });

    }



    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }
    private void getTypeProduct(){
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

        SpipnerAdapter spipnerAdapter = new SpipnerAdapter(listType);
        spinner.setAdapter(spipnerAdapter);
        typePoduct = (TypePoduct) spinner.getSelectedItem();

    }

    private void dialogConfirmUpdate(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm sản phẩm");
//        builder.setIcon(context.getDrawable(R.drawable.icon_save));
        builder.setMessage("Bạn chắc chắn muốn thêm " + binding.edNameProduct.getText().toString().trim() + " vào menu");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveProduct(listProduct);
                cleanEditText();
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





    private void saveProduct(ArrayList<Product> list){
        Log.d("TAG", "saveProduct: "+binding.imgProduct.getImageAlpha());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        product = new Product(binding.edNameProduct.getText().toString().trim(), Double.parseDouble(binding.edPrice.getText().toString().trim()),
                typePoduct,
                binding.edNote.getText().toString().trim());
        list.add(product);

        reference.child("Products").setValue(listProduct, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "OKE", Toast.LENGTH_SHORT).show();
            }
        });

        if(imgProduct != null){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("imgProducts/"+product.getName_product());
            storageReference.putFile(imgProduct).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getContext(), "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(command -> {
                Toast.makeText(getContext(), "Cập nhật ảnh thất bại", Toast.LENGTH_SHORT).show();
            });
        }
    }


    private void cleanEditText(){
        binding.edNameProduct.setText("");
        binding.edDescribe.setText("");
//        binding.edTypeProduct.setText("");
        binding.edPrice.setText("");
        binding.edNote.setText("");
        binding.tvAddImgProduct.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(R.drawable.ic_product).into(binding.imgProduct);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }

        } else {
            Log.d("TAG", "requestPermission: 11111111111");
            addImage();
        }
    }

    private void addImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICL_IMAGES_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICL_IMAGES_CODE) {
                imgProduct = data.getData();
                binding.imgProduct.setImageURI(imgProduct);
                if(imgProduct != null){
                    binding.tvAddImgProduct.setVisibility(View.INVISIBLE);
                }
                Log.d("TAG", "onActivityResult: "+data.getData());

            }
        }
    }

}