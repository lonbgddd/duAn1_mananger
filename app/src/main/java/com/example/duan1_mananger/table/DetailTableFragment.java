package com.example.duan1_mananger.table;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentAddOderBinding;
import com.example.duan1_mananger.home.HomeFragment;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.Receipt;
import com.example.duan1_mananger.model.Table;
import com.example.duan1_mananger.table.adapter.OderAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailTableFragment extends BaseFragment {
    private FragmentAddOderBinding binding = null;
    private Table table;
    private ArrayList<String> idProduct = null;
    private String idTable = null;
    private Receipt receiptModel;

    public DetailTableFragment(Table table) {
        this.table = table;
    }

    private TableViewModel model = null;
    private ArrayList<String> listProduct;
    private String statusTable = "false";
    private Double totalMoney = 0.0;
    private Receipt receipt;

    public DetailTableFragment() {
    }

    public static DetailTableFragment newInstance(Table table) {
        DetailTableFragment fragment = new DetailTableFragment(table);
        Bundle args = new Bundle();
        args.putSerializable("table", (Serializable) table);
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
        binding = FragmentAddOderBinding.inflate(inflater, container, false);
        table = new Table();
        listProduct = new ArrayList<>();
        model = new ViewModelProvider(this).get(TableViewModel.class);
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
        if (getArguments() != null) {
            if (getArguments().getSerializable("table") != null) {
                table = (Table) getArguments().getSerializable("table");
                idTable = String.valueOf(table.getId_table());
                statusTable = table.getStatus();
                binding.tvNameBill.setText(table.getName_table());
            } else {
                idProduct = getArguments().getStringArrayList("list_product_select");
                listProduct.addAll(idProduct);
                idTable = getArguments().getString("table_id");
                binding.tvNameBill.setText(getArguments().getString("table_name"));
            }
            model.listLiveData(listProduct);
        }
        binding.btnSaveOder.setOnClickListener(v -> {
            if (binding.listProductOder.getVisibility() == View.GONE) {
                notificationErrInput(getContext(), "Hãy chọn món !");
            } else {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OderSave");
                String key = reference.push().getKey();
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(date);

                receipt = new Receipt(key, idTable, strDate, totalMoney, listProduct, binding.edNoteOder.getText().toString());

                reference.child(key).setValue(receipt);
                reference.child(key).setValue(receipt);
                model.setStatusTable(idTable, "true");

                replaceFragment(HomeFragment.newInstance());
            }
        });
        if (statusTable.equals("true")) {
            model.liveDataGetReceipt(idTable);
        }

        model.liveDataGetReceipt.observe(getViewLifecycleOwner(), new Observer<Receipt>() {
            @Override
            public void onChanged(Receipt receipt) {
                binding.btnSaveOder.setVisibility(View.GONE);
                receiptModel = receipt;
                model.listLiveData(receipt.getListIdProduct());
                binding.layoutAddProduct.setVisibility(View.GONE);
                binding.edNoteOder.setEnabled(false);
                binding.edNoteOder.setText(receipt.getNoteOder() + "");
            }
        });

        //Thanh toán dơn
        binding.btnPayOder.setOnClickListener(btn -> {
            if (binding.listProductOder.getVisibility() == View.GONE) {
                notificationErrInput(getContext(), "Hãy chọn món !");
            } else {
                model.setStatusTable(idTable, "false");
                model.liveDataPayReceipt(receiptModel);
                backStack();
            }
        });
        model.liveDataGetReceipt.observe(getViewLifecycleOwner(), new Observer<Receipt>() {
            @Override
            public void onChanged(Receipt receipt) {
                binding.btnSaveOder.setVisibility(View.GONE);
                receiptModel = receipt;
                model.listLiveData(receipt.getListIdProduct());
                binding.layoutAddProduct.setVisibility(View.GONE  );
            }
        });
        model.liveDataPayReceipt.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("success")){
                    Toast.makeText(requireContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Thanh toán thất bại", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    @Override
    public void listening() {
        binding.icBack.setOnClickListener(v -> backStack());
        binding.layoutAddProduct.setOnClickListener(v -> {
            FragmentListAllProductToOder productToOder = new FragmentListAllProductToOder();
            Bundle bundle = new Bundle();
            bundle.putSerializable("table", table);
            productToOder.setArguments(bundle);
            replaceFragment(productToOder);
        });
        binding.btnPayOder.setOnClickListener(v -> {
            model.liveDataPayReceipt(receiptModel);
            model.setStatusTable(idTable, "false");
        });

    }

    @Override
    public void initObSever() {
        model.listProductOder.observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                binding.layoutAddProduct.setVisibility(View.GONE);
                binding.listProductOder.setVisibility(View.VISIBLE);

                int number = products.size();
                binding.tvAmountProduct.setText(String.valueOf(number));

                for (Product product : products) {
                    totalMoney += product.getPrice();
                }
                Locale locale = new Locale("en", "EN");
                NumberFormat numberFormat = NumberFormat.getInstance(locale);
                String strMoney = numberFormat.format(totalMoney);

                binding.tvTotalOder.setText(strMoney);
                binding.tvTotalAmount.setText(strMoney);

                OderAdapter adapter = new OderAdapter(products);
                binding.listProductOder.setAdapter(adapter);

            }
        });
        model.oderTableStatus.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String aBoolean) {
                if (aBoolean == "true") {
                    notificationSuccessInput(getContext(), "Đặt bàn thành công!");
                }
            }
        });
    }

    @Override
    public void initView() {

    }


}