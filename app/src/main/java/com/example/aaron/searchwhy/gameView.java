package com.example.aaron.searchwhy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    }
    private void moveBck(){
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
        finish();
    }
}
