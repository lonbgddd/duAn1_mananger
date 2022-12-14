package com.example.duan1_mananger.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentSettingBinding;
import com.example.duan1_mananger.model.User;
import com.example.duan1_mananger.ui.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends BaseFragment {

    private FragmentSettingBinding binding = null;
   private User user;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private static final int PICK_PDF_FILE = 2;

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
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Window window = getActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(getActivity().getColor(R.color.brown_120));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("avatars");
        reference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference files : listResult.getItems()
            ) {
                if (files.getName().equals(firebaseUser.getUid())) {
                    files.getDownloadUrl().addOnSuccessListener(uri -> {
                        if(getActivity() != null){
                            Glide.with(getActivity()).load(uri).into(binding.imgAvatar);
                        }

                    });
                }

            }
        }).addOnFailureListener(e -> {

        });
        listening();
        initObSever();
        loadData();
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
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("avatars");
        reference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference files : listResult.getItems()
            ) {
                if (files.getName().equals(userID)) {
                    files.getDownloadUrl().addOnSuccessListener(uri -> {
                        if(getActivity() != null){
                            Glide.with(getActivity()).load(uri).into(binding.imgAvatar);
                        }

                    });
                }

            }
        }).addOnFailureListener(e -> {

        });

    }

    @Override
    public void listening() {
        binding.icEditUser.setOnClickListener(ic -> {
            replaceFragment(new UpdateUserFragment().newInstance(user));
        });
        binding.btnLogout.setOnClickListener(btn -> {
            signOut(getContext());
        });

        binding.tvSalesReport.setOnClickListener(v -> {
            replaceFragment(DailySalesReportFragment.newInstance());
        });

        binding.icSalesReport.setOnClickListener(v -> {
            replaceFragment(DailySalesReportFragment.newInstance());
        });
        binding.icNextSalesReport.setOnClickListener(v -> {
            replaceFragment(DailySalesReportFragment.newInstance());
        });


        binding.tvOrderStatistics.setOnClickListener(v -> {
            replaceFragment(StatisticalFragment.newInstance());
        });

        binding.icOrderStatistics.setOnClickListener(v -> {
            replaceFragment(StatisticalFragment.newInstance());
        });
        binding.icNextOrderStatistics.setOnClickListener(v -> {
            replaceFragment(StatisticalFragment.newInstance());
        });



    }

    @Override
    public void initObSever() {
    }

    @Override
    public void initView() {


    }



    private void signOut(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("????ng xu???t t??i kho???n ");
        builder.setIcon(context.getDrawable(R.drawable.ic_logout));
        builder.setMessage("B???n ch???c ch???n mu???n ????ng xu???t!");
        builder.setCancelable(false);
        builder.setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SignInActivity.class));
                Toast.makeText(context, "???? ????ng xu???t! ", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Tho??t", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "???? h???y !", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        AlertDialog sh = builder.create();
        sh.show();
    }
}