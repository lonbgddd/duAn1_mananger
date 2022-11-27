package com.example.duan1_mananger.table;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.duan1_mananger.model.Product;
import com.example.duan1_mananger.model.Receipt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TableViewModel extends ViewModel {
    public MutableLiveData<List<Product>> listProductOder = new MutableLiveData<>();
    public MutableLiveData<String> oderTableStatus = new MutableLiveData<>();
    public MutableLiveData<Receipt> liveDataGetReceipt = new MutableLiveData<>();
    private DatabaseReference reference;

    public LiveData<List<Product>> listLiveData(List<String> listIdProduct) {
        ArrayList<Product> list = new ArrayList<>();
        for (String idProduct : listIdProduct) {
            reference = FirebaseDatabase.getInstance().getReference("list_product");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()
                    ) {
                        Product product1 = dataSnapshot.getValue(Product.class);
                        if (product1.getId().equals(idProduct)) {
                            Log.d("TAG", "onDataChange: "+idProduct);
                            list.add(product1);
                        }
                    }
                    listProductOder.postValue(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        };

        return listProductOder;
    }

    public LiveData<String> setStatusTable(String idTable){
         reference = FirebaseDatabase.getInstance().getReference("tables");
         reference.child(idTable).child("status").setValue("true");
         oderTableStatus.postValue("true");
        return oderTableStatus;
    }
    public LiveData<Receipt> liveDataGetReceipt(String idTable){
        reference = FirebaseDatabase.getInstance().getReference("OderSave");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()
                     ) {
                     Receipt receipt1 =dataSnapshot.getValue(Receipt.class);
                     if (receipt1.getIdTable().equals(idTable)){
                         liveDataGetReceipt.postValue(receipt1);
                     }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return liveDataGetReceipt;
    }

}
