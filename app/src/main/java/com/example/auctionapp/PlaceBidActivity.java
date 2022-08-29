package com.example.auctionapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaceBidActivity extends AppCompatActivity {

    String foodImage = "";
    String foodName = "";
    String foodDesc = "";
    String foodPrice = "";
    String phoneNumber = "";
    String username = "";
    String id = "";
    String ownName = "";

    CircleImageView cv_foodImage;
    TextView tv_foodName;
    TextView tv_foodDesc;
    TextView tv_foodPrice;
    TextView tv_phoneNumber;
    TextView tv_ownerName;

    String ownerName = "";

    EditText et_enterBid;

    Button btn_call;
    Button btn_sendMsg;
    Button btn_placeBid;

    Intent intent;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_bid);

        intent = getIntent();
        foodName = intent.getStringExtra("foodName");
        foodImage = intent.getStringExtra("foodImage");
        foodDesc = intent.getStringExtra("foodDesc");
        foodPrice = intent.getStringExtra("foodPrice");
        phoneNumber = intent.getStringExtra("phoneNumber");
        username = intent.getStringExtra("username");
        id = intent.getStringExtra("id");
        ownName = intent.getStringExtra("ownName");

        cv_foodImage = findViewById(R.id.iv_foodImage);
        tv_foodName = findViewById(R.id.tv_foodName);
        tv_foodPrice = findViewById(R.id.tv_foodPrice);
        tv_foodDesc = findViewById(R.id.tv_foodDesc);
        tv_ownerName = findViewById(R.id.tv_ownerName);
        tv_phoneNumber = findViewById(R.id.tv_ownerPhone);
        et_enterBid = findViewById(R.id.et_enterBid);

        setTextViews();

        progressDialog = new ProgressDialog(PlaceBidActivity.this);

        btn_call = findViewById(R.id.btn_call);
        btn_sendMsg = findViewById(R.id.btn_sendMsg);
        btn_placeBid = findViewById(R.id.btn_placeBid);

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });

        btn_sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null)));
            }
        });

        btn_placeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bid = et_enterBid.getText().toString();
                if (bid.isEmpty()) {
                    et_enterBid.setError("Please enter bid");
                } else {
                    progressDialog.setMessage("Adding Your Food");
                    progressDialog.setTitle("Adding...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    placeBid(bid);
                }
            }
        });

    }

    private void setTextViews() {
        Glide.with(PlaceBidActivity.this).load(foodImage).into(cv_foodImage);
        tv_foodName.setText(foodName);
        tv_foodDesc.setText(foodDesc);
        tv_foodPrice.setText("Price : " + foodPrice);
        tv_ownerName.setText(username);
        tv_phoneNumber.setText("Phn: " + phoneNumber);
    }

    private void placeBid(String bid) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.BID).child(username).child(foodName);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("foodName", foodName);
        hashMap.put("foodDesc", foodDesc);
        hashMap.put("foodPrice", foodPrice);
        hashMap.put("foodImage", foodImage);
        hashMap.put("phoneNumber", phoneNumber);
        hashMap.put("username", username);
        hashMap.put("bid", bid);
        hashMap.put("id", userId);
        hashMap.put("bidder", ownName);
        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(PlaceBidActivity.this, "Bid Placed Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlaceBidActivity.this, "Bid Placed Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}