package com.example.auctionapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.model.productModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllBidProduct extends RecyclerView.Adapter<ShowAllBidProduct.viewholder> {

    private Context mcontext;
    private List<productModel> mList;
    private String username;

    public ShowAllBidProduct(Context mcontext, List<productModel> mList, String username) {
        this.mcontext = mcontext;
        this.mList = mList;
        this.username = username;
    }

    @NonNull
    @Override
    public ShowAllBidProduct.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_show_all_auction_products, parent, false);
        ShowAllBidProduct.viewholder holder = new ShowAllBidProduct.viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllBidProduct.viewholder holder, int position) {
        productModel model = mList.get(position);
        Glide.with(mcontext).load(model.getFoodImage()).into(holder.cv_foodImage);
        holder.tv_foodName.setText(model.getFoodName());
        holder.tv_foodDesc.setText(model.getFoodDesc());
        holder.tv_foodPrice.setText("Price : $" + model.getFoodPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ShowAllBidders.class);
                intent.putExtra("foodImage", model.getFoodImage());
                intent.putExtra("foodName", model.getFoodName());
                intent.putExtra("foodDesc", model.getFoodDesc());
                intent.putExtra("foodPrice", model.getFoodPrice());
                intent.putExtra("phoneNumber", model.getPhoneNumber());
                intent.putExtra("username", model.getUsername());
                intent.putExtra("id", model.getId());
                intent.putExtra("ownName", username);
                mcontext.startActivity(intent);
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
        private TextView tv_foodDesc;
        private TextView tv_foodPrice;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            cv_foodImage = itemView.findViewById(R.id.cv_foodImage);
            tv_foodName = itemView.findViewById(R.id.tv_foodName);
            tv_foodDesc = itemView.findViewById(R.id.tv_foodDesc);
            tv_foodPrice = itemView.findViewById(R.id.tv_foodPrice);

        }
    }
}
