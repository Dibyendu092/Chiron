package com.evolver.chiron.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.evolver.chiron.R;
import com.evolver.chiron.databinding.ActivityUserCovidCountryBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class UserCovidCountryActivity extends AppCompatActivity {

    ActivityUserCovidCountryBinding binding;

    private String selectedCountry;
    private ArrayAdapter<CharSequence> countryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserCovidCountryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        countryAdapter = ArrayAdapter.createFromResource(this, R.array.array_country,R.layout.spinner_layout);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCountry.setAdapter(countryAdapter);

        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = binding.spinnerCountry.getSelectedItem().toString();
                binding.tvCountry.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserCovidCountryActivity.super.onBackPressed();
            }
        });

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCountry.equals("Select Your Country")) {
                    Toast.makeText(UserCovidCountryActivity.this, "Please select your country from the list", Toast.LENGTH_LONG).show();
                    binding.tvCountry.setError("Country is required!");
                    binding.detailsContainer.setVisibility(View.INVISIBLE);
                    binding.tvCountry.requestFocus();
                }else{
                    binding.tvCountry.setError(null);
                    apiCall(selectedCountry);
                }
            }
        });
    }

    private void apiCall(String countryName) {
        String URL = "https://disease.sh/v3/covid-19/countries/"+countryName;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    binding.detailsContainer.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    binding.GlobalStat.setText(countryName+"'s Statistics");
                    binding.Casesno.setText(jsonObject.getString("cases"));
                    binding.TodayCasesno.setText(jsonObject.getString("todayCases"));
                    binding.Deathno.setText(jsonObject.getString("deaths"));
                    binding.TodayDeathNo.setText(jsonObject.getString("todayDeaths"));
                    binding.RecoveryNo.setText(jsonObject.getString("recovered"));
                    binding.Activeno.setText(jsonObject.getString("active"));
                    binding.CriticalNo.setText(jsonObject.getString("critical"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserCovidCountryActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}