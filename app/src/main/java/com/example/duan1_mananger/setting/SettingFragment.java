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

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends BaseFragment {

    private FragmentSettingBinding binding = null;
    User user;
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
        binding = FragmentSettingBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        listening();
        initObSever();
        loadData();
    }

    @Override
    public void loadData() {
        user= new User();
        firebaseUser = firebaseAuth.getCurrentUser();

        String userID = firebaseUser.getUid();
        Log.d("zzzz", "onViewCreated: " + userID);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if(firebaseUser!= null){
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
        binding.icEditUser.setOnClickListener(ic ->{
            replaceFragment(new UpdateUserFragment().newInstance(user));
        });

        binding.btnLogout.setOnClickListener(btn ->{
            signOut(getContext());
        });
    }

    @Override
    public void initObSever() {
    }

    @Override
    public void initView() {
    }


    private void dialogChangeProfile(Context context){
        final Dialog dialog = new Dialog(context,android.R.style.Theme_Material_Light_NoActionBar);
        dialog.setContentView(R.layout.layout_change_profile);
        Window window = dialog.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(context.getColor(R.color.white));
        dialog.setCancelable(false);
        dialog.findViewById(R.id.icBack).setOnClickListener(ic ->{
            dialog.dismiss();
        });
        ImageView icSave = dialog.findViewById(R.id.icSave);
        CircleImageView imgAvatar = dialog.findViewById(R.id.imgAvatar);
        ImageButton btnCamera = dialog.findViewById(R.id.ic_add_img);

        TextInputEditText edName = dialog.findViewById(R.id.edName);
        TextInputEditText edPhone = dialog.findViewById(R.id.edPhone);
        TextInputEditText edEmail = dialog.findViewById(R.id.edEmail);
        TextInputEditText edAddress = dialog.findViewById(R.id.edAddress);
        TextInputEditText edBirth = dialog.findViewById(R.id.edBirth);
        icSave.setColorFilter(R.color.red);
        changeColorIcCheck(edName, icSave);
        changeColorIcCheck(edPhone,icSave);
        changeColorIcCheck(edEmail, icSave);
        changeColorIcCheck(edAddress, icSave);
        changeColorIcCheck(edBirth, icSave);
        //Lưu ý: ngày tháng năm sinh phải dùng DatePickerDialog dể chọn, k được nhập tay
        dialog.show();

    }



    //cái này chưa dùng đến, nghiên cứu sau

    private void changeColorIcCheck(EditText editText, ImageView icCheck){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.length() != 0){
                    icCheck.setColorFilter(R.color.blue);
                }else {
                    icCheck.setColorFilter(R.color.grey_350);
                }
            }
        });
    }

    private void signOut(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Đăng xuất tài khoản ");
        builder.setIcon(context.getDrawable(R.drawable.ic_logout));
        builder.setMessage("Bạn chắc chắn muốn đăng xuất!");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SignInActivity.class));
                Toast.makeText(context, "Đã đăng xuất! ", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context,"Đã hủy !",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        AlertDialog sh = builder.create();
        sh.show();
    }
}