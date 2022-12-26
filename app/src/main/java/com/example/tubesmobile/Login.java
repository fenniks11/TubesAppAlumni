package com.example.tubesmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText et_nim, et_password;
    Button btn_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView keRegister;
        TextView keForgotDetails;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        keRegister = findViewById(R.id.tv_sign_up);
        keForgotDetails = findViewById(R.id.tv_forgot_login_details);

        keRegister.setOnClickListener(view -> {
            Intent i = new Intent(Login.this, Register.class);
            startActivity(i);
        });
        keForgotDetails.setOnClickListener(view -> {
            Intent i = new Intent(Login.this, ForgotPassword.class);
            startActivity(i);
        });

        et_nim = findViewById(R.id.et_nim);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pd = new ProgressDialog(Login.this);
                pd.setMessage("Please, wait...");
                pd.show();

                String str_nim = et_nim.getText().toString();
                String str_password = et_password.getText().toString();

                if(TextUtils.isEmpty(str_nim) || TextUtils.isEmpty(str_password)) {
                    Toast.makeText(Login.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(str_nim, str_password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(auth.getCurrentUser().getUid());

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                pd.dismiss();
                                                Intent intent = new Intent(Login.this, StartActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                pd.dismiss();
                                            }
                                        });
                                    }else{
                                        pd.dismiss();
                                        Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}