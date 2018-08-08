package com.toshiro97.chatapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toshiro97.chatapp.R;
import com.toshiro97.chatapp.model.User;
import com.toshiro97.chatapp.until.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtPass)
    EditText edtPass;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    FirebaseDatabase database;
    DatabaseReference referenceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initText();
        database = FirebaseDatabase.getInstance();
        referenceUser = database.getReference("Users");
    }
    private void initText(){
        edtPhone.setText("0976592391");
        edtPass.setText("123456");
    }
    @OnClick({R.id.btnLogin, R.id.btnRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                final String phone = edtPhone.getText().toString();
                final String pass = edtPass.getText().toString();
                referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(phone).exists()){
                            User user = dataSnapshot.child(phone).getValue(User.class);
                            if (user.getPassword().equals(pass)){
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                                Common.currentUser = user;
                            }else {
                                Toast.makeText(LoginActivity.this, "Mat khau sai", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "Tai khoan chua ton tai", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case R.id.btnRegister:
                Intent regiterIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regiterIntent);
                break;
        }
    }
}
