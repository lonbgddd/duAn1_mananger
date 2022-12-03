package com.example.duan1_mananger.table;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duan1_mananger.PushNotification.MyNotificator;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentAddOderBinding;
import com.example.duan1_mananger.home.HomeFragment;
import com.example.duan1_mananger.maket.MarketFragment;
import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.Receipt;
import com.example.duan1_mananger.model.Table;
import com.example.duan1_mananger.table.adapter.OderAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

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
    private ArrayList<String> getIdProduct = null;
    private ArrayList<Integer> getCountProduct = null;
    private String idTable = null;
    private Receipt receiptModel;
    private TableViewModel model = null;
    private String statusTable = "false";
    private Double totalMoney = 0.0;
    private ArrayList<String> listIdProduct;
    private ArrayList<Integer>listCountProduct = null;
    private MyNotificator myNotificator;

    public DetailTableFragment(Table table) {
        this.table = table;
    }

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
        listIdProduct = new ArrayList<>();
        getCountProduct = new ArrayList<>();
        getIdProduct = new ArrayList<>();
        listCountProduct = new ArrayList<>();
        myNotificator = new MyNotificator();
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
                if (getArguments().getStringArrayList("list_product_select") != null) {
                    getIdProduct = getArguments().getStringArrayList("list_product_select");
                    getCountProduct = getArguments().getIntegerArrayList("list_count_product");
                    listIdProduct.addAll(getIdProduct);
                    listCountProduct.addAll(getCountProduct);

                }
                statusTable = table.getStatus();
                binding.tvNameBill.setText(table.getName_table());
                if (table.getName_table() == null) {
                    binding.tvNameBill.setText("Bán mang về");
                    binding.btnPayOder.setVisibility(View.VISIBLE);
                    binding.btnSaveOder.setVisibility(View.GONE);
                }
            } else {
                binding.tvNameBill.setText("Bán mang về");
                binding.btnPayOder.setVisibility(View.VISIBLE);
                binding.btnSaveOder.setVisibility(View.VISIBLE);
            }
            model.listLiveData(listIdProduct);
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

                receiptModel = new Receipt(key, idTable, strDate, totalMoney, listIdProduct,listCountProduct, binding.edNoteOder.getText().toString());
                reference.child(key).setValue(receiptModel);
                model.setStatusTable(idTable, "true");
                replaceFragment(HomeFragment.newInstance());
            }
        });
        if (statusTable.equals("true")) {
            model.liveDataGetReceipt(idTable);

        }
        if (table.getName_table() != null) {
            model.liveDataGetReceipt.observe(getViewLifecycleOwner(), new Observer<Receipt>() {
                @Override
                public void onChanged(Receipt receipt) {
                    binding.btnSaveOder.setVisibility(View.GONE);
                    binding.btnPayOder.setVisibility(View.VISIBLE);
                    receiptModel = receipt;
                    model.listLiveData(receipt.getListIdProduct());


                    model.listProductOder.observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        for(int i = 0 ; i < products.size(); i ++){
                            for(int k = i; k < receiptModel.getListCountProduct().size(); k++){
                                products.get(k).setIsClick(receiptModel.getListCountProduct().get(k));
                            }
                        }

                        OderAdapter adapter = new OderAdapter(products);
                        int totalProduct = 0;
                        for(int i = 0; i < receipt.getListCountProduct().size(); i++){
                            totalProduct += receipt.getListCountProduct().get(i);
                        }
                        binding.tvAmountProduct.setText(String.valueOf(totalProduct));

                        Locale locale = new Locale("en", "EN");
                        NumberFormat numberFormat = NumberFormat.getInstance(locale);
                        String strMoney = numberFormat.format(receiptModel.getMoney());

                        binding.tvTotalOder.setText(strMoney);
                        binding.tvTotalAmount.setText(strMoney);
                        binding.listProductOder.setAdapter(adapter);

                    }
                });

                    binding.layoutAddProduct.setVisibility(View.GONE);
                    binding.edNoteOder.setEnabled(false);
                    binding.edNoteOder.setText(receipt.getNoteOder() + "");
                }
            });
        }
        binding.btnPayOder.setOnClickListener(btn -> {
            if (binding.listProductOder.getVisibility() == View.GONE) {
                notificationErrInput(getContext(), "Hãy chọn món !");
            } else if (receiptModel != null) {
                model.setStatusTable(idTable, "false");
                model.liveDataPayReceipt(receiptModel);
                backStack();
            } else {
                DatabaseReference reference;
                reference = FirebaseDatabase.getInstance().getReference("PayReceipt");
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(date);
                String key = reference.push().getKey();
                Receipt receipt = new Receipt(key, "", strDate, totalMoney, listIdProduct,listCountProduct, binding.edNoteOder.getText().toString());
                reference.child(key).setValue(receipt);
                notificationSuccessInput(getContext(), "Thanh toán thành công!");
                replaceFragment(MarketFragment.newInstance());
            }
        });

        model.liveDataGetReceipt.observe(getViewLifecycleOwner(), new Observer<Receipt>() {
            @Override
            public void onChanged(Receipt receipt) {
                binding.btnSaveOder.setVisibility(View.GONE);
                receiptModel = receipt;
                model.listLiveData(receipt.getListIdProduct());
                binding.layoutAddProduct.setVisibility(View.GONE);
            }
        });

        model.liveDataPayReceipt.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("success")) {
                    notificationSuccessInput(getContext(), "Thanh toán thành công!");
                } else {
                    notificationErrInput(getContext(), "Thanh toán thất bại");
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
                for(int i = 0 ; i < products.size(); i ++){
                    for(int k = i; k < listCountProduct.size(); k++){
                        products.get(k).setIsClick(listCountProduct.get(k));
                    }
                }

                OderAdapter adapter = new OderAdapter(products);
                int totalProduct = 0;
                for(int i = 0; i < listCountProduct.size(); i++){
                    totalProduct += listCountProduct.get(i);
                }
                binding.tvAmountProduct.setText(String.valueOf(totalProduct));

                for (Product product : products) {
                    totalMoney += (product.getPrice() * product.getIsClick());
                }

                Locale locale = new Locale("en", "EN");
                NumberFormat numberFormat = NumberFormat.getInstance(locale);
                String strMoney = numberFormat.format(totalMoney);

                binding.tvTotalOder.setText(strMoney);
                binding.tvTotalAmount.setText(strMoney);
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