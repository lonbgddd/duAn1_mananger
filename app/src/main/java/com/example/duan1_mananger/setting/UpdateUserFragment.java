package com.example.duan1_mananger.setting;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duan1_mananger.MainActivity;
import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.LayoutChangeProfileBinding;
import com.example.duan1_mananger.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

public class UpdateUserFragment extends BaseFragment {
    private LayoutChangeProfileBinding binding = null;
    private User userData = null;
    private UpdateUserViewModel mViewModel;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Calendar calendar;
    ImageView imgAva;


    public UpdateUserFragment newInstance(User user) {
        this.userData = user;
        return new UpdateUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LayoutChangeProfileBinding.inflate(inflater,container,false);
//        Log.d("TAG", "onCreateView: "+ userData.getId());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        listening();
        loadData();
        initObSever();


    }

    @Override
    public void loadData() {
        showUserProfile();
    }

    @Override
    public void listening() {
        binding.icBack.setOnClickListener(v -> {
            replaceFragment(new SettingFragment().newInstance());
        });

        binding.icChooserBirth.setOnClickListener(v -> {
            dialogChooseBirth();
        });

        binding.icAddImg.setOnClickListener(v -> {
            requestPermission();
        });

        binding.icSave.setOnClickListener(v -> {
            dialogConfirmUpdate(getContext());
        });
    }


    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }

    // oke
    private void showUserProfile(){
        firebaseUser = firebaseAuth.getCurrentUser();
        String userID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData = snapshot.getValue(User.class);
//                Log.d("TAG", "onDataChange1: "+userData.getId());
                if(firebaseUser != null) {
//                    Log.d("TAG", "showUserProfile: " + firebaseUser);
                    binding.edName.setText(userData.getName_user());
                    binding.edPhone.setText(userData.getPhone_number());
                    binding.edEmail.setText(firebaseUser.getEmail());
                    binding.edAddress.setText(userData.getAddress());
                    binding.edBirth.setText(userData.getBirthday());
                    Glide.with(getActivity()).load(userData.getAvatar()).error(R.drawable.img_avatar).into(binding.imgAvatar);
                    if(userData.getSex()){
                        binding.rdoMale.setSelected(true);
                    }else {
                        binding.rdoFemale.setSelected(true);
                    }

                }else {
                    Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something Wrong!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // chọn ngày từ DatePickerDiaLog / oke
    private void dialogChooseBirth(){
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String ngay = "";
                        String thang = "";
                        if(dayOfMonth < 10){
                            ngay = "0" + dayOfMonth;
                        }else{
                            ngay = String.valueOf(dayOfMonth);
                        }
                        if((month + 1) < 10){
                            thang = "0" + (month + 1);
                        }else{
                            thang = String.valueOf(month + 1);
                        }
                        binding.edBirth.setText(ngay + "/" + thang + "/" + year);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateUserProfile(){
        firebaseUser = firebaseAuth.getCurrentUser();
        String userID = firebaseUser.getUid();
        boolean gender;
            if(binding.rdoMale.isSelected()){
                gender = true;
            } else {
                gender = false;
            }

        userData = new User(binding.edName.getText().toString().trim()
                , gender, binding.edPhone.getText().toString().trim(), binding.edBirth.getText().toString().trim()
                , binding.edAddress.getText().toString().trim(), binding.edEmail.getText().toString().trim());
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(userID).updateChildren(userData.toMap(),
                    new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (firebaseUser != null) {
                        Log.d("TAG", "onCompleteUPDATE USER: "+gender);
                        Toast.makeText(getContext(), "Sửa thông tin thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void updateEmail(String stremail){
        firebaseUser = firebaseAuth.getCurrentUser();
//        Log.d("TAG", "updateEmail: "+ userData.getEmail());
        firebaseUser.updateEmail(stremail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Đã cập nhật email", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Hãy thử đăng xuất và thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void dialogConfirmUpdate(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cập nhật thông tin cá nhân");
        builder.setIcon(context.getDrawable(R.drawable.icon_save));
        builder.setMessage("Bạn chắc chắn muốn thay đổi thông tin cá nhân");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(firebaseUser.getEmail() != binding.edEmail.getText().toString()){
                    updateEmail(binding.edEmail.getText().toString().trim());
                    updateUserProfile();
                }else {
                    updateUserProfile();
                }

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context,"Đã hủy !",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        AlertDialog sh = builder.create();
        sh.show();
    }

    private void requestPermission(){
        MainActivity mainActivity = (MainActivity) getActivity();

        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            mainActivity.openGallery();
        }
        String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
        getActivity().requestPermissions(permission, 100);
    }
//&&getActivity().checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED

    //Lỗi ở đây
    public void setBitMap(Bitmap bitmap){
        binding.imgAvatar.setImageBitmap(bitmap);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateUserViewModel.class);
        // TODO: Use the ViewModel
    }

}