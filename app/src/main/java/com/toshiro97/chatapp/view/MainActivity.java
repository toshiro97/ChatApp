package com.toshiro97.chatapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toshiro97.chatapp.R;
import com.toshiro97.chatapp.adapter.UserAdapter;
import com.toshiro97.chatapp.listener.UserClickListener;
import com.toshiro97.chatapp.model.User;
import com.toshiro97.chatapp.until.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerUser)
    RecyclerView recyclerUser;

    private UserAdapter userAdapter;
    private List<User> userList;
    FirebaseDatabase database;
    DatabaseReference referenceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        database =FirebaseDatabase.getInstance();
        referenceUser = database.getReference("Users");

        userList = new ArrayList<>();


        userAdapter = new UserAdapter(userList, new UserClickListener() {
            @Override
            public void onClickUser(int postiton) {
                Common.userSend = userList.get(postiton);
                Intent intentChat = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(intentChat);
            }
        });

        recyclerUser.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerUser.setLayoutManager(layoutManager);
        recyclerUser.setAdapter(userAdapter);
        getListUser();

    }
    private void getListUser(){
        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if (!user.getPhone().equals(Common.currentUser.getPhone())){
                        users.add(user);
                    }
                }
                for (int i = users.size() - 1; i >= 0; i--){
                    userList.add(users.get(i));
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
