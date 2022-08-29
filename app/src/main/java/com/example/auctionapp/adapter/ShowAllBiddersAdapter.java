package com.example.auctionapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.UserRegister;
import com.example.auctionapp.model.productModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllBiddersAdapter extends RecyclerView.Adapter<ShowAllBiddersAdapter.viewholder> {

    private Context mcontext;
    private List<productModel> mList;
    private String username;

    public ShowAllBiddersAdapter(Context mcontext, List<productModel> mList, String username) {
        this.mcontext = mcontext;
        this.mList = mList;
        this.username = username;
    }

    @NonNull
    @Override
    public ShowAllBiddersAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_show_all_bidders, parent, false);
        ShowAllBiddersAdapter.viewholder holder = new ShowAllBiddersAdapter.viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllBiddersAdapter.viewholder holder, int position) {
        productModel model = mList.get(position);
        Glide.with(mcontext).load(model.getFoodImage()).into(holder.cv_foodImage);
        holder.tv_foodName.setText(model.getFoodName());
        holder.tv_bidderName.setText(model.getBidder());
        holder.tv_foodPrice.setText("Bid : $" + model.getBid());

        ProgressDialog progressDialog = new ProgressDialog(mcontext);
        holder.btn_makeWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Adding Your Food");
                progressDialog.setTitle("Adding...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = firebaseUser.getUid();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.WINNERS).child(model.getFoodName());
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("bidder", model.getBidder());
                hashMap.put("foodName", model.getFoodName());
                hashMap.put("foodDesc", model.getFoodDesc());
                hashMap.put("bid", model.getBid());
                hashMap.put("foodImage", model.getFoodImage());
                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(mcontext, "Winner Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mcontext, "Winner Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        private CircleImageView cv_foodImage;
        private TextView tv_foodName;
        private TextView tv_bidderName;
        private TextView tv_foodPrice;
        private Button btn_makeWinner;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            cv_foodImage = itemView.findViewById(R.id.cv_foodImage);
            tv_foodName = itemView.findViewById(R.id.tv_foodName);
            tv_bidderName = itemView.findViewById(R.id.tv_bidderName);
            tv_foodPrice = itemView.findViewById(R.id.tv_foodPrice);
            btn_makeWinner = itemView.findViewById(R.id.btn_makeWinner);

        }
    }

}
