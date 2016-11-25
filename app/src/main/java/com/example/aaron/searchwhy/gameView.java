package com.example.aaron.searchwhy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
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
    float x =100;
    float y =100;
    boolean isFirst=true;
    Bitmap newImg;
    Bitmap img1, img2;
    ImageView view1, view2;

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
        img1 = BitmapFactory.decodeByteArray(array1, 0 , array1.length);
        img2 = BitmapFactory.decodeByteArray(array2, 0, array2.length);
        pgBar = (ProgressBar)findViewById(R.id.progressBar);
        labeling label = new labeling(img1, img2);
        Bitmap sub_img = label.labelingDo();
        final gameInfo gameInfo = new gameInfo(sub_img,label.getIndex(),label.getSpotArea());
        pgBar.setMax(gameInfo.time);


        view1 = (ImageView)findViewById(R.id.imageView);
        view2 = (ImageView)findViewById(R.id.imageView2);
        view2.setOnTouchListener(tEvent);


        view1.setImageBitmap(img1);
        if(isFirst){
            view2.setImageBitmap(img2);
            isFirst = false;
        }


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
    public View.OnTouchListener tEvent = new View.OnTouchListener() {
        @Override
               public boolean onTouch(View v, MotionEvent event){
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                x = event.getX();
                y = event.getY();
               int[] temp = new int[2];
                view2.getLocationOnScreen(temp);
                float widthpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
                float heightpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());
                float a,b,c,d;
                if(img2.getHeight()>img2.getWidth()){
                    c= heightpx/img2.getHeight();
                    a =img2.getHeight()/ heightpx ;
                    b =1/c;
                    d = (widthpx - c*img2.getWidth())/2;
                    x = x*b - d+60;
                    y = y*a-40;
                }else{
                    c= widthpx/img2.getWidth();
                    b = img2.getWidth()/widthpx;
                    a = 1/c;
                    d = (heightpx - c*img2.getHeight())/2;
                    x = x*b - 40;
                    y = y*a - d - 60;
                }
                newImg = drawCircle(img2, x,y);
                img2 = newImg;
                view2.setImageBitmap(newImg);
            }
            return true;
        }
        public Bitmap drawCircle(Bitmap img1, float x, float y){
            Bitmap new_img = Bitmap.createBitmap(img1.getWidth(), img1.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Bitmap circle = BitmapFactory.decodeResource(getResources(),
                    R.drawable.correct_s);
            Canvas canvas = new Canvas(new_img);
            canvas.drawBitmap(img1, new Matrix(), null);
            canvas.drawBitmap(circle, x, y, null);
            return new_img;
        }


    };
}
