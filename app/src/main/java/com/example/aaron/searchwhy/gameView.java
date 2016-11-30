package com.example.aaron.searchwhy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

import java.util.Stack;

public class gameView extends AppCompatActivity {
    static  ProgressBar pgBar;
    private Thread thread;
    int a= 0;
    float x =100;
    float y =100;
    boolean isFirst=true;
    Bitmap newImg;
    Bitmap img1, img2;
    Bitmap sub_img;
    ImageView view1, view2;
    gameInfo gameInfo;
    int index = 0;


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
        sub_img = label.labelingDo();
        gameInfo = new gameInfo(sub_img,label.getIndex(),label.getSpotArea());
        pgBar.setMax(gameInfo.time);


        view1 = (ImageView)findViewById(R.id.imageView);
        view2 = (ImageView)findViewById(R.id.imageView2);
        view2.setOnTouchListener(tEvent);


        view1.setImageBitmap(img1);
        if(isFirst){
            view2.setImageBitmap(img2);
            isFirst = false;
        }


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(a<gameInfo.time){
                    try {
                        Thread.sleep(1000);
                        a++;

                    }catch(Exception e){
                        finish();
                        break;
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
        });
        thread.start();

    }
    private void moveBck(){
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
        thread.interrupt();
        finish();
    }
    public Point getCircleXY(float x, float y, int Height, int Width, float hepx, float wipx){
        Point point = new Point();
        float a,b,c,d;
        if(Height>Width){
            c= hepx/Height;
            a =Height/ hepx ;
            b =1/c;
            d = (wipx - c*Width)/2;
            point.x = (int)(x*b - d+60);
            point.y = (int)(y*a-40);
        }else{
            c= wipx/Width;
            b = Width/wipx;
            a = 1/c;
            d = (hepx - c*Height)/2;
            point.x = (int)(x*b - 40);
            point.y = (int)(y*a - d - 60);
        }
        return point;
    }
    public Point getPicXY(float x, float y, int Height, int Width, float hepx, float wipx){
        Point point = new Point();
        float a,b,c,d;
        if(Height>Width){
            c= hepx/Height;
            a =Height/ hepx ;
            d = (wipx - c*Width)/2;
            point.x = (int)((x-d)/c);
            point.y = (int)(y*a);
        }else{
            c= wipx/Width;
            b = Width/wipx;
            d = (hepx - c*Height)/2;
            point.x = (int)(x*b);
            point.y = (int)((y - d)/c);
        }
        return point;
    }
    public View.OnTouchListener tEvent = new View.OnTouchListener() {
        @Override
               public boolean onTouch(View v, MotionEvent event){
            if(event.getAction()==MotionEvent.ACTION_DOWN) {
                x = event.getX();
                y = event.getY();
                int[] temp = new int[2];
                int sub_imgPixel;
                view2.getLocationOnScreen(temp);
                float widthpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
                float heightpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());
                Point circleP = getCircleXY(x, y, img2.getHeight(), img2.getWidth(), heightpx, widthpx);
                Point picP = getPicXY(x, y, img2.getHeight(), img2.getWidth(), heightpx, widthpx);
                if(picP.x < sub_img.getWidth() && picP.y < sub_img.getHeight()){
                    sub_imgPixel = sub_img.getPixel(picP.x, picP.y);
                }
                else {
                    sub_imgPixel = sub_img.getPixel(0,0);
                }
                int colorBlue = Color.blue(sub_imgPixel);

                if (colorBlue != 0) {
                    newImg = drawCircle(img2, circleP.x, circleP.y);
                    img2 = newImg;
                    view2.setImageBitmap(newImg);
                    sub_img = eraseIndex(sub_img, picP);
                    index++;
                } else {
                    view2.setImageBitmap(img2);
                }
                if(index==gameInfo.index){
                    moveBck();
                }
            }
            return true;
        }
        public Bitmap drawCircle(Bitmap img1, int x, int y){
            Bitmap new_img = Bitmap.createBitmap(img1.getWidth(), img1.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Bitmap circle = BitmapFactory.decodeResource(getResources(),
                    R.drawable.correct_s);
            Canvas canvas = new Canvas(new_img);
            canvas.drawBitmap(img1, new Matrix(), null);
            canvas.drawBitmap(circle, x, y, null);
            return new_img;
        }
        public Bitmap eraseIndex(Bitmap sub_img, Point pic){
            Stack<Point> stack = new Stack();
            int width = sub_img.getWidth();
            int height = sub_img.getHeight();
            int color;
            stack.push(pic);
            sub_img.setPixel(pic.x, pic.y, Color.rgb(0,0,0));
            while(!stack.empty()){
                Point point = stack.pop();
                color = sub_img.getPixel(point.x-1, point.y);
                if(Color.blue(color) != 0){
                    stack.push(new Point(point.x-1, point.y));
                    sub_img.setPixel(point.x-1, point.y, Color.rgb(0,0,0));
                }
                if (point.x+1 < width)
                    color = sub_img.getPixel(point.x+1, point.y);
                    if (Color.blue(color) != 0) {
                        stack.push(new Point(point.x+1, point.y));
                        sub_img.setPixel(point.x+1, point.y, Color.rgb(0,0,0));
                    }
                color = sub_img.getPixel(point.x, point.y-1);
                if (Color.blue(color) != 0){
                    stack.push(new Point(point.x, point.y-1));
                    sub_img.setPixel(point.x, point.y-1, Color.rgb(0,0,0));
                }
                if(point.y+1 < height){
                    color = sub_img.getPixel(point.x, point.y+1);
                    if(Color.blue(color)!=0){
                        stack.push(new Point(point.x, point.y+1));
                        sub_img.setPixel(point.x, point.y+1, Color.rgb(0,0,0));
                    }

                }

            }

            return sub_img;
        }
    };
}
