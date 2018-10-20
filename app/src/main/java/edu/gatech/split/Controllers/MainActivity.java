package edu.gatech.split.Controllers;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import edu.gatech.split.Model.User;
import edu.gatech.split.R;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView imageView;

    private ArrayList<User> list;

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

        // load members into the listView
        loadMembers();


        // Firebase initialization =========================================
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        OcrManager manager = new OcrManager();
        manager.initAPI();
    }

    private void openGallery() {
        try {
//            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, PICK_IMAGE);
        } catch (Exception e) {
            Log.i("error",e.toString());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
//                Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
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

//        //shows a list of all the members in the database
//        list = new ArrayList<User>();
//        list.add(new User("Jefferson Zhan", 1000.00));
//        list.add(new User("Raymond Zhu", -50.00));
//        list.add(new User("Tony Zhang", 50.00));
////        list.add("Jefferson Zhan");
////        list.add("Raymond Zhu");
////        list.add("Tony Zhang");
//        ArrayAdapter<User> membersAdapter =
//                new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, list);
//        ListView membersList = findViewById(R.id.membersView);
//        membersList.setAdapter(membersAdapter);
////        shelterList.setOnItemClickListener(this);


        int key = 0;

        final String   Name    = "Name: ";
        final String   Total = "Total: ";

        final String[] matrix  = { "_id", "name", "total" };
        final String[] columns = { "name", "total" };
        final int[]    layouts = { android.R.id.text1, android.R.id.text2 };

        MatrixCursor cursor = new MatrixCursor(matrix);


        cursor.addRow(new Object[] { key++, Name, "Test" });
        cursor.addRow(new Object[] { key++, Total, "100" });

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
