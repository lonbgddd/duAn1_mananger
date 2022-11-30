package com.example.duan1_mananger.setting;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;

import com.example.duan1_mananger.model.Receipt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.local.QueryEngine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingViewModel extends ViewModel {
    public MutableLiveData<List<Receipt>> liveDateGetReceipt = new MutableLiveData<>();
    private DatabaseReference reference;

    public LiveData<List<Receipt>> getReceiptByDate(String startDate, String endDate) {
        reference = FirebaseDatabase.getInstance().getReference("PayReceipt");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Receipt> listData = new ArrayList<>();
                listData.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()
                ) {
                    Receipt receipt = dataSnapshot.getValue(Receipt.class);
                    String day = receipt.getTimeOder().substring(0, receipt.getTimeOder().lastIndexOf(" "));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Log.d("TAG", "onDataChange: "+day);
                        Date dayOder = sdf.parse(day);
                        Date start = sdf.parse(startDate);
                        Date end = sdf.parse(endDate);
                        if (start.before(dayOder) && end.after(dayOder)) {
                            listData.add(receipt);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                liveDateGetReceipt.postValue(listData);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return liveDateGetReceipt;
    }
}
