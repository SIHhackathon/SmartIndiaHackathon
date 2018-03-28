package sujannalijo.com.smartindiahackathon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class smarty extends Activity {
    private static final String TAG = smarty.class.getSimpleName();
    private static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT = 0;

    private static final String LOG_TAG ="smarty" ;
  //  public Button btnLoadImage;
    ImageView imageView;
    Button extracttext;
    TextView t1;
    private TextRecognizer detector;
    final int RQS_IMAGE1 = 1;
    int Permission_ALL =1;
    Uri source;
    Bitmap bitmapMaster;
    Canvas canvasMaster;
    String result;
    private String mCurrentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smarty);
      //  btnLoadImage = (Button) findViewById(R.id.loadimage);
      //  imageResult = (ImageView) findViewById(R.id.result);
        extracttext =(Button)findViewById(R.id.extracttext);
        t1= (TextView) findViewById(R.id.t1);
        detector = new TextRecognizer.Builder(getApplicationContext()).build();

        /*String[] Permissions ={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!hasPermissions(this,Permissions)){
            ActivityCompat.requestPermissions(this,Permissions,Permission_ALL);
        }
        *//*  btnLoadImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQS_IMAGE1);
            }
        });
      */  extracttext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQS_IMAGE1);
*/
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        dispatchTakePictureIntent();
                    }else{
                        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            Toast.makeText(getApplicationContext(), "App requires access for external storage", Toast.LENGTH_SHORT).show();
                            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Toast.makeText(getApplicationContext(), "App requires access for external storage", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            ActivityCompat.requestPermissions(smarty.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_FINE_LOCATION_RESULT);
                        }
                    }
                }else{
                    dispatchTakePictureIntent();

                }

            }
        });

    }

    /*void getTextFromImage(View view) {
        Bitmap bitmap = new BitmapFactory().decodeResource(getApplicationContext().getResources(),R.id.result);

                if(!textRecognizer.isOperational()){
                    Toast.makeText(getApplicationContext(),"Unsupported action",Toast.LENGTH_SHORT).show();
                    Log.w(LOG_TAG, "Detector dependencies are not yet available.");

                    // Check for low storage.  If there is low storage, the native library will not be
                    // downloaded, so detection will not become operational.
                    IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                    boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                    if (hasLowStorage) {
                        Toast.makeText(this,"Low Storage", Toast.LENGTH_LONG).show();
                        Log.w(LOG_TAG, "Low Storage");
                    }

                }else{

                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items= textRecognizer.detect(frame);

                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<items.size();++i){
                        TextBlock myitem = items.valueAt(i);
                        sb.append(myitem.getValue());
                        sb.append("\n");
                    }
                 //   t1.setText(sb.toString());
                     result =sb.toString();
                     System.out.println(result);


                }

    }

*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            try{
                Uri selectedImage = data.getData();
                mCurrentPhotoPath=getRealPathFromURI(getApplicationContext(),selectedImage);
                startOCR();

            }catch (Exception e){

                t1.setText("Problem encoured");

            }

        }


    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void dispatchTakePictureIntent () {

        Intent takePictureIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takePictureIntent.setType("image/*");


        // Ensure that there's a camera activity to handle the intent

 /*       if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go

            File photoFile = null;

            try {

                photoFile = createImageFile();

            } catch (IOException ex) {

                // Error occurred while creating the File

            }
*/
            // Continue only if the File was successfully created

           /* if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,

                        BuildConfig.APPLICATION_ID + ".provider",

                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
*/
                startActivityForResult(takePictureIntent, 1024);

     //       }

   //     }

    }
    public void startOCR(){
        try {
            imageView =(ImageView) findViewById(R.id.imageView);

            Bitmap bitmap = getBitmap();

            imageView.setImageBitmap(bitmap);

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this,"Low Storage", Toast.LENGTH_LONG).show();
                Log.d("low torage", "Low Storage");
            }


            detector = new TextRecognizer.Builder(this).build();
            Log.d("Debug",""+bitmap+"\n");
            if (detector.isOperational() && null != bitmap) {

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                SparseArray<TextBlock> textBlocks = detector.detect(frame);

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < textBlocks.size(); ++ i) {

                    TextBlock tb = textBlocks.get(i);

                    if (tb != null && tb.getValue() != null) {

                        sb.append(tb.getValue());

                    }

                }

                if (textBlocks.size() == 0) {

                    t1.setText("Scan failed");

                } else {

                    t1.setText(sb.toString());

                }

            } else {

                t1.setText("Invalid image");

            }

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());

        }

    }

    private Bitmap getBitmap () {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = false;

        options.inSampleSize = 5;

        return BitmapFactory.decodeFile(mCurrentPhotoPath, options);

    }
/*
    private File createImageFile () throws IOException {

        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(

                imageFileName,  *//* prefix *//*

                ".jpg",         *//* suffix *//*

                storageDir      *//* directory *//*

        );



        // Save a file: path for use with ACTION_VIEW intents

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case REQUEST_PERMISSION_FINE_LOCATION_RESULT:
                if(grantResults.length >0){
                    boolean Reading =grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean Writing =grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(Reading && Writing){
                        Toast.makeText(smarty.this,"Permission granted",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(smarty.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                    break;

                }
        }
    }

}