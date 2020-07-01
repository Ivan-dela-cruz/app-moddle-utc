package co.desofsi.souriapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import co.desofsi.souriapp.R;
import co.desofsi.souriapp.adapters.SpecialtyAdapter;
import co.desofsi.souriapp.data.Constant;
import co.desofsi.souriapp.models.Appointment;
import co.desofsi.souriapp.models.Specialty;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    private TextView text__doctor_and_date, text__not_time;
    private TextView text__patient, text_doctor, text_observation, text_date, text_time, text_specialty, text_type;
    private TextInputEditText observation, date_appointment;
    private Spinner specialties, doctors;
    private RadioGroup radioGroup;
    private RadioButton radio_type, radio_start;
    private Button btn_next, btn_next2, btn_confirm;
    private LinearLayout radio_grup_letf, radio_grup_right;
    private Appointment appointment;

    private TextInputLayout layout_description;

    //elemento del cardview

    private CardView cardView_one, cardView_two, cardView_three;

    //array list
    ArrayList<Specialty> lis_specialties;
    ArrayList<String> spinner_list_specialties;

    //shasred preferces
    SharedPreferences sharedPreferences;

    //JSON Array
    private JSONArray result;
    //SPECIALTY
    private Specialty specialty;
    private int position_specialty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        specialty = (Specialty) getIntent().getExtras().getSerializable("specialty");
        position_specialty = (int) getIntent().getExtras().getInt("position");
        init();
        getSpecialties();
        eventButtons();
    }

    private void init() {
        appointment = new Appointment();
        appointment.setReason("Consulta");
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        appointment.setId_specialty(0);
        text_doctor = findViewById(R.id.appointment_tvConfirmDoctorName);
        text_type = findViewById(R.id.appointment_tvConfirmType);
        text_specialty = findViewById(R.id.appointment_tvConfirmSpecialty);
        text_observation = findViewById(R.id.appointment_tvConfirmDescription);
        text_date = findViewById(R.id.appointment_tvConfirmDate);
        text_time = findViewById(R.id.appointment_tvConfirmTime);

        text__doctor_and_date = findViewById(R.id.appointment_tvSelectDoctorAndDate);
        text__not_time = findViewById(R.id.appointment_tvNotAvailableHours);

        observation = findViewById(R.id.appointment_text_description);
        date_appointment = findViewById(R.id.appointment_etScheduledDate);

        specialties = findViewById(R.id.appointment_spinnerSpecialties);
        doctors = findViewById(R.id.appointment_spinnerDoctors);

        radioGroup = findViewById(R.id.appointment_radioGroupType);
        layout_description = findViewById(R.id.appointment_layout_decription);

        //cards views
        cardView_one = findViewById(R.id.appointment_cvStep1);
        cardView_two = findViewById(R.id.appointment_cvStep2);
        cardView_three = findViewById(R.id.appointment_cvStep3);

        btn_next = findViewById(R.id.appointment_btnNext);
        btn_next2 = findViewById(R.id.appointment_btnNext2);
        btn_confirm = findViewById(R.id.appointment_btnConfirmAppointment);
        specialties.setOnItemSelectedListener(this);


    }

    public void checkedTypeAppoinment(View v) {
        int radio_button = radioGroup.getCheckedRadioButtonId();
        radio_type = findViewById(radio_button);
        appointment.setReason(radio_type.getText().toString());


    }

    public boolean validateOneCard() {


        if (appointment.getReason().equals("")) {
            return false;
        }
        if (observation.getText().toString().isEmpty()) {
            layout_description.setErrorEnabled(true);
            layout_description.setError("La descripción es obligatoria");
            return false;
        }
        if (appointment.getId_specialty() == 0) {

            return false;

        }
        return true;
    }

    public void eventButtons() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateOneCard()) {
                    cardView_one.setVisibility(View.GONE);
                    cardView_two.setVisibility(View.VISIBLE);

                }
            }
        });


        observation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!observation.getText().toString().isEmpty()) {
                    layout_description.setErrorEnabled(false);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getSpecialties() {
        lis_specialties = new ArrayList<>();
        spinner_list_specialties = new ArrayList<String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.SPECIALTIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                JSONArray array = new JSONArray(object.getString("specialties"));
                                result = new JSONArray(object.getString("specialties"));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject specialty_object = array.getJSONObject(i);

                                    Specialty specialty = new Specialty();
                                    specialty.setId(specialty_object.getInt("id"));
                                    specialty.setColor(specialty_object.getString("color"));
                                    specialty.setCreated_at(specialty_object.getString("created_at"));
                                    specialty.setDescription(specialty_object.getString("description"));
                                    specialty.setName(specialty_object.getString("name"));
                                    specialty.setStatus(specialty_object.getString("status"));
                                    specialty.setUrl_image(specialty_object.getString("url_image"));
                                    lis_specialties.add(specialty);

                                    spinner_list_specialties.add(specialty.getName());

                                }
                                specialties.setAdapter(new ArrayAdapter<>(AppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, spinner_list_specialties));
                                specialties.setSelection(position_specialty);
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println(error);

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(AppointmentActivity.this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        appointment.setId_specialty(lis_specialties.get(position).getId());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
