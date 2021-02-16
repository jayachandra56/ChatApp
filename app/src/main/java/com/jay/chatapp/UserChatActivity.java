package com.jay.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jay.chatapp.Notifications.Client;
import com.jay.chatapp.Notifications.Data;
import com.jay.chatapp.Notifications.MyResponse;
import com.jay.chatapp.Notifications.Sender;
import com.jay.chatapp.Notifications.Token;
import com.jay.chatapp.adapters.ConvoAdapter;
import com.jay.chatapp.modal.Users;
import com.jay.chatapp.modal.messages;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserChatActivity extends AppCompatActivity {
    TextView username;
    EditText message;
    ImageButton sendbtn;
    FirebaseUser auth;
    String reveiverName, receiverID;
    RecyclerView chatList;
    ArrayList<messages> conversation=new ArrayList<>();
    APIService apiService;
    Boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        auth=FirebaseAuth.getInstance().getCurrentUser();
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        reveiverName =getIntent().getStringExtra("username");
        receiverID =getIntent().getStringExtra("userid");
        username=findViewById(R.id.username);
        username.setText(reveiverName);
        message=findViewById(R.id.message);
        sendbtn=findViewById(R.id.send);
        chatList=findViewById(R.id.chat_list);
        conversation();
        register("test");
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setting notify to "true" to send notification only when button clicked
                notify=true;
                String msg=message.getText().toString();
                if(!msg.equals("")){
                    sendMessage(auth.getUid(), receiverID,msg);
                }
                message.setText("");
            }
        });
    }

    //sending message
    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> messageDetails=new HashMap<>();
        messageDetails.put("sender",sender);
        messageDetails.put("receiver",receiver);
        messageDetails.put("message",message);
        reference.child("chats").push().setValue(messageDetails);
        if(notify){
            sendNotification(receiverID,reveiverName,message);
        }

        notify=false;

    }

    //called in onCreate method to load chat list of particular user
    private void conversation(){
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                conversation.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    messages convo=snapshot.getValue(messages.class);
                    assert convo != null;
                    assert user != null;


                    if(convo.getSender().equals(user.getUid()) && convo.getReceiver().equals(receiverID)){
                        conversation.add(convo);
                    }
                    if(convo.getReceiver().equals(user.getUid()) && convo.getSender().equals(receiverID)){
                        conversation.add(convo);
                    }
                }
                LinearLayoutManager layoutManager=new LinearLayoutManager(UserChatActivity.this,RecyclerView.VERTICAL,false);
                chatList.setLayoutManager(layoutManager);
                Log.e("testMSG",conversation.toString());
                ConvoAdapter usersListAdapter=new ConvoAdapter(conversation,UserChatActivity.this);
                chatList.setAdapter(usersListAdapter);
                chatList.scrollToPosition(conversation.size()-1);
                chatList.setHasFixedSize(true);
//                chatList.smoothScrollToPosition(conversation.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final String msg=message.getText().toString();
        reference=FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user=snapshot.getValue(Users.class);
//                if(notify){
//                    sendNotification(receiverID,reveiverName,msg);
//                }
//
//                notify=false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //send notification
    private void sendNotification(final String receiverID, final String reveiverName, final String msg) {




        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiverID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Token token=snapshot1.getValue(Token.class);
                    Data data=new Data(auth.getUid(),R.drawable.notify,reveiverName+" "+msg,"new message",receiverID);
                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code()==200){
                                MyResponse myResponse=response.body();
                                Toast.makeText(UserChatActivity.this,""+myResponse.getSuccess(), Toast.LENGTH_SHORT).show();
                                Log.e(" Success messageStatus",""+myResponse.getSuccess());
//                                if(response.body().success)
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(UserChatActivity.this,"failed", Toast.LENGTH_SHORT).show();
                            Log.e(" Failed messageStatus",t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void register(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    String msg = "success";
                    if (!task.isSuccessful()) {
                        msg = "failed";
                    }
                    Log.d("messageTest", msg);
                    Toast.makeText(UserChatActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }
}