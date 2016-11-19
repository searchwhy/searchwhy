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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;

public class Main extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonLoad = (Button)findViewById(R.id.buttonLoad);
        Button buttonExt = (Button)findViewById(R.id.buttonExit);
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadIMG();
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
            gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            gallery.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(gallery,PICK_IMAGE);
            if(gallery.getClipData().getItemCount()>=2){
                gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
           try {
               if(data.getClipData().getItemCount()>=2) {
                   
                   //isFirst = true;
                   //loadIMG();
               }
               else{
                   data.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
               }
               String name1 = getImg(data.getClipData().getItemAt(0).getUri());
               Bitmap img1 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getClipData().getItemAt(0).getUri());
               String name2 = getImg(data.getClipData().getItemAt(1).getUri());
               Bitmap img2 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getClipData().getItemAt(1).getUri());
           } catch(FileNotFoundException e){
               e.printStackTrace();
           } catch(IOException e){
               e.printStackTrace();
           }
        }
        Intent intent = new Intent(this,gameView.class);
        startActivity(intent);
        this.finish();
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
