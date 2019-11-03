package com.example.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int count;
    private Button countButton;
    private Button toastButton;
    private TextView counterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        count=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countButton = (Button) findViewById(R.id.button_toast);
        toastButton = (Button) findViewById(R.id.button_toast);
        counterText = (TextView) findViewById(R.id.show_count);
    }
    public void showToast(View v){
        Toast toast = Toast.makeText(this, R.string.toast_message,Toast.LENGTH_SHORT);
        toast.show();
    }

    public void countUp(View v){
        ++count;
        if (counterText != null){
            counterText.setText(Integer.toString(count));
        }
    }
}
