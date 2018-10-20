package edu.gatech.split.Controllers;

import android.content.Intent;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.gatech.split.Model.Transaction;
import edu.gatech.split.Model.User;
import edu.gatech.split.R;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView imageView;

    private ArrayList<User> membersList;
    private DatabaseReference databaseUsers;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, TransactionActivity.class));
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openGallery();
            }
        });


        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewUserActivity.class));
            }
        });



        // Firebase initialization =========================================
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("transactions");

        // Read from the database
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                Transaction membersDatabase = dataSnapshot.getValue(Transaction.class);
                ArrayList<Transaction> transactionList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    transactionList.add(snapshot.getValue(Transaction.class));
                }
                Log.w(TAG, transactionList.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        OcrManager manager = new OcrManager();
        manager.initAPI();

        // TODO: load members into membersList from firebase here
        membersList = new ArrayList<>();

        // load members into the listView
        loadMembers();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
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

    private void loadMembers() {

        // used to create a unique id for each column in the listview
        int key = 0;


        // specify columns to be used in the matrix
        final String[] matrix  = { "_id", "name", "total" };
        final String[] columns = { "name", "total" };
        final int[]    layouts = { android.R.id.text1, android.R.id.text2 };

        MatrixCursor cursor = new MatrixCursor(matrix);

        // dummy entries for testing purpose
        membersList.add(new User("Jeff Zhan", 1000 ));
        membersList.add(new User("Raymond Zhu", 50 ));
        membersList.add(new User("Tony Zhang", 60 ));
        membersList.add(new User("Bob", 70.50 ));
        membersList.add(new User("Joe", 20.00 ));
        membersList.add(new User("Matthew", -20.00 ));
        membersList.add(new User("Alice", 1000 ));

        for (User member : membersList) {
            cursor.addRow(new Object[] { key++, member.getName(), "$ " + member.getTotal()});
        }

        // create a listview adapter from the cursor
        // NOTE: gives a deprecation warning
        SimpleCursorAdapter data =
                new SimpleCursorAdapter(this,
                        R.layout.listview_layout,
                        cursor,
                        columns,
                        layouts);

        ListView membersList = findViewById(R.id.membersView);
        membersList.setAdapter( data );
    }
}
