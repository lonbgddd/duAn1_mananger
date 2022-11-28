package com.example.duan1_mananger.maket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.base.OnclickOptionMenu;
import com.example.duan1_mananger.databinding.FragmentEmptyTableToOderBinding;
import com.example.duan1_mananger.databinding.FragmentListEmptyTablesBinding;
import com.example.duan1_mananger.model.Table;
import com.example.duan1_mananger.table.DetailTableFragment;
import com.example.duan1_mananger.table.adapter.TableAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentListEmptyTablesToOder extends BaseFragment implements OnclickOptionMenu {
    private FragmentEmptyTableToOderBinding binding;
    private TableAdapter adapter = null;
    FirebaseDatabase database;
    private RecyclerView recyclerView;
    private List<Table> listTable;


    public FragmentListEmptyTablesToOder() {

    }

    public static FragmentListEmptyTablesToOder newInstance() {
        FragmentListEmptyTablesToOder fragment = new FragmentListEmptyTablesToOder();
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
        listTable = new ArrayList<>();
        recyclerView = binding.recViewTableEmpty;
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void loadData() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("tables");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTable.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Table table = snapshot1.getValue(Table.class);
                    if(table.getStatus().equals("false")){
                        listTable.add(table);
                    }
                }
                binding.tvNumberOfTable.setText("Có tất cả "+listTable.size()+" bàn trống");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new TableAdapter(listTable, FragmentListEmptyTablesToOder.this,getContext());
        recyclerView.setAdapter(adapter);

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
    @Override
    public void onClick(Table table) {
        replaceFragment(DetailTableFragment.newInstance(table));
    }
}