package com.example.duan1_mananger.product;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentMaketBinding;
import com.example.duan1_mananger.databinding.FragmentProductBinding;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductFragment extends BaseFragment {
    private FragmentProductBinding binding = null;
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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.layoutType.setOnClickListener(layout ->{
            layoutTypeProduct(getContext());
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


    private void layoutTypeProduct(Context context){
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Material_Light_NoActionBar);
        dialog.setContentView(R.layout.layout_product_type);
        Window window = dialog.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(context.getColor(R.color.white));
        dialog.setCancelable(false);
        dialog.findViewById(R.id.ic_back).setOnClickListener(ic ->{
            dialog.dismiss();
        });
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvCloseSearchView = dialog.findViewById(R.id.tvCloseSearchView);
        ImageView icAddType = dialog.findViewById(R.id.icAddType);
        ImageView icSearch = dialog.findViewById(R.id.ic_show_search);
        SearchView searchViewType = dialog.findViewById(R.id.search_view_type_product);

        icSearch.setOnClickListener(ic ->{
            tvTitle.setVisibility(View.GONE);
            icSearch.setVisibility(View.GONE);
            icAddType.setVisibility(View.GONE);
            tvCloseSearchView.setVisibility(View.VISIBLE);
            searchViewType.setVisibility(View.VISIBLE);
        });
        tvCloseSearchView.setOnClickListener(ic ->{
            tvTitle.setVisibility(View.VISIBLE);
            icSearch.setVisibility(View.VISIBLE);
            icAddType.setVisibility(View.VISIBLE);
            tvCloseSearchView.setVisibility(View.GONE);
            searchViewType.setVisibility(View.GONE);
        });
        icAddType.setOnClickListener(ic ->{
            dialogAddTypeProduct(context);
        });



        dialog.show();

    }


    public void dialogAddTypeProduct(Context context ) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_type_product);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(tv ->{
            dialog.cancel();
        });


        dialog.show();


    }


}