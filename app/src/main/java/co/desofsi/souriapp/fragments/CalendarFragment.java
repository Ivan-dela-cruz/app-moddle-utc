package co.desofsi.souriapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.desofsi.souriapp.R;
import co.desofsi.souriapp.activities.HomeActivity;
import co.desofsi.souriapp.adapters.MyAppointmentAdapter;
import co.desofsi.souriapp.adapters.MyAppointmentHistoryAdapter;
import co.desofsi.souriapp.adapters.MyTreatmentAdapter;
import co.desofsi.souriapp.adapters.SpecialtyAdapter;
import co.desofsi.souriapp.data.Constant;
import co.desofsi.souriapp.models.Appointment;
import co.desofsi.souriapp.models.AppointmentDescription;
import co.desofsi.souriapp.models.Specialty;
import co.desofsi.souriapp.models.Treatment;

public class CalendarFragment extends Fragment {
    private View view;
    private ArrayList<Specialty> lis_specialties;
    private SwipeRefreshLayout refreshLayout;
    private SpecialtyAdapter specialtyAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    ///atrututos del perfil

    private TextView name_user;
    //MY APPOINTMENTS
    private ArrayList<AppointmentDescription> list_appointments;
    private RecyclerView my_appointments_recycler;
    ///json array
    JSONArray array, array_my_appointments;
    private RelativeLayout empty;
    public CalendarFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        init();
        return view;
    }


    public void init() {
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        empty = view.findViewById(R.id.vacio);

        refreshLayout = view.findViewById(R.id.home_fragment_swipe);
        toolbar = view.findViewById(R.id.home_fragment_toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ///INICIAR ATRIBUTOS DEL PERFIL
        name_user = view.findViewById(R.id.fragment_home_text_user_name);

        ///RECYCLER MY_APPOINTMEST INIT
        my_appointments_recycler = view.findViewById(R.id.home_fragment_recycler_my_appointments);
        my_appointments_recycler.setHasFixedSize(true);
        LinearLayoutManager LayoutManagaer_my_appointments = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        my_appointments_recycler.setLayoutManager(LayoutManagaer_my_appointments);



        getMyAPointments();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getMyAPointments();

            }
        });

    }



    private void getMyAPointments() {
        list_appointments = new ArrayList<>();
        refreshLayout.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.MY_APPOINTMENTS_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array_my_appointments = new JSONArray(object.getString("appointments"));

                                for (int i = 0; i < array_my_appointments.length(); i++) {
                                    JSONObject specialty_object = array_my_appointments.getJSONObject(i);
                                    AppointmentDescription appointmentDescription = new AppointmentDescription();
                                    appointmentDescription.setId(specialty_object.getInt("id"));
                                    appointmentDescription.setColor(specialty_object.getString("color"));
                                    appointmentDescription.setDate(specialty_object.getString("date"));
                                    appointmentDescription.setObservation(specialty_object.getString("observation"));
                                    appointmentDescription.setStart(specialty_object.getString("start"));
                                    appointmentDescription.setStatus(specialty_object.getString("status"));
                                    appointmentDescription.setEnd(specialty_object.getString("end"));
                                    appointmentDescription.setReason(specialty_object.getString("reason"));

                                    appointmentDescription.setStatus(specialty_object.getString("status"));
                                    appointmentDescription.setSpecialty(specialty_object.getString("specialty"));
                                    appointmentDescription.setName_p(specialty_object.getString("name_p"));
                                    appointmentDescription.setLast_name_p(specialty_object.getString("last_name_p"));
                                    appointmentDescription.setCi(specialty_object.getString("ci"));
                                    appointmentDescription.setName_d(specialty_object.getString("name_d"));
                                    appointmentDescription.setLast_name_d(specialty_object.getString("last_name_d"));
                                    appointmentDescription.setUrl_image_d(specialty_object.getString("url_image"));

                                    //  System.out.println("objeto   : =>      " + appointmentDescription.getSpecialty() + " , " + appointmentDescription.getName_d() + "  , " + appointmentDescription.getObservation());
                                    list_appointments.add(appointmentDescription);

                                }
                                if (list_appointments.size() == 0) {
                                    empty.setVisibility(View.VISIBLE);
                                } else {
                                    empty.setVisibility(View.GONE);
                                }
                                MyAppointmentHistoryAdapter myAppointmentAdapter = new MyAppointmentHistoryAdapter(getContext(), list_appointments);
                                my_appointments_recycler.setAdapter(myAppointmentAdapter);


                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        refreshLayout.setRefreshing(false);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println(error);
                        refreshLayout.setRefreshing(false);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
}
