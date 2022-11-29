package com.example.duan1_mananger.Oder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.example.duan1_mananger.Oder.Adapter.ListOderAdapter;
import com.example.duan1_mananger.base.BaseFragment;
import com.example.duan1_mananger.databinding.FragmentListOderBinding;
import com.example.duan1_mananger.model.Receipt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOderFragment extends BaseFragment {
    private FragmentListOderBinding binding = null;
    private ArrayList<Receipt> listReceipt;
    private Receipt receipt;
    private ListOderAdapter adapter ;
    public ListOderFragment(){

    }
    public static ListOderFragment newInstance() {
        ListOderFragment fragment = new ListOderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentListOderBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        listening();
    }

    @Override
    public void loadData() {
        listReceipt = new ArrayList<>();
        getReceipt();
        adapter= new ListOderAdapter(listReceipt);
        binding.recListBill.setAdapter(adapter);

    }

    @Override
    public void listening() {
        binding.icBack.setOnClickListener(ic->{
            backStack();
        });
        binding.searchViewTable.clearFocus();
        binding.searchViewTable.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterReceipt(newText);
                return true;
            }
        });

    }

    @Override
    public void initObSever() {

    }

    @Override
    public void initView() {

    }

    private void getReceipt(){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("PayReceipt");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listReceipt.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Receipt receipt = dataSnapshot.getValue(Receipt.class);
                    listReceipt.add(receipt);
                }
                binding.tvNumberOfOder.setText(listReceipt.size()+" đơn");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void filterReceipt(String text){
        ArrayList<Receipt> filterReceipt = new ArrayList<>();
        for (Receipt receipt: listReceipt) {
            if(receipt.getTimeOder().toLowerCase().contains(text.toLowerCase())
                    ||receipt.getMoney().toString().toLowerCase().contains(text.toLowerCase())){
                filterReceipt.add(receipt);
            }

        }
        if(filterReceipt.isEmpty()){

        }else {
            adapter.setFilterList(filterReceipt);
            binding.tvNumberOfOder.setText(filterReceipt.size()+" đơn");
        }

    }
}
