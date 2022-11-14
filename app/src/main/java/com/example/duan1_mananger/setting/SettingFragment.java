package com.example.duan1_mananger.setting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentSettingBinding;

public class SettingFragment extends BaseFragment {
    private FragmentSettingBinding binding = null;
    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        binding = FragmentSettingBinding.inflate(inflater,container,false);
        return binding.getRoot();
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