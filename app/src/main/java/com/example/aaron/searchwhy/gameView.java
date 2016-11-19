package com.example.aaron.searchwhy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class gameView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        Button buttonBck = (Button)findViewById(R.id.buttonBck);
        buttonBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveBck();
            }
        });
        Intent img = getIntent();
        Bitmap img1 = (Bitmap)img.getParcelableExtra("img1");
        Bitmap img2 = (Bitmap)img.getParcelableExtra("img2");
        ImageView view1 = (ImageView)findViewById(R.id.imageView);
        ImageView view2 = (ImageView)findViewById(R.id.imageView2);
        view1.setImageBitmap(img1);
        view2.setImageBitmap(img2);

    }
    private void moveBck(){
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
        finish();
    }
}
