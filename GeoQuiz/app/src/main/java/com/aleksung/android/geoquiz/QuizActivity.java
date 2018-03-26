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

    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String TAG = "QuizActivity";
//    private static final String KEY_ARRAY_ANSWERED = "array_answered";
//    private static final String KEY_ARRAY_CORRECT = "array_correct";
//    private static final String KEY_ARRAY_CHEATED = "cheated";
    private static final String KEY_INDEX = "index";

    private enum QuestionStatus {
        UNANSWERED, ANSWERED_CORRECTLY, ANSWERED_INCORRECTLY, CHEATED
    }

    private QuestionStatus[] mQuestionStatuses;
//     private QuizBooleanArray mAnsweredQuestionParcel;;
//     private QuizBooleanArray mAnsweredCorrectParcel;
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
        Button cheatButton;
        ImageButton nextButton;
        ImageButton prevButton;

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) { // Restoring saved data
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnsweredQuestionParcel = savedInstanceState.getParcelable(KEY_ARRAY_ANSWERED);
            mAnsweredCorrectParcel = savedInstanceState.getParcelable(KEY_ARRAY_CORRECT);

            // TODO: Need to make question status class
            mAnsweredQuestion = mAnsweredQuestionParcel.getData();
            mAnsweredCorrect = mAnsweredCorrectParcel.getData();
        } else { // Initializing arrays
//            mAnsweredCorrect = new boolean[mQuestionArray.length];
//            mAnsweredQuestion = new boolean[mQuestionArray.length];
            mQuestionStatuses = new QuestionStatus[mQuestionArray.length];
        }

        // Retrieving resource IDs
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        cheatButton = findViewById(R.id.cheat_button);
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
        cheatButton.setOnClickListener(new View.OnClickListener() {
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
            // mCheated = CheatActivity.wasAnswerShown(data);
            if (CheatActivity.wasAnswerShown(data)) {
                mQuestionStatuses[mCurrentIndex] = QuestionStatus.CHEATED;
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

        // TODO: Need to make question status class
        mAnsweredQuestionParcel = new QuizBooleanArray(mAnsweredQuestion);
        mAnsweredCorrectParcel = new QuizBooleanArray(mAnsweredCorrect);

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putParcelable(KEY_ARRAY_ANSWERED, mAnsweredQuestionParcel);
        savedInstanceState.putParcelable(KEY_ARRAY_CORRECT, mAnsweredCorrectParcel);
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
        int messageID;
        if (mQuestionStatuses[mCurrentIndex] == QuestionStatus.UNANSWERED) {
            Question question = mQuestionArray[mCurrentIndex];
            if (question.isAnswerTrue() == input) {
                messageID = R.string.correct_toast;
                mQuestionStatuses[mCurrentIndex] = QuestionStatus.ANSWERED_CORRECTLY;
            } else {
                messageID = R.string.incorrect_toast;
                mQuestionStatuses[mCurrentIndex] = QuestionStatus.ANSWERED_INCORRECTLY;
            }
        } else { // mQuestionStatuses[mCurrentIndex] == QuestionStatus.CHEATED
            messageID = R.string.judgement_toast;
        }
        Toast toast = Toast.makeText(QuizActivity.this, messageID, Toast.LENGTH_SHORT);
        toast.show();

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
            if (mQuestionStatuses[i] == QuestionStatus.UNANSWERED)
                return false;
        }
        return true;
    }

    private int numberOfCorrectAnswers() {
        int sumOfCorrectAnswers = 0;
        for (int i = 0; i < mQuestionArray.length; i++) {
            if (mQuestionStatuses[i] == QuestionStatus.ANSWERED_CORRECTLY)
                sumOfCorrectAnswers++;
        }
        return sumOfCorrectAnswers;
    }

}
