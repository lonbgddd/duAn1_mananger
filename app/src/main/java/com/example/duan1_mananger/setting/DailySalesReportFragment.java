package com.example.duan1_mananger.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.Oder.Adapter.ListOderAdapter;
import com.example.duan1_mananger.Oder.DetailReceiptFragment;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentSalesReportBinding;
import com.example.duan1_mananger.databinding.FragmentSettingBinding;
import com.example.duan1_mananger.model.Receipt;
import com.example.duan1_mananger.model.User;
import com.example.duan1_mananger.ui.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailySalesReportFragment extends BaseFragment implements  ListOderAdapter.OnClickListener{

    private FragmentSalesReportBinding binding = null;
    private SettingViewModel viewModel;
    private ListOderAdapter adapter;

    public DailySalesReportFragment() {
        // Required empty public constructor
    }

    public static DailySalesReportFragment newInstance() {
        DailySalesReportFragment fragment = new DailySalesReportFragment();
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
        binding = FragmentSalesReportBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Window window = getActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(getActivity().getColor(R.color.white));

        listening();
        initObSever();
        loadData();
    }

    @Override
    public void loadData() {
        Date toDay = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        String strToday = dateFormat.format(toDay);
        String strToday2 = dateFormat2.format(toDay);
        binding.tvDate.setText(strToday2);
        viewModel.getReceiptByToDay(strToday);
        viewModel.liveDateGetReceiptToDay.observe(getViewLifecycleOwner(), new Observer<List<Receipt>>() {
            @Override
            public void onChanged(List<Receipt> receipts) {
                if(receipts.size() == 0){
                    binding.layoutNotificationNullData.setVisibility(View.VISIBLE);
                    binding.tvOrderNumber.setText("0");
                    binding.tvTotalOderValue.setText("0");
                    binding.viewHeader.setVisibility(View.GONE);
                    binding.recVListOder.setVisibility(View.GONE);
                }else {
                    binding.layoutNotificationNullData.setVisibility(View.GONE);
                    binding.viewHeader.setVisibility(View.VISIBLE);
                    binding.recVListOder.setVisibility(View.VISIBLE);
                    binding.tvOrderNumber.setText(receipts.size() + "");
                    Double money = 0.0;
                    for (Receipt receipt : receipts) {
                        money += receipt.getMoney();
                    }
                    Locale locale = new Locale("en", "EN");
                    NumberFormat numberFormat = NumberFormat.getInstance(locale);
                    String strMoney = numberFormat.format(money);

                    binding.tvTotalOderValue.setText(strMoney);
                    adapter = new ListOderAdapter((ArrayList<Receipt>) receipts, DailySalesReportFragment.this, 0);
                    LinearLayoutManager layoutManager  = new LinearLayoutManager(getContext());
                    layoutManager.setStackFromEnd(true);
                    layoutManager.setReverseLayout(true);
                    binding.recVListOder.setLayoutManager(layoutManager);
                    binding.recVListOder.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    public void listening() {
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


    @Override
    public void onClickListener(Receipt receipt) {
        replaceFragment(new DetailReceiptFragment(receipt));
    }
}