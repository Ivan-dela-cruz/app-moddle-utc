package co.desofsi.cursosutc.fragments;

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

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.activities.HomeActivity;
import co.desofsi.cursosutc.adapters.MyTreatmentAdapter;
import co.desofsi.cursosutc.adapters.SpecialtyAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Specialty;
import co.desofsi.cursosutc.models.Treatment;

public class TreatmentFragment extends Fragment {
    private View view;

    private ArrayList<Specialty> lis_specialties;
    private SwipeRefreshLayout refreshLayout;
    private SpecialtyAdapter specialtyAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    ///atrututos del perfil

    private TextView name_user;
    //MY APPOINTMENTS
    private ArrayList<Treatment> list_treatments;
    private RecyclerView my_appointments_recycler;
    ///json array
    JSONArray array, array_my_appointments;
    private RelativeLayout empty;
    public TreatmentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_treatment, container, false);
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
        list_treatments = new ArrayList<>();
        refreshLayout.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.MY_TREATMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array_my_appointments = new JSONArray(object.getString("treatments"));

                                for (int i = 0; i < array_my_appointments.length(); i++) {
                                    JSONObject specialty_object = array_my_appointments.getJSONObject(i);

                                    Treatment treatment = new Treatment();
                                    treatment.setId(specialty_object.getInt("id"));
                                    treatment.setPatient_id(specialty_object.getInt("patient_id"));
                                    treatment.setDoctor_id(specialty_object.getInt("doctor_id"));
                                    treatment.setSpecailty_id(specialty_object.getInt("specailty_id"));
                                    treatment.setReason(specialty_object.getString("reason"));
                                    treatment.setDescription(specialty_object.getString("description"));
                                    treatment.setStatus(specialty_object.getString("status"));
                                    treatment.setStatus_pay(specialty_object.getString("status_pay"));
                                    treatment.setUrl_file(specialty_object.getString("url_file"));

                                    treatment.setUpdated_at(specialty_object.getString("updated_at"));
                                    treatment.setPrice_total(specialty_object.getString("price_total"));
                                    treatment.setName_p(specialty_object.getString("name_p"));
                                    treatment.setLast_name_p(specialty_object.getString("last_name_p"));
                                    treatment.setColor(specialty_object.getString("color"));
                                    treatment.setName_d(specialty_object.getString("name_d"));
                                    treatment.setLast_name_d(specialty_object.getString("last_name_d"));
                                    treatment.setUrl_image(specialty_object.getString("url_image"));
                                    treatment.setUrl_image_s(specialty_object.getString("url_image_s"));
                                    treatment.setName_s(specialty_object.getString("name_s"));

                                    //  System.out.println("objeto   : =>      " + appointmentDescription.getSpecialty() + " , " + appointmentDescription.getName_d() + "  , " + appointmentDescription.getObservation());
                                    list_treatments.add(treatment);

                                }

                                if(list_treatments.size()==0){
                                    empty.setVisibility(View.VISIBLE);
                                }
                                else {
                                    empty.setVisibility(View.GONE);
                                }
                                MyTreatmentAdapter myAppointmentAdapter = new MyTreatmentAdapter(getContext(), list_treatments);
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
