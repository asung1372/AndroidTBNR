package com.aleksung.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String KEY_ANSWER_SHOWN = "answer_shown";
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.aleksung.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.aleksung.android.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;
    private boolean mAnswerShown; // for onSaveInstanceState(Bundle);

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button showAnswerButton;
        final TextView answerTextView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN);
        } else {
            mAnswerShown = false;
        }
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        // Retrieving resource IDs
        showAnswerButton = findViewById(R.id.show_answer_button);
        answerTextView = findViewById(R.id.answer_text_view);

        if (mAnswerShown)
            answerTextView.setText("The answer is: " + mAnswerIsTrue);

        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerTextView.setText("The answer is: " + mAnswerIsTrue);
                setAnswerShownResult(true);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mAnswerShown);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        mAnswerShown = true;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
