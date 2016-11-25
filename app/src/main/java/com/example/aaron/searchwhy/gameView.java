package com.example.aaron.searchwhy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class gameView extends AppCompatActivity {
    static  ProgressBar pgBar;
    int a= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Button buttonBck = (Button)findViewById(R.id.buttonBck);
        buttonBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveBck();
            }
        });
        Bundle img = getIntent().getExtras();
        byte [] array1 = img.getByteArray("img1");
        byte [] array2 = img.getByteArray("img2");
        Bitmap img1 = BitmapFactory.decodeByteArray(array1, 0 , array1.length);
        Bitmap img2 = BitmapFactory.decodeByteArray(array2, 0, array2.length);
        pgBar = (ProgressBar)findViewById(R.id.progressBar);
        labeling label = new labeling(img1, img2);
        Bitmap sub_img = label.labelingDo();
        final gameInfo gameInfo = new gameInfo(sub_img,label.getIndex(),label.getSpotArea());
        pgBar.setMax(gameInfo.time);


        ImageView view1 = (ImageView)findViewById(R.id.imageView);
        ImageView view2 = (ImageView)findViewById(R.id.imageView2);

        Bitmap new_img = drawCircle(img2, 50, 50);
        view1.setImageBitmap(img1);
        view2.setImageBitmap(new_img);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(a<gameInfo.time){
                    try {
                        Thread.sleep(1000);
                        a++;

                    }catch(Exception e){
                    }finally{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pgBar.setProgress(a);
                                pgBar.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
                moveBck();
            }
        }).start();

    }
    private void moveBck(){
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
        finish();
    }

    public Bitmap drawCircle(Bitmap img1, int x, int y){
        Bitmap new_img = Bitmap.createBitmap(img1.getWidth(), img1.getHeight(),
                Bitmap.Config.ARGB_8888);
        Bitmap circle = BitmapFactory.decodeResource(getResources(),
                R.drawable.correct_m);
        Canvas canvas = new Canvas(new_img);
        canvas.drawBitmap(img1, new Matrix(), null);
        canvas.drawBitmap(circle, x, y, null);
        return new_img;
    }
}
