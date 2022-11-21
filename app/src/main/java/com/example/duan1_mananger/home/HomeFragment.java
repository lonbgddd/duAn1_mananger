package com.example.duan1_mananger.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentHomeBinding;
import com.example.duan1_mananger.model.User;
import com.example.duan1_mananger.table.FragmentListAllTables;
import com.example.duan1_mananger.home.fragments.FragmentListEmptyTables;
import com.example.duan1_mananger.home.fragments.FragmentListOpenTables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends BaseFragment {
   private FragmentHomeBinding binding;
   private User user;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("avatars");
        reference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference files: listResult.getItems()
            ) {
                if (files.getName().equals(user.getUid())){
                    files.getDownloadUrl().addOnSuccessListener(uri -> {
                        Log.d("TAG", "initView: "+uri);
                        Glide.with(getContext()).load(uri).into(binding.icUserSetting);
                    });
                }
            }
        }).addOnFailureListener(e -> {

        });
        listening();
        initObSever();
    }

    @Override
    public void loadData() {
        user = new User();
        firebaseUser = firebaseAuth.getCurrentUser();
        String userID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (firebaseUser != null) {
                    binding.tvName.setText(user.getName_user());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void listening() {
        binding.icCloseSlide.setOnClickListener(ic ->{
            binding.layoutSlide.setVisibility(View.GONE);

        });
        selectTabFragment();

    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {
        binding.tvTitleAll.setBackgroundColor(getContext().getColor(R.color.red_100));
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListAllTables()).commit();
    }

    private void selectTabFragment(){
        binding.btnAllTable.setOnClickListener(btn ->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListAllTables()).commit();
            changeBgColorTextView(binding.tvTitleAll,getContext().getColor(R.color.red_100));
            changeBgColorTextView(binding.tvTitleEmpty,getContext().getColor(R.color.grey_55));
            changeBgColorTextView(binding.tvTitleOpen,getContext().getColor(R.color.grey_55));

        });
        binding.btnTableEmpty.setOnClickListener(btn ->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListEmptyTables()).commit();
            changeBgColorTextView(binding.tvTitleAll,getContext().getColor(R.color.grey_55));
            changeBgColorTextView(binding.tvTitleEmpty,getContext().getColor(R.color.red_100));
            changeBgColorTextView(binding.tvTitleOpen,getContext().getColor(R.color.grey_55));

        });

        binding.btnTableOpen.setOnClickListener(btn ->{
            changeBgColorTextView(binding.tvTitleAll,getContext().getColor(R.color.grey_55));
            changeBgColorTextView(binding.tvTitleEmpty,getContext().getColor(R.color.grey_55));
            changeBgColorTextView(binding.tvTitleOpen,getContext().getColor(R.color.red_100));
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListOpenTables()).commit();
        });
    }

    private void changeBgColorTextView( TextView tv ,int idColor){
        tv.setBackgroundColor(idColor);
    }





}