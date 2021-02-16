package com.jay.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jay.chatapp.Notifications.Token;
import com.jay.chatapp.modal.Users;
import com.jay.chatapp.modal.messages;

import java.util.ArrayList;
import java.util.List;

public class chatFragment extends Fragment {
    RecyclerView recyclerView;
    List<Users> mUsers;
    ArrayList<String> MyUsersList =new ArrayList<>();
    FirebaseUser currentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        // Inflate the layout for this fragment
        recyclerView=view.findViewById(R.id.user_listChat);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    messages chatUsers=snapshot.getValue(messages.class);
                    assert chatUsers != null;
                    assert currentUser != null;

                    if(currentUser.getUid().equals(chatUsers.getReceiver())){
                        if(!MyUsersList.contains(chatUsers.getSender())){
                            MyUsersList.add(chatUsers.getSender());
                        }
                    }
                    if(currentUser.getUid().equals(chatUsers.getSender())){
                        if(!MyUsersList.contains(chatUsers.getReceiver())){
                            MyUsersList.add(chatUsers.getReceiver());
                        }
                    }
                }
                getUser(MyUsersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }
    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(currentUser.getUid()).setValue(token1);
    }

    private void getUser(final ArrayList<String> myUsersList) {
        mUsers=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()) {
                    Users users=data.getValue(Users.class);
                    for(int i=0;i<myUsersList.size();i++){
                        if(myUsersList.get(i).equals(users.getId())){
                                mUsers.add(users);
                        }
                    }
                }
                LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                recyclerView.setLayoutManager(layoutManager);
                UsersListAdapter usersListAdapter=new UsersListAdapter(mUsers,getContext());
                recyclerView.setAdapter(usersListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error",error.getMessage());

            }
        });
    }

}