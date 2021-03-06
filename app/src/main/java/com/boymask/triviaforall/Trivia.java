package com.boymask.triviaforall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.boymask.triviaforall.network.Category;
import com.boymask.triviaforall.network.GetDataService;
import com.boymask.triviaforall.network.Questions;
import com.boymask.triviaforall.network.RetrofitClientInstance;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Trivia extends AppCompatActivity {

    private TextView mTextdomanda;
    private TextView mTextargval;
    private TextView mTextLevel;
    private TextView mTextCurrQuestion;
    private TextView mTextTotalQuestion;

    private TextView mTextRightReplies;
    private TextView mTextWrongReplies;

    private ViewGroup layout;
    private Questions questions;
    private int current_question = 0;
    private int points = 0;
    private int numRight = 0;
    private int numWrong = 0;
    private RadioGroup rg;
    private Button submit;
    private boolean right = false;
    private String correct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        questions = (Questions) intent.getSerializableExtra("questions");

        init();

        showQuestions();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_trivia, null);
        layout = view.findViewById(R.id.ll);

        setContentView(view);

        mTextdomanda = (TextView) findViewById(R.id.domanda);
        mTextargval = (TextView) findViewById(R.id.argval);
        mTextLevel = (TextView) findViewById(R.id.levelval);
        mTextCurrQuestion = (TextView) findViewById(R.id.questionval);
        mTextTotalQuestion = (TextView) findViewById(R.id.questiontotal);

        mTextRightReplies = (TextView) findViewById(R.id.rightval);
        mTextWrongReplies = (TextView) findViewById(R.id.wrongval);
        mTextRightReplies.setText("" + numRight);
        mTextWrongReplies.setText("" + numWrong);

        mTextargval.setText(questions.getCategory().getName());
        mTextLevel.setText(questions.getDiffic());
    }


    private void showQuestions() {
        List<Map> qs = questions.getQuestions();

        mTextCurrQuestion.setText("" + (current_question + 1));
        mTextTotalQuestion.setText("" + qs.size());
        Map m = qs.get(current_question);

        String questionText = (String) m.get("question");
        String result = null;
        try {
            //  result = java.net.URLDecoder.decode(questionText, StandardCharsets.UTF_8.name());
            result = getString(questionText);
        } catch (Exception e) {
            e.printStackTrace();
        }


        mTextdomanda.setText(result);
        List<String> wrongs = (List<String>) m.get("incorrect_answers");

        final RadioButton[] rb = new RadioButton[wrongs.size() + 1];
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        int randN = rand.nextInt(wrongs.size());

        correct = (String) m.get("correct_answer");
        createButtonSubmit(questions, wrongs, rb, randN);


        rg = new RadioGroup(this); //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);
        int delta = 0;


        for (int i = 0; i < wrongs.size(); i++) {
            if (i == randN) {
                rb[i] = new RadioButton(this);
                rb[i].setText(getString(correct));
                rg.addView(rb[i]);
                delta = 1;
            }
            rb[i + delta] = new RadioButton(this);
            rg.addView(rb[i + delta]); //the RadioButtons are added to the radioGroup instead of the layout
            rb[i + delta].setText(getString(wrongs.get(i)));
        }
        layout.addView(rg);
        layout.addView(submit);
    }

    private String getString(String questionText) {
        byte[] res = Base64.getDecoder().decode(questionText);
        return new String(res);

    }

    private void createButtonResult() {
        Button result = new Button(this);

        result.setText(right ? "Correct" : "Not correct. Correct answer was : " + getString(correct));
        result.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layout.removeView(result);
                layout.removeView(rg);

                if (current_question < questions.getQuestions().size())
                    showQuestions();
                else finish();
            }
        });
        layout.addView(result);
    }

    private void createButtonSubmit(Questions questions, List<String> wrongs, RadioButton[] rb, int randN) {
        submit = new Button(this);
        right = false;
        submit.setText("Ok");
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean checked = false;
                for (int i = 0; i <= wrongs.size(); i++) {
                    if (rb[i].isChecked()) {
                        checked = true;
                        if (i == randN) {
                            points++;
                            numRight++;
                            right = true;
                            createButtonResult();
                        } else {
                            points--;
                            numWrong++;
                            createButtonResult();
                        }
                    }
                }
                if (!checked) {
                    //Toast.makeText(Trivia.this, "Select one", Toast.LENGTH_LONG).show();
                } else {
                    mTextRightReplies.setText("" + numRight);
                    mTextWrongReplies.setText("" + numWrong);
                    System.out.println("points_ " + points);

                    layout.removeView(submit);
                    current_question++;
                }

            }
        });
    }
}