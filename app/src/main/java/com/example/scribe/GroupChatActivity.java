package com.example.scribe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.scribe.Adapters.MessageAdapter;
import com.example.scribe.Models.AllMethods;
import com.example.scribe.Models.Message;
import com.example.scribe.Models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    Users u;
   List <Message> messages;


   RecyclerView rvMessage;

   EditText etMessage;
   ImageButton imgButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        init();
    }

    private  void  init(){
        auth = FirebaseAuth .getInstance();
        database = FirebaseDatabase.getInstance();
        u = new Users();
        rvMessage = (RecyclerView)findViewById(R.id.rvMessage);
        etMessage = findViewById(R.id.etMesssage);
        imgButton = findViewById(R.id.btnSend);
        imgButton.setOnClickListener(this);
        messages = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {

        if(!TextUtils.isEmpty(etMessage.getText().toString())){

            Message message = new Message(etMessage.getText().toString(), u.getName());
            etMessage.setText("");
            messagedb.push().setValue(message);
        }else{
            Toast.makeText(getApplicationContext(),"Enter a Message",Toast.LENGTH_SHORT).show();
        }
    }

    public void onStart(){
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();

        u.setUid(currentUser.getUid());
        u.setEmail(currentUser.getEmail());

        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 u = dataSnapshot.getValue(Users.class);
                 u.setUid(currentUser.getUid());
                AllMethods.name = u.getName();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        messagedb = database.getReference("messages");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // displays all messages

                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // for changes

                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());

                List<Message> newMessages = new ArrayList<>();

                for(Message m: messages){

                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    }else{
                        newMessages.add(m);
                    }
                }

                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Message message = dataSnapshot.getValue(Message.class);

            message.setKey(dataSnapshot.getKey());
            List<Message> newMessages = new ArrayList<Message>();

            for(Message m:messages){

                if(!m.getKey().equals(message.getKey())){
                    newMessages.add(m);
                }
            }

            messages = newMessages;
            displayMessages(messages);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuLogout) {
            auth.signOut();
            finish();
            startActivity(new Intent(GroupChatActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();
    }

    private void displayMessages(List<Message> messages) {

        rvMessage.setLayoutManager(new LinearLayoutManager(GroupChatActivity.this));
        messageAdapter = new MessageAdapter(GroupChatActivity.this, messages,messagedb);
        rvMessage.setAdapter(messageAdapter);
    }

}
