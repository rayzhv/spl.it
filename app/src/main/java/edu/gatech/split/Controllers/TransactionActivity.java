package edu.gatech.split.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.gatech.split.Model.Transaction;
import edu.gatech.split.Model.User;
import edu.gatech.split.R;

public class TransactionActivity extends AppCompatActivity {
    DatabaseReference databaseTransactions;
    EditText total;
    EditText purpose;
//    EditText payer;
    Spinner payerSpinner;
    Button submitButton;
    
    ArrayList<String> usersList;
    private DatabaseReference databaseUsers;

    private static final String TAG = "TransitionActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseTransactions = FirebaseDatabase.getInstance().getReference("transactionTest");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        Button cancelButton = (Button) findViewById(R.id.cancel);
        submitButton = (Button) findViewById(R.id.submit);
        total = (EditText) findViewById(R.id.total);
        purpose = (EditText) findViewById(R.id.purpose);
//        payer = (EditText) findViewById(R.id.payer);
        payerSpinner = (Spinner) findViewById(R.id.spinnerPayer);

        // Read from the database
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // iterate through members and add to users
                ArrayList<User> users = new ArrayList<>();
                // this runs in O(n^2) time so isn't too efficient
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                for (User user : users) {
                    if (user.getName() != "Split cost evenly") {
                        usersList.add(user.getName());
                    }
                }

                // add a 'split cost' option
                usersList.add("Split cost evenly");

                // load users into the dropdown menu
                populateSpinner();
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
            
        usersList = new ArrayList<>();
        populateSpinner();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction();
                Intent cancelIntent = new Intent();
                cancelIntent.setClass(getBaseContext(), MainActivity.class);
                startActivity(cancelIntent);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = new Intent();
                cancelIntent.setClass(getBaseContext(), MainActivity.class);
                startActivity(cancelIntent);
            }
        });

    }
    private void addTransaction() {
        if (total.getText().toString().length()!=0 && purpose.getText().toString().length()!=0) {
            double amount = Double.parseDouble(total.getText().toString().trim());
            String subject = purpose.getText().toString().trim();
            String user = String.valueOf(payerSpinner.getSelectedItem());
            if (amount != 0 && !subject.isEmpty()) {
                String key = databaseTransactions.push().getKey();
                Transaction txn = new Transaction(key, subject, amount, user);
                databaseTransactions.child(key).setValue(txn);
                Toast.makeText(this, "Transaction recorded.", Toast.LENGTH_LONG).show();
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
    

    public void populateSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, usersList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payerSpinner.setAdapter(dataAdapter);
    }

}
