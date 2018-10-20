package edu.gatech.split.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.gatech.split.Model.Transaction;
import edu.gatech.split.R;

public class TransactionActivity extends AppCompatActivity {
    DatabaseReference databaseTransactions;
    EditText total;
    EditText purpose;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseTransactions = FirebaseDatabase.getInstance().getReference("transactions");
        Button cancelButton = (Button) findViewById(R.id.cancel);
        submitButton = (Button) findViewById(R.id.submit);
        total = (EditText) findViewById(R.id.total);
        purpose = (EditText) findViewById(R.id.purpose);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction();
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
            if (amount != 0 && !subject.isEmpty()) {
                String key = databaseTransactions.push().getKey();
                Transaction txn = new Transaction(key, subject, amount);
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

}
