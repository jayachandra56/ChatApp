package com.jay.chatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jay.chatapp.R;
import com.jay.chatapp.UsersListAdapter;
import com.jay.chatapp.modal.messages;

import java.util.ArrayList;

public class ConvoAdapter extends RecyclerView.Adapter<ConvoAdapter.MyViewHolder>{
    ArrayList<messages> conversation=new ArrayList<>();
    Context context;
    FirebaseUser fuser;
    int MSG_TYPE_RIGHT=0;
    int MSG_TYPE_LEFT=1;

    public ConvoAdapter(ArrayList<messages> conversation, Context context) {
        this.conversation = conversation;
        this.context = context;
    }

    @NonNull
    @Override
    public ConvoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
            ConvoAdapter.MyViewHolder holder=new ConvoAdapter.MyViewHolder(view);
            return holder;
        }else{
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
            ConvoAdapter.MyViewHolder holder=new ConvoAdapter.MyViewHolder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ConvoAdapter.MyViewHolder holder, int position) {
        messages msg=conversation.get(position);
        holder.message.setText(msg.getMessage());

    }

    @Override
    public int getItemCount() {
        return conversation.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.msg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(conversation.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;

        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
