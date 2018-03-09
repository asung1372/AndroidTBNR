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
    private static final String KEY_ARRAY_ANSWERED = "array_answered";
    private static final String KEY_ARRAY_CORRECT = "array_correct";

    private boolean[] mAnsweredQuestion;
    private boolean[] mAnsweredCorrect;
    private QuizBooleanArray mAnsweredQuestionParcel;
    private QuizBooleanArray mAnsweredCorrectParcel;
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
        Button trueButton;
        Button falseButton;
        ImageButton nextButton;
        ImageButton prevButton;

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) { // Restoring saved data
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnsweredQuestionParcel = savedInstanceState.getParcelable(KEY_ARRAY_ANSWERED);
            mAnsweredCorrectParcel = savedInstanceState.getParcelable(KEY_ARRAY_CORRECT);

            mAnsweredQuestion = mAnsweredQuestionParcel.getData();
            mAnsweredCorrect = mAnsweredCorrectParcel.getData();
        } else { // Initializing arrays
            mAnsweredCorrect = new boolean[mQuestionArray.length];
            mAnsweredQuestion = new boolean[mQuestionArray.length];
        }

        // Retrieving resource IDs
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        mQuestionTextView = findViewById(R.id.question_text_view);

        // Setting up listeners
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
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
        if (answeredAll())
            displayFinalScore();
    }

    private void displayFinalScore() {
        double percentageScore = ((double) numberOfCorrectAnswers() / (double) mQuestionArray.length) * 100.00;
        Toast toast = Toast.makeText(QuizActivity.this,
                "Percentage score: " + percentageScore + "%", Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean answeredAll() {
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

        mAnsweredQuestionParcel = new QuizBooleanArray(mAnsweredQuestion);
        mAnsweredCorrectParcel = new QuizBooleanArray(mAnsweredCorrect);

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putParcelable(KEY_ARRAY_ANSWERED, mAnsweredQuestionParcel);
        savedInstanceState.putParcelable(KEY_ARRAY_CORRECT, mAnsweredCorrectParcel);
    }
}
