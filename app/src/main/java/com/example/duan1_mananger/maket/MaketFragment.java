package com.example.duan1_mananger.maket;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.DialogEvaluateBinding;
import com.example.duan1_mananger.databinding.FragmentMaketBinding;

public class MaketFragment extends BaseFragment {
    private FragmentMaketBinding binding = null;
    public MaketFragment() {
        // Required empty public constructor
    }

    public static MaketFragment newInstance() {
        MaketFragment fragment = new MaketFragment();
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
        binding = FragmentMaketBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listening();
        initObSever();
    }

    @Override
    public void loadData() {

    }

    @Override
    public void listening() {
        binding.icCloseLayoutStar.setOnClickListener(ic ->{
            binding.layoutStart.setVisibility(View.GONE);
        });

        binding.layoutEvaluate.setOnClickListener(layout ->{
            dialogEvaluate(getContext());
        });

    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {


    }

    public void dialogEvaluate(Context context){
        final Dialog dialog = new Dialog(context);
        DialogEvaluateBinding binding = DialogEvaluateBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(ratingBar.getRating() == 1){
                    binding.tvEvaluate.setText("Kém");
                }else if(ratingBar.getRating() == 2){
                    binding.tvEvaluate.setText("Trung bình");
                } else if(ratingBar.getRating() == 3){
                    binding.tvEvaluate.setText("Khá");
                } else if(ratingBar.getRating() == 4){
                    binding.tvEvaluate.setText("Tốt");
                } else if(ratingBar.getRating() == 5){
                    binding.tvEvaluate.setText("Rất tốt");
                }
            }
        });

        binding.btnCancel.setOnClickListener(button ->{
            dialog.dismiss();
        });
        binding.btnSend.setOnClickListener(button ->{


        });

        dialog.show();
    }



}