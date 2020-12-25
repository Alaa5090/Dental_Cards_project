package com.example.dentalcart.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentalcart.Pojo.CartModel;
import com.example.dentalcart.R;
import com.example.dentalcart.Repositories.FirebaseOperations;
import com.example.dentalcart.Repositories.GeneralOperations;
import com.example.dentalcart.UI.CartActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder>{
    private List<CartModel> list ;
    private Context context ;
    public CartAdapter(List<CartModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_items , parent , false) ;
        MyViewHolder myViewHolder = new MyViewHolder(view) ;
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartModel itemModel = list.get(position) ;
        holder.cartNameTv.setText(itemModel.getName());
        holder.cartPriceTv.setText(itemModel.getPrice());
        Picasso.get()
                .load(itemModel.getImage())
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(holder.cartImage);
        holder.cartCounterTV.setText(String.valueOf(itemModel.getQuantity()));

        // this method is used to increase price
        holder.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Integer.parseInt(holder.cartCounterTV.getText().toString());
                int newResult = result+1;
//                HashMap<String , Object> hashMap = new HashMap<>() ;
//                hashMap.put("id" , itemModel.getID()) ;
//                hashMap.put("price" , itemModel.getPrice()) ;
//                hashMap.put("name" , itemModel.getName()) ;
//                hashMap.put("image" , itemModel.getImage()) ;
//                hashMap.put("quantity" , newResult) ;
                Log.i("newResult" , newResult + "") ;
                CartModel cartModel = new CartModel(itemModel.getID(), itemModel.getPrice(),itemModel.getName(),itemModel.getImage(),newResult,itemModel.getQuanCount());
                FirebaseOperations.increaseCart(itemModel.getID() , cartModel , context);
            }
        });

        // this method is used to decrease
        holder.minusIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Integer.parseInt(holder.cartCounterTV.getText().toString());
                int price = Integer.parseInt(itemModel.getPrice()) ;
                int covResult = result - 1;
                //int calcResult = covResult * price ;
//                HashMap<String , Object> hashMap = new HashMap<>() ;
//                hashMap.put("id" , itemModel.getID()) ;
//                hashMap.put("price" , itemModel.getPrice()) ;
//                hashMap.put("name" , itemModel.getName()) ;
//                hashMap.put("image" , itemModel.getImage()) ;
//                hashMap.put("quantity" , covResult) ;
                CartModel cartModel = new CartModel(itemModel.getID(), itemModel.getPrice(),itemModel.getName(),itemModel.getImage(),covResult,itemModel.getQuanCount());
                FirebaseOperations.decreaseCart(itemModel.getID() , cartModel , result , context);
            }
        });

        //this method is used to delete item from cart and update quantity of item
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int realQuantity = itemModel.getQuanCount() + itemModel.getQuantity() ;
                GeneralOperations.deleteProductFromCart(itemModel.getID() , realQuantity , context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView cartImage , addImage , minusIamge , deleteItem ;
        private TextView cartNameTv , cartPriceTv , cartCounterTV ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cartImeo_id);
            addImage = itemView.findViewById(R.id.addNumber_id);
            minusIamge = itemView.findViewById(R.id.minusNumber_id);
            cartNameTv = itemView.findViewById(R.id.cartName_id);
            cartPriceTv = itemView.findViewById(R.id.cartprice_id);
            cartCounterTV = itemView.findViewById(R.id.counter_id) ;
            deleteItem = itemView.findViewById(R.id.delete_item_id) ;
        }
    }
}
