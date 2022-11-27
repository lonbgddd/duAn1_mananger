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
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.Receipt;
import com.example.duan1_mananger.model.Table;
import com.example.duan1_mananger.product.ProductFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailTableFragment extends BaseFragment {
    private FragmentAddOderBinding binding = null;
    private Table table;
    private String idProduct = null;
    private String idTable = null;
    public DetailTableFragment(Table table) {
        this.table = table;
    }
    private TableViewModel model = null;
    private List<String> listProduct;
    private String statusTable = "false";
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
        model = new  ViewModelProvider(this).get(TableViewModel.class);
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
            if (getArguments().getSerializable("table") != null){
                table = (Table) getArguments().getSerializable("table");
                idTable = String.valueOf(table.getId_table());
                statusTable = table.getStatus();
                binding.tvNameBill.setText(table.getName_table());
            } else {
                idProduct = getArguments().getString("idProduct");
                listProduct.add(idProduct);
                idTable = getArguments().getString("table_id");
                binding.tvNameBill.setText(getArguments().getString("table_name"));
            }
            model.listLiveData(listProduct);
        }

        binding.btnSaveOder.setOnClickListener(v -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OderSave");
            String key = reference.push().getKey();
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            Log.d("TAG", "listening: "+idTable);
            Receipt receipt = new Receipt(key,idTable,strDate,10000.0,listProduct);


            reference.child(key).setValue(receipt);
            model.setStatusTable(idTable);

            Toast.makeText(requireContext(), "Đã đặt bàn", Toast.LENGTH_SHORT).show();
        });
        if (statusTable.equals("true")){
            model.liveDataGetReceipt(idTable);
        }

        model.liveDataGetReceipt.observe(getViewLifecycleOwner(), new Observer<Receipt>() {
            @Override
            public void onChanged(Receipt receipt) {
                binding.btnSaveOder.setVisibility(View.GONE);
                model.listLiveData(receipt.getListIdProduct());
                binding.layoutAddProduct.setVisibility(View.GONE  );
            }
        });

    }

    @Override
    public void listening() {
        binding.icBack.setOnClickListener(v -> backStack());
        binding.imgList.setOnClickListener(v -> {
            ProductFragment productFragment = new ProductFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("table", table);
            productFragment.setArguments(bundle);
            replaceFragment(productFragment);
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
                Double money = 0.0;
                for (Product product:products
                     ) {
                    money += product.getPrice();
                }
                binding.tvTotalOder.setText(String.valueOf(money));
                binding.tvTotalAmount.setText(String.valueOf(money));
                OderAdapter adapter = new OderAdapter(products);
                binding.listProductOder.setAdapter(adapter);
            }
        });
        model.oderTableStatus.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String aBoolean) {
                if (aBoolean == "true"){
                    Toast.makeText(requireContext(), "Đặt bàn thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void initView() {

    }
}