package com.jay.chatapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jay.chatapp.modal.Users;

import java.util.ArrayList;
import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.MyViewHolder> {
    List<Users> names;
    Context context;

    public UsersListAdapter(List<Users> names, Context context) {
        this.names = names;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_for_users,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersListAdapter.MyViewHolder holder, final int position) {
        holder.textView.setText(names.get(position).getUsername());
        Log.e("username",names.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,UserChatActivity.class);
                intent.putExtra("username",names.get(position).getUsername());
                intent.putExtra("userid",names.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.userName);
        }
    }
}
