package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.adapter.WinnerListAdapter;
import com.example.auctionapp.model.productModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WinnerList extends AppCompatActivity {

    RecyclerView rv_showAllFood;
    String username = "";
    List<productModel> mList = new ArrayList<>();
    WinnerListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_list);

        rv_showAllFood = findViewById(R.id.rv_showAllOrders);
        rv_showAllFood.setHasFixedSize(true);
        rv_showAllFood.setLayoutManager(new LinearLayoutManager(WinnerList.this));

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        getAllOrders();

    }

    private void getAllOrders() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.WINNERS);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        productModel restaurantFood = dataSnapshot.getValue(productModel.class);
                        mList.add(restaurantFood);
                    }
                    mAdapter = new WinnerListAdapter(WinnerList.this, mList, username);
                    rv_showAllFood.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}