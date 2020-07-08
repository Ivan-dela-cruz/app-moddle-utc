package co.desofsi.souriapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import co.desofsi.souriapp.R;
import co.desofsi.souriapp.adapters.SpecialtyAdapter;
import co.desofsi.souriapp.data.Constant;
import co.desofsi.souriapp.init.UserProfileActivity;
import co.desofsi.souriapp.models.Appointment;
import co.desofsi.souriapp.models.Doctor;
import co.desofsi.souriapp.models.Specialty;
import co.desofsi.souriapp.models.TimeAvailable;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity {

    private TextView text__doctor_and_date, text__not_time;
    private TextView text__patient, text_doctor, text_observation, text_date, text_time, text_specialty, text_type;
    private TextInputEditText observation, date_appointment;
    private Spinner specialties, doctors;
    private RadioGroup radioGroup, radioGroup_times;
    private RadioButton radio_type, radio_start;
    private Button btn_next, btn_next2, btn_confirm;
    private LinearLayout radio_grup_letf, radio_grup_right;
    private Appointment appointment;

    private TextInputLayout layout_description;

    //elemento del cardview

    private CardView cardView_one, cardView_two, cardView_three;

    //ARRAY LIST SPECIALTIES AND DOCTORS
    ArrayList<Specialty> lis_specialties;
    ArrayList<String> spinner_list_specialties;
    ArrayList<Doctor> list_doctors;
    ArrayList<String> spinner_list_doctors;


    //shasred preferces
    SharedPreferences sharedPreferences;

    //JSON Array
    private JSONArray result, result_doctors, result_times;
    //SPECIALTY
    private Specialty specialty;
    private int position_specialty;
    //MEDIC
    private Doctor doctor;
    private int position_doctor;
    //TIMES
    private TimeAvailable timeAvailable;
    private ArrayList<TimeAvailable> list_times;
    ///fechas
    private int dia, mes, anio;
    Calendar calendar;
    //TEXT DOCTOR AND SPECIALTY and date
    String specialty_selected, doctor_selected, date_selected;
    //PROGRES DIALOG
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        specialty = (Specialty) getIntent().getExtras().getSerializable("specialty");
        position_specialty = (int) getIntent().getExtras().getInt("position");
        init();

        getDoctors();
        getSpecialties();

        eventButtons();
    }

    private void init() {
        appointment = new Appointment();
        appointment.setReason("Consulta");
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        appointment.setId_specialty(0);
        calendar = Calendar.getInstance();

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
        radioGroup_times = findViewById(R.id.appointment_radioGroupTimes);
        layout_description = findViewById(R.id.appointment_layout_decription);

        //cards views
        cardView_one = findViewById(R.id.appointment_cvStep1);
        cardView_two = findViewById(R.id.appointment_cvStep2);
        cardView_three = findViewById(R.id.appointment_cvStep3);

        btn_next = findViewById(R.id.appointment_btnNext);
        btn_next2 = findViewById(R.id.appointment_btnNext2);
        btn_confirm = findViewById(R.id.appointment_btnConfirmAppointment);
        specialties.setOnItemSelectedListener(new SpecialtySpinnerClass());
        doctors.setOnItemSelectedListener(new DoctorSpinnerClass());
        date_appointment.setText(dateToday());
        date_selected = dateToday();
        appointment.setDate(date_selected);
        dialog = new ProgressDialog(AppointmentActivity.this);
        dialog.setCancelable(false);

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

    public String dateToday() {
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH) + 1;
        anio = calendar.get(Calendar.YEAR);
        String date = "2020/10/10";
        if (mes < 10 && dia < 10) {
            date = anio + "/0" + mes + "/0" + dia;
        } else if (mes > 10 && dia < 10) {
            date = anio + "/" + mes + "/0" + dia;
        } else if (mes < 10 && dia > 10) {
            date = anio + "/0" + mes + "/" + dia;
        } else {
            date = anio + "/" + mes + "/" + dia;
        }
        return date;
    }

    public void eventButtons() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateOneCard()) {
                    cardView_one.setVisibility(View.GONE);
                    cardView_two.setVisibility(View.VISIBLE);
                    appointment.setObservation(observation.getText().toString().trim());

                }
            }
        });
        btn_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int radio_time = radioGroup_times.getCheckedRadioButtonId();
                    RadioButton radioButton_time = findViewById(radio_time);
                    String slect_time = radioButton_time.getText().toString().trim();
                    if (!slect_time.isEmpty()) {
                        cardView_two.setVisibility(View.GONE);
                        cardView_three.setVisibility(View.VISIBLE);
                        appointment.setStart(slect_time);
                        loadDescriptionAppoimet();
                    }
                } catch (Exception e) {
                    Toast.makeText(AppointmentActivity.this, "Selecciona la hora de la cita", Toast.LENGTH_SHORT).show();
                }

                // Toast.makeText(AppointmentActivity.this, "" + radioButton_time.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postAppointment();
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
        date_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AppointmentActivity.this
                        , new DateApointmentPicker(),
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    public void loadDescriptionAppoimet() {

        text_observation.setText(appointment.getObservation());
        text_type.setText(appointment.getReason());
        text_date.setText(appointment.getDate());
        text_time.setText(appointment.getStart());
        text_doctor.setText(doctor_selected);
        text_specialty.setText(specialty_selected);



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

    private void getDoctors() {
        list_doctors = new ArrayList<>();
        spinner_list_doctors = new ArrayList<String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.DOCTORS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                JSONArray array = new JSONArray(object.getString("doctors"));
                                result_doctors = new JSONArray(object.getString("doctors"));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject specialty_object = array.getJSONObject(i);

                                    Doctor doctor_r = new Doctor();
                                    doctor_r.setId(specialty_object.getInt("id"));
                                    doctor_r.setName(specialty_object.getString("name"));
                                    doctor_r.setLast_name(specialty_object.getString("last_name"));
                                    doctor_r.setAcademy_title(specialty_object.getString("academic_title"));
                                    doctor_r.setBiography(specialty_object.getString("biography"));
                                    doctor_r.setUrl_image(specialty_object.getString("url_image"));
                                    list_doctors.add(doctor_r);

                                    spinner_list_doctors.add(doctor_r.getName() + " " + doctor_r.getLast_name());
                                    System.out.println("DOCTOR" + doctor_r.getLast_name() + " " + doctor_r.getName());

                                }
                                doctors.setAdapter(new ArrayAdapter<>(AppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, spinner_list_doctors));
                                doctors.setSelection(position_doctor);
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

    public void postAppointment() {
        dialog.setMessage("Actualizando");
        dialog.show();
        final String description = appointment.getObservation();
        final String reason = appointment.getReason();
        final String id_specialty = String.valueOf(appointment.getId_specialty());
        final String id_doctor = String.valueOf(appointment.getId_doctor());
        final String date = appointment.getDate() + " 00:00:00";
        final String start = appointment.getDate() + " " + appointment.getStart()+":00";
        final String end = start;
        final String status = "Pendiente";

        System.out.println(description);
        System.out.println(reason);
        System.out.println(id_specialty);
        System.out.println(id_doctor);
        System.out.println(date);
        System.out.println(start);
        System.out.println(end);
        System.out.println(status);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.REQUEST_APPOINTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {

                                startActivity(new Intent(AppointmentActivity.this, HomeActivity.class));
                                finish();
                                Toast.makeText(AppointmentActivity.this, "Actualizado", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(AppointmentActivity.this, "Error al guardar", Toast.LENGTH_LONG).show();
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id_doctor", id_doctor);
                map.put("id_specialty", id_specialty);
                map.put("date", date);
                map.put("start", start);
                map.put("end", end);
                map.put("reason", reason);
                map.put("observation", description);
                map.put("status", status);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AppointmentActivity.this);
        requestQueue.add(stringRequest);
    }

    class SpecialtySpinnerClass implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(view.getContext(), "Your choose :" + lis_specialties.get(i).getColor(),Toast.LENGTH_SHORT).show();
            appointment.setId_specialty(lis_specialties.get(i).getId());
            specialty_selected = lis_specialties.get(i).getName();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class DoctorSpinnerClass implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(view.getContext(), "Your choose :" + lis_specialties.get(i).getColor(),Toast.LENGTH_SHORT).show();
            appointment.setId_doctor(list_doctors.get(i).getId());
            String date_select = date_appointment.getText().toString().trim();
            DateApointmentPicker dateApointmentPicker = new DateApointmentPicker();
            dateApointmentPicker.getTimes(date_select, appointment.getId_specialty(), appointment.getId_doctor());
            doctor_selected = list_doctors.get(i).getName() + " " + list_doctors.get(i).getLast_name();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class DateApointmentPicker implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            date_selected = dateFormato(i, i1 + 1, i2);
            date_appointment.setText(date_selected);
            appointment.setDate(date_selected);
            getTimes(date_selected, appointment.getId_specialty(), appointment.getId_doctor());


        }

        public String dateFormato(int year, int month, int day) {
            String date = "2020/10/10";
            if (month < 10 && day < 10) {
                date = year + "/0" + month + "/0" + day;
            } else if (month > 10 && day < 10) {
                date = year + "/" + month + "/0" + day;
            } else if (month < 10 && day > 10) {
                date = year + "/0" + month + "/" + day;
            } else {
                date = year + "/" + month + "/" + day;
            }
            return date;
        }

        private void getTimes(String date, int id_specialty, int id_doctor) {
            list_times = new ArrayList<>();
            list_times.clear();
            final String date_selec = date.trim();
            final String id_specialty_selec = String.valueOf(id_specialty).trim();
            final String id_doctor_selec = String.valueOf(id_doctor).trim();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.TIMES,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject object = new JSONObject(response);
                                if (object.getBoolean("success")) {

                                    JSONArray array = new JSONArray(object.getString("times"));
                                    result_times = new JSONArray(object.getString("times"));

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject specialty_object = array.getJSONObject(i);

                                        TimeAvailable time_r = new TimeAvailable();
                                        time_r.setId(specialty_object.getInt("id"));
                                        time_r.setDate(specialty_object.getString("date"));
                                        time_r.setEnd(specialty_object.getString("end"));
                                        time_r.setStart(specialty_object.getString("start"));
                                        list_times.add(time_r);

                                    }
                                    clearRadioGroup();
                                    loadTimesAppointment();
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

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id_doctor", id_doctor_selec);
                    map.put("id_specialty", id_specialty_selec);
                    map.put("date", date_selec);
                    return map;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(AppointmentActivity.this);
            requestQueue.add(stringRequest);
        }

        public void loadTimesAppointment() {

            int times_avalible = 36;
            int init_time = 9;
            int interval = 15;
            int cont = 0;
            String hour = "00:00:00";
            for (int i = 1; i <= times_avalible; i++) {

                if (cont == 60) {
                    cont = 0;
                    init_time = init_time + 1;
                }


                if (init_time < 10) {
                    if (cont < 10) {
                        hour = "0" + init_time + ":0" + cont;
                    } else {
                        hour = "0" + init_time + ":" + cont;
                    }

                } else {
                    if (cont < 10) {
                        hour = init_time + ":0" + cont;
                    } else {
                        hour = init_time + ":" + cont;
                    }
                }

                hour = hourcollapse(hour);

                if (!hour.equals("")) {
                    if (init_time < 13) {
                        RadioButton radioButton = new RadioButton(AppointmentActivity.this);
                        radioButton.setId(i);
                        radioButton.setText(hour);
                        radioGroup_times.addView(radioButton);
                    }
                    if (init_time > 14) {
                        RadioButton radioButton = new RadioButton(AppointmentActivity.this);
                        radioButton.setId(i);
                        radioButton.setText(hour);
                        radioGroup_times.addView(radioButton);
                    }
                }

                cont = cont + interval;


            }
        }
    }

    public String hourcollapse(String time) {
        String hour = "";
        for (TimeAvailable time_av : list_times) {
            String time_dis = time_av.getStart().substring(11, 16);
            // System.out.println("=>>" + time_dis + " =>>" + time);
            if (time_dis.equals(time)) {
                list_times.remove(time_av);
                // System.out.println("=>>TAMAÑO LISTA" + list_times.size());
                return hour;
            }

        }
        return time;
    }

    public void clearRadioGroup() {
        radioGroup_times.removeAllViews();

    }
}
