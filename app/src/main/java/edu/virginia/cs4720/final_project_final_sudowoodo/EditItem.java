package edu.virginia.cs4720.final_project_final_sudowoodo;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class EditItem extends AppCompatActivity {

    static final int TAKE_PHOTO_PERMISSION = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int PICK_IMAGE_REQUEST = 3;

    ImageView imageView;
    Button takePictureButton;

    Uri file;

    //For SaveEntry
    EditText nameInput;
    Spinner categoryInput;
    EditText textDollar;
    EditText detailInput;
    //Some pic of the object
    //Date
    DatePicker dateInput;
    //Image
    byte[] imgbyte;
    Bitmap imgSave;
    //Row Index in SQLite Table
    String rowIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //add back button
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        takePictureButton = (Button) findViewById(R.id.takePhotoButton);
        imageView = (ImageView) findViewById(R.id.imageView);

        // Check for camera permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, TAKE_PHOTO_PERMISSION);
        }

        //For SaveEntry
        nameInput = (EditText) findViewById(R.id.nameInput);
        categoryInput =(Spinner) findViewById(R.id.category);
        textDollar = (EditText) findViewById(R.id.textDollar);
        detailInput = (EditText) findViewById(R.id.detailInput);
        dateInput = (DatePicker) findViewById(R.id.dateInput);


        //Info passed from the caller intent
        Intent intent = this.getIntent();
        if (intent != null) {
            ArrayList<String> item = intent.getStringArrayListExtra("item");
            imgbyte = intent.getByteArrayExtra("image");

            //Name
            nameInput.setText(item.get(0));
            //Category
            for (int i = 0; i < categoryInput.getCount(); i++) {
                if (categoryInput.getItemAtPosition(i).equals(item.get(1))) {
                    categoryInput.setSelection(i);
                    break;
                }
            }
            //Amount spent
            textDollar.setText(item.get(2));
            //Date
            int year = Integer.parseInt(item.get(5));
            int month = Integer.parseInt(item.get(6)) - 1;
            int day = Integer.parseInt(item.get(7));
            dateInput.updateDate(year, month, day);
            //Detail
            detailInput.setText(item.get(3));
            //Image
            if (imgbyte != null) {
                imgSave = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);
                imageView.setImageBitmap(imgSave);
            }
            //rowIndex
            rowIndex = item.get(8);
        }


    }

    //Necessary for accessing SQLite table in this activity.
    public static class DataTable implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_DAY = "day";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // This is called when permissions are either granted or not for the app
        // You do not need to edit this code.

        if (requestCode == TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        // Add code here to start the process of taking a picture
        // Note you can send an intent to the camera to take a picture...
        // So start by considering that!
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }

    }

    public void getImageFromLibrary(View view) {
        // Add code here to start the process of getting a picture from the library
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);

    }

    public void saveImage(View view) {
        // Saves image
        //Create Path to save Image
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+" Money Tracker"); //Creates app specific folder
        path.mkdirs();
        File imageFile = new File(path, imgbyte +".png"); // Imagename.png
        try{
            FileOutputStream out = new FileOutputStream(imageFile);
            imgSave.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
            out.flush();
            out.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(this, new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
            Toast.makeText(this, "Image saved to gallery!", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {}
    }

    //OnClick of SaveButton
    public void saveEntry (View view) {
        //Prepare intent
        Intent i = new Intent(this,MainActivity.class);

        String entryName = nameInput.getText().toString();
        String entryCategory = categoryInput.getSelectedItem().toString();
        String entryDollar = textDollar.getText().toString();
        String entryDetail = detailInput.getText().toString();
        String entryYear = Integer.toString(dateInput.getYear());
        String entryMonth = Integer.toString(dateInput.getMonth() + 1);
        String entryDay = Integer.toString(dateInput.getDayOfMonth());
        String entryDate = entryMonth + "/" + entryDay + "/" + entryYear;


        //Some information is missing
        if ((entryName.equals("")) || (entryCategory.equals("")) || (entryDollar.equals("")) || (entryDetail.equals(""))) {
            Toast.makeText(this, "Required information missing", Toast.LENGTH_SHORT).show();
        } else {
            //all required info is there; save the data into the SQL database
            DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
            //Get the data repository in write mode
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //Create a new map of values, where column names are the keys
            ContentValues initialValues = new ContentValues();
            initialValues.put("name", entryName);
            initialValues.put("category",entryCategory);
            initialValues.put("amount", entryDollar);
            initialValues.put("details",entryDetail);
            initialValues.put("date",entryDate);
            initialValues.put("year",entryYear);
            initialValues.put("month",entryMonth);
            initialValues.put("day",entryDay);
            initialValues.put("image",imgbyte);
            initialValues.put("rowIndex", rowIndex);
            db.update("item", initialValues, "rowIndex =" + rowIndex, null);

            //Checking database (to delete later)
            db = dbHelper.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + DataTable.TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            
            i.putExtra("Source", "EditItemActivity Success");
        }

        startActivity(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Add code here to handle results from both taking a picture or pulling
        // from the image gallery.

        if (requestCode == REQUEST_TAKE_PHOTO) {
            // Add here.
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            imgSave = imageBitmap;

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            imgbyte = outputStream.toByteArray();

        }
        else if (requestCode == PICK_IMAGE_REQUEST) {
            //Add here.
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
                imgSave = bitmap;

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                imgbyte = outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



}
