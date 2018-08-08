package com.toshiro97.chatapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.toshiro97.chatapp.R;
import com.toshiro97.chatapp.adapter.ChatApdater;
import com.toshiro97.chatapp.model.ChatMessenger;
import com.toshiro97.chatapp.model.User;
import com.toshiro97.chatapp.until.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    TextView tvName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerChat)
    RecyclerView recyclerChat;
    @BindView(R.id.edtMessenger)
    EditText edtMessenger;
    @BindView(R.id.btnSend)
    ImageView btnSend;
    ChatApdater chatApdater;

    RecyclerView.LayoutManager layoutManager;
    List<ChatMessenger> chatMessengerList;
    FirebaseDatabase database;
    DatabaseReference referenceSend;
    DatabaseReference referenceReceive;
    String userSend = Common.currentUser.getPhone();
    String userRecei = Common.userSend.getPhone();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        tvName = toolbar.findViewById(R.id.tvName);
        tvName.setText(userRecei);

        database = FirebaseDatabase.getInstance();
        referenceSend = database.getReference("messenger").child(userSend + "-" + userRecei);
        referenceReceive = database.getReference("messenger").child(userRecei + "-" + userSend);

        chatMessengerList = new ArrayList<>();

        chatApdater = new ChatApdater(chatMessengerList);
        recyclerChat.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true);
        recyclerChat.setLayoutManager(layoutManager);
        recyclerChat.setAdapter(chatApdater);
        getListMessenger();

    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {
        String messenger = edtMessenger.getText().toString();
        ChatMessenger chatMessengerSend = new ChatMessenger(messenger);
        chatMessengerSend.setSend(true);
        referenceSend.push().setValue(chatMessengerSend);
        ChatMessenger chatMessengerRecei = new ChatMessenger(messenger);
        referenceReceive.push().setValue(chatMessengerRecei);
        edtMessenger.setText("");
    }

    private void getListMessenger(){
        Query query = referenceSend.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessengerList.clear();
                ArrayList<ChatMessenger> chatMessengers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatMessenger chatMessenger = snapshot.getValue(ChatMessenger.class);
                    chatMessengers.add(chatMessenger);
                }
                for (int i = chatMessengers.size() - 1; i >= 0; i--){
                    chatMessengerList.add(chatMessengers.get(i));
                }
                chatApdater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
