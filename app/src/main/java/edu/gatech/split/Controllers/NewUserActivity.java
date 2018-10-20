package edu.gatech.split.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.gatech.split.Model.User;
import edu.gatech.split.R;
import java.util.*;

public class NewUserActivity extends AppCompatActivity {
    DatabaseReference databaseUsers;
    EditText username;
    EditText total;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        databaseUsers= FirebaseDatabase.getInstance().getReference("users");

        Button cancelButton = (Button) findViewById(R.id.cancel2);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = new Intent();
                cancelIntent.setClass(getBaseContext(), MainActivity.class);
                startActivity(cancelIntent);
            }
        });
        submitButton = (Button) findViewById(R.id.submit2);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        total = (EditText) findViewById(R.id.total2);
        username = (EditText) findViewById(R.id.newUserNameText);

    }

    private void addUser() {
        if(username.getText().toString().length() != 0 && total.getText().toString().length() != 0) {
            double amount = Double.parseDouble(total.getText().toString().trim());
            String un = username.getText().toString().trim();
            if(amount !=  0 && !un.isEmpty()) {
                String key = databaseUsers.push().getKey();
                User newUser = new User(un, amount);
//                List<Object> object = new ArrayList<Object>();
//                userArray[0] = un;
//                userArray[1] = amount;
                databaseUsers.child(key).setValue(newUser);
                Toast.makeText(this, "User Edited.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Can't have empty fields.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
