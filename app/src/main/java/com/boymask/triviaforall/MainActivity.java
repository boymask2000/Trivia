package com.boymask.triviaforall;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boymask.triviaforall.lists.ArgsListAdapter;
import com.boymask.triviaforall.network.Category;
import com.boymask.triviaforall.network.CategoryList;
import com.boymask.triviaforall.network.GetDataService;
import com.boymask.triviaforall.network.Questions;
import com.boymask.triviaforall.network.RetrofitClientInstance;
import com.boymask.triviaforall.network.SessionToken;

import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //17924370-829281b902bc1af726c0e6dee pixabay
    private ImageView simpleImageView;
    private GetDataService service;
    private int counterInterstitial = 0;
    //  private InterstitialAd mInterstitialAd;
    private final String interID_TEST = "ca-app-pub-3940256099942544/1033173712";
    private final String interID_PROD = "ca-app-pub-6114671792914206/9958086850";
    private final String interID = interID_TEST;
    private int level = 0;
    private String sessionToken;

    private RecyclerView rvContacts;
    private ArgsListAdapter adapter;
    private Category triviaCat;
    private Button start;
    private boolean questionFound;
    private Questions questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       /* AccountManager am = AccountManager.get(this); // "this" references the current Context

        Account[] accounts = am.getAccounts();

        Account a = accounts[0];*/

        setContentView(R.layout.activity_main);

        start = findViewById(R.id.start);

        // Create adapter passing in the sample user data
        adapter = new ArgsListAdapter(this);
        // Attach the adapter to the recyclerview to populate items
        rvContacts = findViewById(R.id.list_view0);
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        rvContacts.setNestedScrollingEnabled(false);
        getPreferences();


        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);


        getData(service);


        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (triviaCat != null)
                    startTrivia(triviaCat);
            }
        });

    }

    private void getData(GetDataService service) {

        getSessionToken(service);
    }

    private void getPreferences() {
        SharedPreferences sharedPref =
                PreferenceManager
                        .getDefaultSharedPreferences(this);
//        String v = sharedPref.getString("reply_entries", "");
//        String v1 = sharedPref.getString("reply_values", "");
        Map<String, ?> v2 = sharedPref.getAll();
        Set<String> keys = v2.keySet();
        for (String k : keys) {
            System.out.println(k);
            String val = (String) v2.get(k);
            System.out.println(val);

        }
    }


    private void getSessionToken(GetDataService service) {
        //   String now = getCurrentDate();

        Call<SessionToken> call = service.getSessionToken();

        call.enqueue(new Callback<SessionToken>() {
            @Override
            public void onResponse(Call<SessionToken> call, Response<SessionToken> response) {
                SessionToken datas = response.body();
                sessionToken = datas.getToken();

                getArgsList();
            }

            @Override
            public void onFailure(Call<SessionToken> call, Throwable t) {

            }
        });


    }

    private void getArgsList() {
        Call<CategoryList> call = service.getArguments();

        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                CategoryList args = response.body();


                // Create adapter passing in the sample user data
                ArgsListAdapter adapter = new ArgsListAdapter(MainActivity.this, args.getCategories());
                // Attach the adapter to the recyclerview to populate items
                rvContacts.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                // Set layout manager to position the items
                //  rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                //  adapter.notifyDataSetChanged();

                rvContacts.setHasFixedSize(true);

            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void setImg(Bitmap bmp) {
        simpleImageView.setImageBitmap(bmp);
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.facile:
                if (checked)
                    level = 0;
                break;
            case R.id.medio:
                if (checked)
                    level = 1;
                break;
            case R.id.difficile:
                if (checked)
                    level = 2;
                break;
        }
    }


    public void setTriviaCat(Category cat) {
        this.triviaCat = cat;
        start.setText("Start " + cat.getName());
    }

    public void startTrivia(Category cat) {

        Call<Questions> call = service.getQuestions("10", cat.getId(), getDifficoult(), sessionToken);

        call.enqueue(new Callback<Questions>() {
            @Override
            public void onResponse(Call<Questions> call, Response<Questions> response) {
                questions = response.body();
                if (checkQuestions()) {
                    questions.setCategory(cat);
                    questions.setDiffic(getDifficoult());
                    Intent intent = new Intent(MainActivity.this, Trivia.class);

                    intent.putExtra("questions", questions);
                    intent.putExtra("token", sessionToken);
                    intent.putExtra("level", level);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Questions> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    private boolean checkQuestions() {
        List<Map> qs = questions.getQuestions();
        if( qs.size()==0){
            Toast.makeText(this, "No more questions", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private String getDifficoult() {
        switch (level) {
            case 0:
                return "easy";
            case 1:
                return "medium";
            default:
                return "hard";
        }
    }
}