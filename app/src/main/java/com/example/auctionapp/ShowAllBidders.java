package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.adapter.ShowAllBiddersAdapter;
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

public class ShowAllBidders extends AppCompatActivity {

    RecyclerView rv_showAllFood;
    List<productModel> mList = new ArrayList<>();
    ShowAllBiddersAdapter mAdapter;

    String foodImage = "";
    String foodName = "";
    String foodDesc = "";
    String foodPrice = "";
    String phoneNumber = "";
    String username = "";
    String id = "";
    String ownName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_bidders);

        rv_showAllFood = findViewById(R.id.rv_showAllOrders);
        rv_showAllFood.setHasFixedSize(true);
        rv_showAllFood.setLayoutManager(new LinearLayoutManager(ShowAllBidders.this));

        Intent intent = getIntent();
        foodName = intent.getStringExtra("foodName");
        foodImage = intent.getStringExtra("foodImage");
        foodDesc = intent.getStringExtra("foodDesc");
        foodPrice = intent.getStringExtra("foodPrice");
        phoneNumber = intent.getStringExtra("phoneNumber");
        username = intent.getStringExtra("username");
        id = intent.getStringExtra("id");
        ownName = intent.getStringExtra("ownName");

        getAllOrders();

    }

    private void getAllOrders() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.BID).child(username).child(foodName);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        productModel restaurantFood = dataSnapshot.getValue(productModel.class);

                        mList.add(restaurantFood);
                    }
                    mAdapter = new ShowAllBiddersAdapter(ShowAllBidders.this, mList, username);
                    rv_showAllFood.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}