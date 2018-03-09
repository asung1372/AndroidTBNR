package com.aleksung.android.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private boolean[] mAnsweredCorrect;
    private boolean[] mAnsweredQuestion;
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionArray = new Question[] {
            new Question(R.string.question1, false),
            new Question(R.string.question2, false),
            new Question(R.string.question3, true),
            new Question(R.string.question4, true),
            new Question(R.string.question5, false),
            new Question(R.string.question6, true),
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        // Restoring saved data
        if (savedInstanceState != null)
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);

        // Initializing arrays
        mAnsweredCorrect = new boolean[mQuestionArray.length];
        mAnsweredQuestion = new boolean[mQuestionArray.length];

        // Retrieving resource IDs
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.prev_button);
        mQuestionTextView = findViewById(R.id.question_text_view);

        // Setting up listeners
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevQuestion();
            }
        });
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        updateQuestion();
    }

    private void prevQuestion() {
        if (mCurrentIndex == 0)
            mCurrentIndex = mQuestionArray.length;
        mCurrentIndex = (--mCurrentIndex) % mQuestionArray.length;
        updateQuestion();
    }

    private void nextQuestion() {
        mCurrentIndex = (++mCurrentIndex) % mQuestionArray.length;
        updateQuestion();
    }

    private void updateQuestion() {
        int question = mQuestionArray[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean input) {
        if (!mAnsweredQuestion[mCurrentIndex]) {
            Question question = mQuestionArray[mCurrentIndex];
            int messageID;
            if (question.isAnswerTrue() == input) {
                messageID = R.string.correct_toast;
                mAnsweredCorrect[mCurrentIndex] = true;
            } else {
                messageID = R.string.incorrect_toast;
                mAnsweredCorrect[mCurrentIndex] = false;
            }

            Toast toast = Toast.makeText(QuizActivity.this, messageID, Toast.LENGTH_SHORT);
            toast.show();
        }
        mAnsweredQuestion[mCurrentIndex] = true;
        displayFinalScore();
    }

    private void displayFinalScore() {
        if (checkAnsweredAll()) {
            double percentageScore = ((double) numberOfCorrectAnswers() / (double) mQuestionArray.length) * 100.00;
            Toast toast = Toast.makeText(QuizActivity.this,
                    "Percentage score: " + percentageScore + "%", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean checkAnsweredAll() {
        for (int i = 0; i < mQuestionArray.length; i++) {
            if (!mAnsweredQuestion[i])
                return false;
        }
        return true;
    }

    private int numberOfCorrectAnswers() {
        int sumOfCorrectAnswers = 0;
        for (int i = 0; i < mQuestionArray.length; i++) {
            if (mAnsweredCorrect[i])
                sumOfCorrectAnswers++;
        }
        return sumOfCorrectAnswers;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
}
