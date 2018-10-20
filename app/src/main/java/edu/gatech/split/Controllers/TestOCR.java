package edu.gatech.split.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import edu.gatech.split.R;
import java.io.File;
import android.widget.EditText;
import android.widget.TextView;

public class TestOCR extends AppCompatActivity {
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ocr);

        output = (TextView) findViewById(R.id.textview);

        OcrManager manager = new OcrManager();
        manager.initAPI();
        File imgFile = new File("@drawable/testreceipt1.png");
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            String hopefullythisworks = manager.startRecognize(bitmap);
            output.setText(hopefullythisworks);
        }
        output.setText("image not found yet...");
    }
}
