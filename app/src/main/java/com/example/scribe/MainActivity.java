package com.example.scribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextInputEditText textemail, textpassword;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            Intent i = new Intent(MainActivity.this, GroupChatActivity.class);
            startActivity(i);
        }
        else{
            setContentView(R.layout.activity_main);

            textemail = findViewById(R.id.email_ed_login);
            textpassword = findViewById(R.id.password_ed_login);

        }


    }

    public void LoginUser(View v){
        String email = textemail.getText().toString();
        String password = textpassword.getText().toString();

        if(!email.equals("") && !password.equals("")){
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Logged In", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, GroupChatActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(getApplicationContext(),"Wrong Credentials! Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    public void gotoRegister(View v){
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }
}
