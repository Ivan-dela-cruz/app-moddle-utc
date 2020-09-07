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
import androidx.recyclerview.widget.GridLayoutManager;
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
import co.desofsi.souriapp.adapters.MyPaymentAdapter;
import co.desofsi.souriapp.adapters.MyTreatmentAdapter;
import co.desofsi.souriapp.adapters.SpecialtyAdapter;
import co.desofsi.souriapp.data.Constant;
import co.desofsi.souriapp.models.Payments;
import co.desofsi.souriapp.models.Specialty;
import co.desofsi.souriapp.models.Treatment;

public class NotificationFragment extends Fragment {
    private View view;

    private ArrayList<Specialty> lis_specialties;
    private SwipeRefreshLayout refreshLayout;
    private SpecialtyAdapter specialtyAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    ///atrututos del perfil

    private TextView name_user;
    //MY APPOINTMENTS
    private ArrayList<Payments> list_payments;
    private RecyclerView my_appointments_recycler;
    ///json array
    JSONArray array, array_my_appointments;

    private RelativeLayout empty;
    public NotificationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);
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
        //LinearLayoutManager LayoutManagaer_my_appointments = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        //my_appointments_recycler.setLayoutManager(LayoutManagaer_my_appointments);
        my_appointments_recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));



        getMyAPointments();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getMyAPointments();

            }
        });

    }



    private void getMyAPointments() {
        list_payments = new ArrayList<>();
        refreshLayout.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.MY_PAYMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array_my_appointments = new JSONArray(object.getString("payments"));

                                for (int i = 0; i < array_my_appointments.length(); i++) {
                                    JSONObject specialty_object = array_my_appointments.getJSONObject(i);
                                    Payments payment = new Payments();
                                    payment.setId(specialty_object.getInt("id"));
                                    payment.setId_patient(specialty_object.getInt("id_patient"));
                                    payment.setId_doctor(specialty_object.getInt("id_doctor"));
                                    payment.setId_treatment(specialty_object.getInt("id_treatment"));
                                    payment.setReason(specialty_object.getString("reason"));
                                    payment.setDescription(specialty_object.getString("description"));
                                    payment.setStatus(specialty_object.getString("status"));
                                    payment.setStatus_pay(specialty_object.getString("status_pay"));

                                    payment.setUrl_file(specialty_object.getString("url_file"));

                                    payment.setUpdated_at(specialty_object.getString("updated_at"));
                                    payment.setPrice_total(specialty_object.getString("price_total"));
                                    payment.setName_p(specialty_object.getString("name_p"));
                                    payment.setLast_name_p(specialty_object.getString("last_name_p"));
                                    payment.setDocument(specialty_object.getString("document"));
                                    payment.setName_d(specialty_object.getString("name_d"));
                                    payment.setLast_name_d(specialty_object.getString("last_name_d"));
                                    payment.setDues(specialty_object.getString("dues"));
                                    payment.setName_s(specialty_object.getString("name_s"));

                                    //  System.out.println("objeto   : =>      " + appointmentDescription.getSpecialty() + " , " + appointmentDescription.getName_d() + "  , " + appointmentDescription.getObservation());
                                    list_payments.add(payment);

                                }

                                if(list_payments.size()==0){
                                    empty.setVisibility(View.VISIBLE);
                                }
                                else {
                                    empty.setVisibility(View.GONE);
                                }
                                MyPaymentAdapter myPaymentAdapter = new MyPaymentAdapter(getContext(), list_payments);
                                my_appointments_recycler.setAdapter(myPaymentAdapter);


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
