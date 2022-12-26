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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    ImageView keLogin;
    EditText et_nim, et_email, et_password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        keLogin = findViewById(R.id.back_to_login);

        keLogin.setOnClickListener(view -> {
            Intent i = new Intent(Register.this, Login.class);
            startActivity(i);
        });

        et_nim = findViewById(R.id.et_nim);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(Register.this);
                pd.setMessage("Please, wait...");
                pd.show();

                String str_nim = et_nim.getText().toString();
                String str_email = et_email.getText().toString();
                String str_password = et_password.getText().toString();

                if(TextUtils.isEmpty(str_nim) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
                    Toast.makeText(Register.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }else if(str_password.length() < 6) {
                    Toast.makeText(Register.this, "Password must have 6 characters", Toast.LENGTH_SHORT).show();
                }else {
                    register(str_nim, str_email, str_password);
                }
            }
        });
    }

    private void register(String et_nim, String et_email, String et_password){
        auth.createUserWithEmailAndPassword(et_email, et_password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child(userid);

                            HashMap<String, Object>hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("NIM", et_nim.toLowerCase());
                            hashMap.put("bio", "");
                            hashMap.put("imager", "https://firebasestorage.googleapis.com/v0/b/tubes-mobile-a3a82.appspot.com/o/ic_profile.png?alt=media&token=784a13df-3f5c-4b07-b9a1-cd099ab52522");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();
                                        Intent intent = new Intent(Register.this, StartActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                        }else{
                            pd.dismiss();
                            Toast.makeText(Register.this, "You can't register with this NIM or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}