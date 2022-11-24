package com.example.duan1_mananger.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.databinding.LayoutErrorInputBinding;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
                R.anim.slide_out_right).replace(R.id.fade_control, fragment).addToBackStack(null).commit();
    }

    public void errSnackBarInput(Context context,String textErr){
        LayoutErrorInputBinding binding = LayoutErrorInputBinding.inflate(LayoutInflater.from(context));
        final  Snackbar snackbar = Snackbar.make(binding.getRoot(),"", Snackbar.LENGTH_SHORT);

        binding.tvErr.setText(textErr);

        snackbar.show();

    }

    abstract public void loadData();

    abstract public void listening();

    abstract public void initObSever();

    public void backStack() {
        getParentFragmentManager().popBackStack();
    }

    abstract public void initView();

}
