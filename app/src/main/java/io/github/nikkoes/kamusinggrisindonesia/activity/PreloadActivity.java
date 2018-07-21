package io.github.nikkoes.kamusinggrisindonesia.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.nikkoes.kamusinggrisindonesia.R;
import io.github.nikkoes.kamusinggrisindonesia.db.KamusHelper;
import io.github.nikkoes.kamusinggrisindonesia.model.Kamus;
import io.github.nikkoes.kamusinggrisindonesia.utils.PreferencesManager;

public class PreloadActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        ButterKnife.bind(this);

        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void> {

        KamusHelper kamusHelper;
        PreferencesManager preferencesManager;

        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            kamusHelper = new KamusHelper(getApplicationContext());
            preferencesManager = new PreferencesManager(getApplicationContext());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean firstRun = preferencesManager.getFirstTimeLoad();
            if (firstRun) {
                ArrayList<Kamus> kamusEnglish = preLoadRaw(R.raw.english_indonesia);
                ArrayList<Kamus> kamusIndonesia = preLoadRaw(R.raw.indonesia_english);

                publishProgress((int) progress);

                try {
                    kamusHelper.open();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (kamusEnglish.size() + kamusIndonesia.size());

                kamusHelper.insertTransaction(kamusEnglish, true);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.insertTransaction(kamusIndonesia, false);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.close();
                preferencesManager.setFirstTimeLoad(false);

                publishProgress((int) maxprogress);

                Log.e("List Kamus", ""+kamusEnglish.size());
            }
            else {
                try {
                    synchronized (this) {
                        this.wait(1000);
                        publishProgress(50);

                        this.wait(300);
                        publishProgress((int) maxprogress);
                    }
                }
                catch (Exception e) {

                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(PreloadActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public ArrayList<Kamus> preLoadRaw(int data) {
        ArrayList<Kamus> listKamus = new ArrayList<>();
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(data);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            String line = null;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");
                Kamus kamus;
                kamus = new Kamus(splitstr[0], splitstr[1]);
                listKamus.add(kamus);
            } while (line != null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return listKamus;
    }
}
