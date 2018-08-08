package com.toshiro97.chatapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtPass)
    EditText edtPass;
    @BindView(R.id.edtCfPass)
    EditText edtCfPass;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    FirebaseDatabase database;
    DatabaseReference referenceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initText();

        database = FirebaseDatabase.getInstance();
        referenceUser = database.getReference("Users");
    }

    private void initText(){
        edtPhone.setText("0976592391");
        edtName.setText("toshiro");
        edtPass.setText("123456");
        edtCfPass.setText("123456");
    }

    @OnClick(R.id.btnRegister)
    public void onViewClicked() {
        final String phone = edtPhone.getText().toString();
        String name = edtName.getText().toString();
        String pass = edtPass.getText().toString();
        String cfPass = edtCfPass.getText().toString();
        if (pass.equals(cfPass)){
            final User user = new User(phone,name,pass);
            referenceUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phone).exists()){
                        Toast.makeText(RegisterActivity.this, "Tai khoan da ton tai", Toast.LENGTH_SHORT).show();
                    }else {
                        referenceUser.child(phone).setValue(user);
                        Toast.makeText(RegisterActivity.this, "Dang ky thanh cong", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else {
            Toast.makeText(this, "Mat khau khong khop", Toast.LENGTH_SHORT).show();
        }
    }
}
