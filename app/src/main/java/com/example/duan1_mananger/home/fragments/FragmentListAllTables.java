package com.example.duan1_mananger.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentListAllTablesBinding;
import com.example.duan1_mananger.databinding.FragmentSlideImageBinding;


public class FragmentListAllTables extends BaseFragment {
    private FragmentListAllTablesBinding binding;

    public FragmentListAllTables() {
        // Required empty public constructor
    }

    public static FragmentListAllTables newInstance() {
        FragmentListAllTables fragment = new FragmentListAllTables();
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
        binding = FragmentListAllTablesBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
}