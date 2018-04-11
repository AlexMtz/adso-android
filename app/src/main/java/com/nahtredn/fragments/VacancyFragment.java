package com.nahtredn.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.internal.gmsg.HttpClient;
import com.nahtredn.adso.R;
import com.nahtredn.adso.VacancyActivity;
import com.nahtredn.entities.Address;
import com.nahtredn.entities.Vacancy;
import com.nahtredn.adapters.VacancyAdapter;
import com.nahtredn.utilities.Messenger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Me on 10/03/2018.
 */

public class VacancyFragment extends Fragment {
    private ListView mVacancyList;
    private VacancyAdapter mVacancyAdapter;
    private ProgressDialog pDialog;
    private String result;
    private ArrayList<Vacancy> vacancies;

    public VacancyFragment() {
        // Required empty public constructor
    }

    public static VacancyFragment newInstance(/*parámetros*/) {
        VacancyFragment fragment = new VacancyFragment();
        // Setup parámetros
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vacancy, container, false);
        mVacancyList = root.findViewById(R.id.vacancy_list);

        vacancies = new ArrayList<>();

        loadData();

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mVacancyAdapter = new VacancyAdapter(getActivity(),
                vacancies);
        mVacancyList.setAdapter(mVacancyAdapter);
    }

    /*private List<Vacancy> getVacancies(){
        ArrayList<Vacancy> vacancies = new ArrayList<>();
        Vacancy vacancy = new Vacancy("Gerente","FEMSA","$$");
        Address address = new Address("Guadalupe","Zacatecas");
        Vacancy v2 =  new Vacancy("Empleado de mostrador","WalMart","$$$");
        vacancy.setAddress(address);
        v2.setAddress(address);
        vacancies.add(vacancy);
        vacancies.add(vacancy);
        vacancies.add(vacancy);
        vacancies.add(v2);
        vacancies.add(v2);
        vacancies.add(v2);
        vacancies.add(v2);
        return vacancies;
    }*/

    public void loadData(){
        new FetchVacants().execute("https://naht-redn-dev.cloud.tyk.io/vacancies/availables");
        // new FetchVacants().execute("");
    }

    class FetchVacants extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            try {
                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.setConnectTimeout(5000);
                conection.connect();

                InputStream input = new BufferedInputStream(url.openStream());

                try {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(input, "utf-8"), 8);
                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }

                    input.close();
                    result = sBuilder.toString();

                } catch (Exception e) {
                    Log.e("VacancyFragment", "Error converting result " + e.toString());
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * After completing background task
         * **/
        @Override
        protected void onPostExecute(String file_url) {

            //parse JSON data
            try {
                JSONArray jArray = new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);
                    Vacancy vacancy = new Vacancy();
                    vacancy.setJobTitle(jObject.getString("job_title"));
                    vacancy.setCompanyName(jObject.getString("company"));
                    vacancy.setSalary(jObject.getString("salary"));
                    vacancy.setMunicipality(jObject.getString("municipality"));
                    vacancy.setState(jObject.getString("state"));
                    vacancies.add(vacancy);
                    Log.w("VacancyFragment", "vacancy fetched: " + jObject.getString("job_title"));

                } // End Loop
            } catch (JSONException e) {
                Messenger.with(getActivity()).showMessage("Error al consultar las vacantes en el servidor");
                Log.e("JSONException", "Error: " + e.toString());
            } catch (NullPointerException npe) {
                Messenger.with(getActivity()).showMessage("No se encontró ningúna vacante");
                Log.e("NullPointerException", "Error: " + npe.toString());
            }

            // System.out.println("Downloaded: " + file_url);
            pDialog.dismiss();

            // Inicializar el adaptador con la fuente de datos.
            mVacancyAdapter = new VacancyAdapter(getActivity(),
                    vacancies);

            //Relacionando la lista con el adaptador
            mVacancyList.setAdapter(mVacancyAdapter);

            // Eventos
            mVacancyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(getActivity(), VacancyActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
