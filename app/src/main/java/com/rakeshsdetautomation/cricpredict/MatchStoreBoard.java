package com.rakeshsdetautomation.cricpredict;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MatchStoreBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_store_board);

        TextView hello_word = (TextView) findViewById(R.id.scoreboard_match_id_text_view);
        hello_word.setText("Hello ---" + getIntent().getStringExtra("matchId"));
    }
}