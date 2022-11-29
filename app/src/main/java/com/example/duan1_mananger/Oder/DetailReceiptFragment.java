package com.example.duan1_mananger.Oder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentAddOderBinding;
import com.example.duan1_mananger.databinding.FragmentDetailsProductBinding;
import com.example.duan1_mananger.databinding.LayoutFullImageProductBinding;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.Receipt;
import com.example.duan1_mananger.product.UpdateProductFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailReceiptFragment extends BaseFragment {

    private FragmentAddOderBinding binding = null;
    private Receipt dataReceipt = null;


    public DetailReceiptFragment(Receipt receipt) {
        this.dataReceipt = receipt;

    }

    public DetailReceiptFragment() {
    }

    public DetailReceiptFragment newInstance() {
        return new DetailReceiptFragment(dataReceipt);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddOderBinding.inflate(inflater, container, false);
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
        binding.constraintLayout.setVisibility(View.GONE);
        binding.icBack.setOnClickListener(v->{
            backStack();
        });
    }

    @Override
    public void initObSever() {
        showDetailProduct();
    }


    @Override
    public void initView() {

    }
    private void showDetailProduct() {
        Locale locale = new Locale("en","EN");
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        binding.tvAmountProduct.setText(dataReceipt.getListIdProduct().size()+"");
        binding.tvNameBill.setText("POLY000"+dataReceipt.getIdReceipt().substring(16,20));
        binding.edNoteOder.setText(dataReceipt.getNoteOder());
        binding.edNoteOder.setEnabled(false);
        Double Money =dataReceipt.getMoney();
        String strMoney = numberFormat.format(Money);
        binding.tvTotalAmount.setText(strMoney);
    }




}