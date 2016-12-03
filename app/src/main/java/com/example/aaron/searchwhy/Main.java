package com.example.aaron.searchwhy;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private Uri imgUri;
    private int getLoadbtn = 1;
    static Bitmap img1 , img2;
    static String name1, name2;
    static byte[] array1 , array2;
    static Bitmap resized1, resized2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
        private void openGallery(){
            Intent gallery = new Intent(Intent.ACTION_PICK);
            gallery.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(gallery,PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
           try {
                if(getLoadbtn ==1) {
                    name1 = getImg(data.getData());
                    img1 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    resized1 = resized(img1);
                    resized1.compress(Bitmap.CompressFormat.PNG, 100,stream1);
                    array1 = stream1.toByteArray();
                } else if(getLoadbtn == 2) {
                    name2 = getImg(data.getData());
                    img2 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    resized2 = resized(img2);
                    resized2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                    array2 = stream2.toByteArray();
                }
           } catch(FileNotFoundException e){
               e.printStackTrace();
           } catch(IOException e){
               e.printStackTrace();
           }
        }
        if(img1!=null && img2!=null) {
            System.out.println(img1.getHeight());
            System.out.println(img2.getHeight());
            if (img1.getHeight() != img2.getHeight()){
                Toast.makeText(this, "사이즈가 다른 이미지입니다. 다시 선택하세요.",4).show();
                img1 = null;
                img2 = null;
                resized1 = null;
                resized2 = null;
                array1 = null;
                array2 = null;
            }else{
                Intent intent = new Intent(this, gameView.class);
                intent.putExtra("img1", array1);
                intent.putExtra("img2", array2);
                startActivity(intent);
                img1 = null;
                img2 = null;
                resized1 = null;
                resized2 = null;
                array1 = null;
                array2 = null;
                this.finish();
            }

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

        public Bitmap resized(Bitmap src){
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            int maxWid = dm.widthPixels;
            int maxHei = dm.heightPixels;
            int hgt = (int)(maxHei*0.3);
            int wid = (int)(maxWid*0.15);
            Bitmap resized;

            int width = src.getWidth();
            int height = src.getHeight();
            if(width <height) {
                resized = src.createScaledBitmap(src, ((width * hgt) / height), hgt, true);
            }
            else{
                resized = src.createScaledBitmap(src, wid , ((height * wid) / width), true);
            }
            return resized;
        }

}
