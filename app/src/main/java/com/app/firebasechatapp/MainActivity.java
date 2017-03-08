package com.app.firebasechatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<Message> adapter;
    private RelativeLayout activity_main;
    private Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.editText);
                FirebaseDatabase.getInstance().getReference().push()
                        .setValue(new Message(
                                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                editText.getText().toString()));
                editText.setText("");
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().build()
                    , SIGN_IN_REQUEST_CODE);
        }else {
            displayChat();
        }


    }

    private void displayChat(){
        ListView listView = (ListView) findViewById(R.id.list);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.message_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView message,author,date;
                message = (TextView) v.findViewById(R.id.messageView);
                date = (TextView) v.findViewById(R.id.dateView);
                author = (TextView) v.findViewById(R.id.authorView);

                message.setText(model.getMessage());
                author.setText(model.getAuthor());

                date.setText(DateFormat.format("dd-mm-yyyy (HH:mm:ss)",model.getDate()));
            }
        };

        listView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE){

            if(resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(),"Signed in",Toast.LENGTH_SHORT).show();
                displayChat();
            }else{
                Toast.makeText(getApplicationContext(),"Unsigned",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout){
            AuthUI.getInstance().signOut(this);
        }
        return true;
    }
}
