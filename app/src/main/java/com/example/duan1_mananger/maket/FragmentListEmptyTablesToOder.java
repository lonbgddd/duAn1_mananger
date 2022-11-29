package com.example.duan1_mananger.maket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.base.OnclickOptionMenu;
import com.example.duan1_mananger.databinding.FragmentEmptyTableToOderBinding;
import com.example.duan1_mananger.databinding.FragmentListEmptyTablesBinding;
import com.example.duan1_mananger.model.Receipt;
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
    private ArrayList<Table> listTable;

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
        loadTablesEmpty();
        adapter = new TableAdapter(listTable, FragmentListEmptyTablesToOder.this,getContext());
        binding.recViewTableEmpty.setAdapter(adapter);
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
                loadTablesEmpty();
                binding.recViewTableEmpty.setAdapter(adapter);
                binding.swiperRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }

    public void loadTablesEmpty(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tables");
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
            binding.tvNumberOfTable.setText(filterTables.size() + " bàn");
        }
    }

    @Override
    public void onClick(Table table) {
        replaceFragment(DetailTableFragment.newInstance(table));
    }
}