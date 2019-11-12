package com.example.scribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.scribe.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText textemail, textpassword,textname;
    DatabaseReference reference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textemail = findViewById(R.id.email_ed_register);
        textpassword = findViewById(R.id.password_ed_register);
        textname = findViewById(R.id.name_ed_register);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void gotoLogin(View v){
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void RegisterUser(View v){
        final String email = textemail.getText().toString();
        final String password = textpassword.getText().toString();
        final String name = textname.getText().toString();

        if(!email.equals("") && !password.equals("")){
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                //insert in db

                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                Users u = new Users();
                                u.setName(name);
                                u.setEmail(email);

                                reference.child(firebaseUser.getUid()).setValue(u)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(),"Registered Successfully!", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(RegisterActivity.this, GroupChatActivity.class);
                                                    startActivity(i);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"Registeration Failed!", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                            }
                        }
                    });
        }
    }


}
