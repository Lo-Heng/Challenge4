package com.bignerdranch.android.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private int userAnsweredCorrect = 0;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWERED = "answered";
    private static final String KEY_CORRECT = "correct";

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            boolean answerIsAnswered[] = savedInstanceState.getBooleanArray(KEY_ANSWERED);
            for(int i = 0; i < mQuestionBank.length;i++)
            {
                mQuestionBank[i].setAnswered(answerIsAnswered[i]) ;
            }
            userAnsweredCorrect = savedInstanceState.getInt(KEY_CORRECT);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                showRecored();
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                showRecored();
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                showRecored();
            }
        });

        updateQuestion();
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        boolean answerIsAnswered[] = new boolean[mQuestionBank.length];
        for(int i = 0; i < mQuestionBank.length;i++)
        {
            answerIsAnswered[i] = mQuestionBank[i].isAnswered();

        }
        savedInstanceState.putBooleanArray(KEY_ANSWERED, answerIsAnswered);
        savedInstanceState.putInt(KEY_CORRECT, userAnsweredCorrect);
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        checkIfAnswered();
    }
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            userAnsweredCorrect++;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        mQuestionBank[mCurrentIndex].setAnswered(true);
        checkIfAnswered();
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();

    }
    //封装一个检查是否回答过问题的函数，以便调用
    private void checkIfAnswered(){
        boolean answerIsAnswered = mQuestionBank[mCurrentIndex].isAnswered();
        if (answerIsAnswered == true) {
            //如果题目被回答，则按键设置不可按下
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);

        } else {
            //如果题目没有被回答，则按键设置可按下
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }
    private void showRecored(){
        boolean allAnsnswered = true;
        String message = null;
        double correctMark = 0;//百分比形式的评分,正确率
        int correctAnswerNum = 0;//答对的题目数量
        for(int i = 0; i < mQuestionBank.length; i++){
            if(mQuestionBank[i].isAnswered() == false) {
                allAnsnswered = false;
                break;
            }
        }
        if(allAnsnswered == true){
            correctMark = (double)userAnsweredCorrect/mQuestionBank.length;
            //保留后两位
            correctMark = (double)((int)(correctMark * 10000)/100.0);
            message = "正确率" + String.valueOf(correctMark) + "%";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
