package com.example.duan1_mananger.setting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.example.duan1_mananger.Oder.Adapter.ListOderAdapter;
import com.example.duan1_mananger.Oder.DetailReceiptFragment;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.DialogChooseTimeStatisticBinding;
import com.example.duan1_mananger.databinding.DialogFunctionImageProductBinding;
import com.example.duan1_mananger.databinding.FragmentOderStatisticsBinding;
import com.example.duan1_mananger.model.Receipt;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticalFragment extends BaseFragment implements ListOderAdapter.OnClickListener {
    private FragmentOderStatisticsBinding binding = null;
    private SettingViewModel viewModel;
    private ListOderAdapter adapter;
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    public StatisticalFragment() {
        // Required empty public constructor
    }

    public static StatisticalFragment newInstance() {
        StatisticalFragment fragment = new StatisticalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        binding = FragmentOderStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listening();
        initObSever();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void loadData() {
        viewModel.liveDateGetReceipt.observe(getViewLifecycleOwner(), new Observer<List<Receipt>>() {
            @Override
            public void onChanged(List<Receipt> receipts) {
                binding.tvOrderNumber.setText(receipts.size() + "");
                Double money = 0.0;
                for (Receipt receipt : receipts) {
                    money += receipt.getMoney();
                }
                binding.tvTotalOderValue.setText(money + " Đ");
                adapter = new ListOderAdapter((ArrayList<Receipt>) receipts,StatisticalFragment.this,0);
                binding.recVListOder.setAdapter(adapter);

            }
        });
    }

    @Override
    public void listening() {
        binding.icBack.setOnClickListener(v -> backStack());
        binding.tvNameTypeProduct.setOnClickListener(v -> dialogFunctionPickDate(requireContext()));

    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }

    private void dialogFunctionPickDate(Context context) {
        final Dialog dialog = new Dialog(context);
        DialogChooseTimeStatisticBinding bindingDialog = DialogChooseTimeStatisticBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(bindingDialog.getRoot());
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        bindingDialog.dialogChooserFunction.setTranslationY(150);
        bindingDialog.dialogChooserFunction.animate().translationYBy(-150).setDuration(400);

        bindingDialog.layoutChooserTimeStart.setOnClickListener(v -> {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    bindingDialog.tvTimeStart.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    lastSelectedYear = year;
                    lastSelectedMonth = monthOfYear;
                    lastSelectedDayOfMonth = dayOfMonth;
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    dateSetListener,
                    lastSelectedYear,
                    lastSelectedMonth,
                    lastSelectedDayOfMonth);
            datePickerDialog.show();
        });
        bindingDialog.layoutChooserTimeEnd.setOnClickListener(tv -> {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    bindingDialog.tvTimeEnd.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    lastSelectedYear = year;
                    lastSelectedMonth = monthOfYear;
                    lastSelectedDayOfMonth = dayOfMonth;
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    dateSetListener,
                    lastSelectedYear,
                    lastSelectedMonth,
                    lastSelectedDayOfMonth);
            datePickerDialog.show();
        });
        bindingDialog.btnFilter.setOnClickListener(v -> {
            viewModel.getReceiptByDate(
                    bindingDialog.tvTimeStart.getText().toString(),
                    bindingDialog.tvTimeEnd.getText().toString());
            dialog.cancel();
        });

        dialog.show();
    }

    @Override
    public void onClickListener(Receipt receipt) {
        replaceFragment(new DetailReceiptFragment(receipt));
    }
}