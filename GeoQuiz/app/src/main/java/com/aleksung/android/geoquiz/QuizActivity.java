package com.aleksung.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final int MAX_CHEAT_COUNT = 3;
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String TAG = "QuizActivity";
    private static final String KEY_QUESTION_STATUSES = "question_statuses";
    private static final String KEY_INDEX = "index";

    private Button mCheatButton;
    private QuestionStatusArray mQuestionStatuses;
    private TextView mCheatCountTextView;
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
            mQuestionStatuses = savedInstanceState.getParcelable(KEY_QUESTION_STATUSES);
        } else { // Initializing arrays
            mQuestionStatuses = new QuestionStatusArray(mQuestionArray.length);
        }

        // Retrieving resource IDs
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        mCheatButton = findViewById(R.id.cheat_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        mQuestionTextView = findViewById(R.id.question_text_view);
        mCheatCountTextView = findViewById(R.id.cheat_count_text_view);

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
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Cheat Activity
                boolean answerIsTrue = mQuestionArray[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
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
        updateCheatCount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            if (CheatActivity.wasAnswerShown(data) && mQuestionStatuses.unanswered(mCurrentIndex)) {
                mQuestionStatuses.setCheated(mCurrentIndex);
                updateCheatCount();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called from QuizActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called from QuizActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called from QuizActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called from QuizActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called from QuizActivity");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState from QuizActivity");

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putParcelable(KEY_QUESTION_STATUSES, mQuestionStatuses);
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

    private void updateCheatCount() {
        // Counting cheats
        int count = 0;
        for (int i = 0; i < mQuestionArray.length; i++) {
            if (mQuestionStatuses.cheated(i))
                count++;
            if (count >= MAX_CHEAT_COUNT)
                break;
        }
        int cheatsLeft = (MAX_CHEAT_COUNT - count);

        // Setting cheat TextView
        String text = "Number of cheats left: " + cheatsLeft;
        mCheatCountTextView.setText(text);

        // Setting cheat button
        if (cheatsLeft <= 0)
            mCheatButton.setEnabled(false);
    }

    private void updateQuestion() {
        int question = mQuestionArray[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean input) {
        int messageID;
        if (mQuestionStatuses.unanswered(mCurrentIndex)) {
            Question question = mQuestionArray[mCurrentIndex];
            if (question.isAnswerTrue() == input) {
                messageID = R.string.correct_toast;
                mQuestionStatuses.setAnsweredCorrectly(mCurrentIndex);
            } else {
                messageID = R.string.incorrect_toast;
                mQuestionStatuses.setAnsweredIncorrectly(mCurrentIndex);
            }
            Toast toast = Toast.makeText(QuizActivity.this, messageID, Toast.LENGTH_SHORT);
            toast.show();
        } else if (mQuestionStatuses.cheated(mCurrentIndex)){
            messageID = R.string.judgement_toast;
            Toast toast = Toast.makeText(QuizActivity.this, messageID, Toast.LENGTH_SHORT);
            toast.show();
        }

        // Checking if all questions answered
        if (answeredAll())
            displayFinalScore();
    }

    private void displayFinalScore() {
        double percentageScore = ((double) numberOfCorrectAnswers() /
                (double) mQuestionArray.length) * 100.00;
        Toast toast = Toast.makeText(QuizActivity.this,
                "Percentage score: " + percentageScore + "%", Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean answeredAll() {
        for (int i = 0; i < mQuestionArray.length; i++) {
            if (mQuestionStatuses.unanswered(i))
                return false;
        }
        return true;
    }

    private int numberOfCorrectAnswers() {
        int sumOfCorrectAnswers = 0;
        for (int i = 0; i < mQuestionArray.length; i++) {
            if (mQuestionStatuses.answeredCorrectly(i))
                sumOfCorrectAnswers++;
        }
        return sumOfCorrectAnswers;
    }

}
