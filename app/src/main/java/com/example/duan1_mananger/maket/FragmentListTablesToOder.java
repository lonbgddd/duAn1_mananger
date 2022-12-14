package com.example.duan1_mananger.maket;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.duan1_mananger.R;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.base.OnclickOptionMenu;
import com.example.duan1_mananger.databinding.DialogAddTypeProductBinding;
import com.example.duan1_mananger.databinding.DialogFunctionProductBinding;
import com.example.duan1_mananger.databinding.DialogFunctionTableBinding;
import com.example.duan1_mananger.databinding.FragmentEmptyTableToOderBinding;
import com.example.duan1_mananger.home.HomeFragment;
import com.example.duan1_mananger.model.Table;
import com.example.duan1_mananger.model.TypeProduct;
import com.example.duan1_mananger.table.DetailTableFragment;
import com.example.duan1_mananger.table.adapter.TableAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FragmentListTablesToOder extends BaseFragment implements OnclickOptionMenu,TableAdapter.OnItemLongClickListener {
    private FragmentEmptyTableToOderBinding binding;
    private TableAdapter adapter = null;
    private ArrayList<Table> listTable;
    private PopupMenu popupMenuTables;

    public FragmentListTablesToOder() {

    }

    public static FragmentListTablesToOder newInstance() {
        FragmentListTablesToOder fragment = new FragmentListTablesToOder();
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
        binding = FragmentEmptyTableToOderBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listening();
    }

    @Override
    public void loadData() {
        listTable = new ArrayList<>();
        getAllTables();

    }

    @Override
    public void listening() {

        binding.searchViewTable.clearFocus();
        binding.searchViewTable.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTables(newText);
                return true;
            }
        });

        binding.swiperRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTables();
                binding.recViewTableEmpty.setAdapter(adapter);
                binding.swiperRefreshLayout.setRefreshing(false);
            }
        });

        binding.layoutFilterTable.setOnClickListener(layout ->{
            popupMenuTables = new PopupMenu(getContext(),binding.layoutFilterTable);
            popupMenuTables.inflate(R.menu.menu_popup_table);
            Menu menu = popupMenuTables.getMenu();
            popupMenuTables.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return menuItemClicked(item);
                }
            });
            popupMenuTables.show();

        });



        binding.fabAddTable.setOnClickListener(btn ->{
            dialogAddTable(getContext());
        });



    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }

    public void dialogAddTable(Context context) {
        final Dialog dialog = new Dialog(context);
        DialogAddTypeProductBinding binding = DialogAddTypeProductBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.tvTitle.setText("Th??m b??n tr???ng m???i");
        binding.edNameType.setHint("Nh???p t??n b??n");

        binding.tvCancel.setOnClickListener(tv ->{
            dialog.dismiss();
        });

        binding.tvAdd.setOnClickListener(add->{
            FirebaseDatabase data = FirebaseDatabase.getInstance();
            DatabaseReference mRef = data.getReference("tables");
            String key = mRef.push().getKey();

            if(TextUtils.isEmpty(binding.edNameType.getText().toString())){
                Toast.makeText(context, "H??y nh???p t??n b??n !"  , Toast.LENGTH_SHORT).show();
            }else {
                Table table = new Table(key,binding.edNameType.getText().toString().trim(),"false",true);
                mRef.child(key).setValue(table).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Th??m b??n th??nh c??ng", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Th??m th???t b???i", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private boolean menuItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tvAll:
                getAllTables();
                binding.tvFilterTable.setText("T???t c??? b??n");
                break;
            case R.id.tvEmpty:
                getFilterTable("false");
                binding.tvFilterTable.setText("B??n ??ang tr???ng");
                break;
            case R.id.tvOpen:
                getFilterTable("true");
                binding.tvFilterTable.setText("B??n ??ang m???");
                break;
            default:
                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }

        return  true;
    }

    private void getFilterTable(String statusTable){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tables");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTable.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Table table = snapshot1.getValue(Table.class);
                    if(table.isHidden() && table.getStatus().equals(statusTable)){
                        listTable.add(table);
                    }
                }
                if(statusTable.equals("true")){
                    binding.tvNumberOfTable.setText("C?? "+listTable.size()+" b??n ??ang m???");
                }else if(statusTable.equals("false")){
                    binding.tvNumberOfTable.setText("C?? "+listTable.size()+" b??n ??ang tr???ng");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new TableAdapter(listTable, FragmentListTablesToOder.this, FragmentListTablesToOder.this,getContext());
        binding.recViewTableEmpty.setAdapter(adapter);
    }

    public void getAllTables(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tables");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTable.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Table table = snapshot1.getValue(Table.class);
                    if(table.isHidden()){
                        listTable.add(table);
                    }
                }
                binding.tvNumberOfTable.setText("C?? t???t c??? "+listTable.size()+" b??n ");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new TableAdapter(listTable, FragmentListTablesToOder.this, FragmentListTablesToOder.this,getContext());
        binding.recViewTableEmpty.setAdapter(adapter);
    }

    private void filterTables(String text){
        ArrayList<Table> filterTables = new ArrayList<>();
        for (Table table: listTable) {
            if(table.getName_table().toLowerCase().contains(text.toLowerCase())){
                filterTables.add(table);
            }
        }
        if(!filterTables.isEmpty()) {
            adapter.setFilterList(filterTables);
            binding.tvNumberOfTable.setText(filterTables.size() + " b??n");
        }
    }


    private void dialogFunctionProduct(Context context, Table table) {
        final Dialog dialogFunctionTable = new Dialog(context);
        DialogFunctionTableBinding bindingDialog = DialogFunctionTableBinding.inflate(LayoutInflater.from(context));
        dialogFunctionTable.setContentView(bindingDialog.getRoot());
        Window window = dialogFunctionTable.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        bindingDialog.dialogChooserFunction.setTranslationY(150);
        bindingDialog.dialogChooserFunction.animate().translationYBy(-150).setDuration(400);

        bindingDialog.tvChangeName.setOnClickListener(tv ->{
            final Dialog dialogChange = new Dialog(context);
            DialogAddTypeProductBinding bindingChange = DialogAddTypeProductBinding.inflate(LayoutInflater.from(context));
            dialogChange.setContentView(bindingChange.getRoot());
            dialogChange.setCancelable(false);
            Window window2 = dialogChange.getWindow();
            window2.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            bindingChange.tvTitle.setText("S???a th??ng tin b??n");
            bindingChange.tvAdd.setText("L??u");
            bindingChange.edNameType.setText(table.getName_table());
            bindingChange.edNameType.setHint("Nh???p t??n b??n");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tables");
            bindingChange.tvAdd.setOnClickListener(tv2 ->{
                if(TextUtils.isEmpty(bindingChange.edNameType.getText().toString())){
                    Toast.makeText(context, "H??y nh???p t??n b??n !"  , Toast.LENGTH_SHORT).show();
                }else {
                    Table table1 = new Table(table.getId_table(), bindingChange.edNameType.getText().toString(), table.getStatus(),true);
                    reference.child(table.getId_table()).setValue(table1).addOnCompleteListener(task->{
                        if (task.isSuccessful()){
                            Toast.makeText(context, "C???p nh???t th??nh c??ng!", Toast.LENGTH_SHORT).show();
                            dialogChange.cancel();
                        }else {
                            Toast.makeText(context, "C???p nh???t kh??ng th??nh c??ng!", Toast.LENGTH_SHORT).show();
                            dialogChange.cancel();
                        }
                    });
                }

            });

            bindingChange.tvCancel.setOnClickListener(tv2 ->{
                dialogChange.dismiss();
            });

            dialogChange.show();
            dialogFunctionTable.cancel();
        });



        bindingDialog.tvDelete.setOnClickListener(tv ->{
            if(table.getStatus().equals("true")){
                Toast.makeText(context, "B??n ??ang ???????c m???.Kh??ng th??? x??a", Toast.LENGTH_SHORT).show();
            }else {
                table.setHidden(false);
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("tables");
                reference2.child(table.getId_table()).setValue(table).addOnCompleteListener(task->{
                    if (task.isSuccessful()){
                        Toast.makeText(context, "???? x??a", Toast.LENGTH_SHORT).show();
                        dialogFunctionTable.cancel();
                    }else {
                        Toast.makeText(context, "X??a kh??ng th??nh c??ng!", Toast.LENGTH_SHORT).show();
                        dialogFunctionTable.cancel();
                    }
                });
                adapter.notifyDataSetChanged();
            }

            dialogFunctionTable.cancel();
        });

        dialogFunctionTable.show();

    }
    @Override
    public void onClick(Table table) {
        replaceFragment(DetailTableFragment.newInstance(table));
    }

    @Override
    public void onLongClickTable(Table table) {
        dialogFunctionProduct(getContext(),table);
    }
}