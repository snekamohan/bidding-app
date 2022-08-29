package com.example.auctionapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.model.productModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder> {
    private Context mcontext;
    private List<productModel> mList;

    public ProductAdapter(Context mcontext, List<productModel> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.product_layout, parent, false);
        viewholder holder = new viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        productModel model = mList.get(position);
        Glide.with(mcontext).load(model.getFoodImage()).into(holder.iv_foodImage);
        holder.tv_foodDesc.setText(model.getFoodDesc());
        holder.tv_foodPrice.setText("Price : $" + model.getFoodPrice());
        holder.tv_foodName.setText(model.getFoodName());
        holder.tv_ownerPhone.setText("Phn: " + model.getPhoneNumber());
        holder.tv_ownerName.setText(model.getUsername());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        private CircleImageView iv_foodImage;
        private TextView tv_ownerName;
        private TextView tv_ownerPhone;
        private TextView tv_foodName;
        private TextView tv_foodPrice;
        private TextView tv_foodDesc;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            iv_foodImage = itemView.findViewById(R.id.iv_foodImage);
            tv_ownerName = itemView.findViewById(R.id.tv_ownerName);
            tv_ownerPhone = itemView.findViewById(R.id.tv_ownerPhone);
            tv_foodName = itemView.findViewById(R.id.tv_foodName);
            tv_foodPrice = itemView.findViewById(R.id.tv_foodPrice);
            tv_foodDesc = itemView.findViewById(R.id.tv_foodDesc);

        }
    }
}
