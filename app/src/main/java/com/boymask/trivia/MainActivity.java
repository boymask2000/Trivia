package com.boymask.trivia;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boymask.trivia.lists.ArgsListAdapter;
import com.boymask.trivia.network.Category;
import com.boymask.trivia.network.CategoryList;
import com.boymask.trivia.network.GetDataService;
import com.boymask.trivia.network.RetrofitClientInstance;
import com.boymask.trivia.network.SessionToken;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start=findViewById(R.id.start);

        // Create adapter passing in the sample user data
        adapter = new ArgsListAdapter(this);
        // Attach the adapter to the recyclerview to populate items
        rvContacts = findViewById(R.id.list_view0);
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        rvContacts.setNestedScrollingEnabled(false);
        getPreferences();

        // Menu Inizio ---------------------------------------------------------------------
   /*     Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


        // Menu fine
        // Pubblicita Inizio ----------------------------------------------------------------
      /*  MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(interID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());*/
// Pubblicita fine

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        //   simpleImageView = (ImageView) findViewById(R.id.imageview);

        getData(service);

     /*   final Button button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                getImage(service);
            }
        });*/
start.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {
        if(triviaCat!=null)
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
        String now = getCurrentDate();

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


                //    RecyclerView rvContacts = (RecyclerView) findViewById(R.id.list_view);


                // Create adapter passing in the sample user data
                ArgsListAdapter adapter = new ArgsListAdapter(MainActivity.this, args.getCategories());
                // Attach the adapter to the recyclerview to populate items
                rvContacts.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                // Set layout manager to position the items
                //  rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                //  adapter.notifyDataSetChanged();

                rvContacts.setHasFixedSize(true);

                rvContacts.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                    @Override
                    public void onTouchEvent(RecyclerView recycler, MotionEvent event) {
                        // Handle on touch events here
                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView recycler, MotionEvent event) {
                        return false;
                    }

                });
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    private String getCurrentDate() {
        // 2020-03-01T00:00:00Z

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());
        return date + "T00:00:00Z";

    }

    private void show(String embed) {
        ImageDownloader runner = new ImageDownloader(embed, simpleImageView, this);
        runner.execute("");
    }

    public void setImg(Bitmap bmp) {
        simpleImageView.setImageBitmap(bmp);
    }



    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
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

    public void startTrivia(Category cat) {
        Intent intent = new Intent(this, Trivia.class);


        intent.putExtra("CAT", cat);
        intent.putExtra("token", sessionToken);
        intent.putExtra("level", level);
        startActivity(intent);

    }

    public void setTriviaCat(Category cat) {
        this.triviaCat=cat;
        start.setText("Start "+cat.getName());
    }

}