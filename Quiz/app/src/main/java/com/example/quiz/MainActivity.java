package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button falseButton;
    private Button trueButton;
    private TextView questionTextView;
    private ImageButton nextButton;
    private Question[] questions = new Question[]{
            new Question(R.string.question1, true),
            new Question(R.string.question2, true),
            new Question(R.string.question3, true)
    };

    private int questionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Question question = new Question(R.id.question_text_box, true);

        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        nextButton = findViewById(R.id.next_question);
        questionTextView = findViewById(R.id.question_text_box);

        // to set this up you need to implement onclicklistener interface
        falseButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.false_button:
                checkAnswer(true);
                Toast.makeText(MainActivity.this, "False", Toast.LENGTH_LONG).show();
                break;
            case R.id.true_button:
                checkAnswer(false);
                Toast.makeText(MainActivity.this, "True", Toast.LENGTH_LONG).show();
                break;
            case R.id.next_question:
                questionIndex++;
                if (questionIndex == questions.length){
                    questionIndex = 0;
                }
                updateQuestion();


        }
    }

    private void updateQuestion(){
        Log.d("current","onClick " + questionIndex);
        questionTextView.setText(questions[questionIndex].getAnswerId());
    }

    private void checkAnswer(boolean userChooseCorrectAnswer){
        boolean answerTrue = questions[questionIndex].isAnswerTrue();
        int answer = 0;

        if(userChooseCorrectAnswer == answerTrue){
            answer = R.string.False;
        }else {
            answer = R.string.True;
        }
        Toast.makeText(MainActivity.this, answer, Toast.LENGTH_SHORT).show();
    }
}
