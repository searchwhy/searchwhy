package com.example.aaron.searchwhy;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private Uri imgUri;
    private int getLoadbtn = 1;
    static Bitmap img1 , img2;
    static String name1, name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonLoad1 = (Button)findViewById(R.id.buttonLoad1);
        Button buttonLoad2 = (Button)findViewById(R.id.buttonLoad2);
        Button buttonExt = (Button)findViewById(R.id.buttonExit);
        buttonLoad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoadbtn = 1;
                openGallery();
            }
        });
        buttonLoad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoadbtn = 2;
                openGallery();
            }
        });
        buttonExt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void loadIMG(){
        openGallery();


    }
        private void openGallery(){
            Intent gallery = new Intent(Intent.ACTION_PICK);
            gallery.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(gallery,PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
           try {
                if(getLoadbtn ==1) {
                    name1 = getImg(data.getData());
                    img1 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } else if(getLoadbtn == 2) {
                    name2 = getImg(data.getData());
                    img2 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                }
           } catch(FileNotFoundException e){
               e.printStackTrace();
           } catch(IOException e){
               e.printStackTrace();
           }
        }
        if(img1!=null && img2!=null) {
            Intent intent = new Intent(this, gameView.class);
            intent.putExtra("img1", img1);
            intent.putExtra("img2", img2);
            startActivity(intent);
            this.finish();
        }
    }

    public String getImg(Uri data){
        String[] proj = {MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(columnIndex);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }

}
