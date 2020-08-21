package com.boymask.trivia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.boymask.trivia.network.Category;
import com.boymask.trivia.network.GetDataService;
import com.boymask.trivia.network.Questions;
import com.boymask.trivia.network.RetrofitClientInstance;

import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Trivia extends AppCompatActivity {

    private TextView mTextdomanda;
    private Category category;
    private GetDataService service;
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
        category = (Category) intent.getSerializableExtra("CAT");

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        init();

        getQuestions();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_trivia, null);
        layout = view.findViewById(R.id.ll);

        setContentView(view);

        mTextdomanda = (TextView) findViewById(R.id.domanda);
    }

    private void getQuestions() {

        Call<Questions> call = service.getQuestions("10", category.getId());

        call.enqueue(new Callback<Questions>() {
            @Override
            public void onResponse(Call<Questions> call, Response<Questions> response) {
                questions = response.body();
                showQuestions();
            }

            @Override
            public void onFailure(Call<Questions> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }



    private void showQuestions() {
        List<Map> qs = questions.getQuestions();

        Map m = qs.get(current_question);
        mTextdomanda.setText((CharSequence) m.get("question"));
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
                rb[i].setText(correct);
                rg.addView(rb[i]);
                delta = 1;
            }
            rb[i + delta] = new RadioButton(this);
            rg.addView(rb[i + delta]); //the RadioButtons are added to the radioGroup instead of the layout
            rb[i + delta].setText(wrongs.get(i));
        }
        layout.addView(rg);
        layout.addView(submit);
    }

    private void createButtonResult() {
        Button result = new Button(this);

        result.setText(right ? "Correct" : "Not correct. Correct answer was : "+correct);
        result.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layout.removeView(result);
                layout.removeView(rg);

                if (current_question < questions.getQuestions().size() + 1)
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
                System.out.println("llllll");
                for (int i = 0; i <= wrongs.size(); i++) {
                    if (rb[i].isChecked()) {
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
                System.out.println("points_ " + points);

                layout.removeView(submit);
                current_question++;


            }
        });
    }
}