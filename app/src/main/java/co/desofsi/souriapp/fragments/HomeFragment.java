package co.desofsi.souriapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import co.desofsi.souriapp.R;
import co.desofsi.souriapp.adapters.SpecialtyAdapter;
import co.desofsi.souriapp.activities.HomeActivity;
import co.desofsi.souriapp.data.Constant;
import co.desofsi.souriapp.models.AppointmentDescription;
import co.desofsi.souriapp.models.Specialty;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Specialty> lis_specialties;
    private SwipeRefreshLayout refreshLayout;
    private SpecialtyAdapter specialtyAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    ///atrututos del perfil

    private TextView name_user;
    private CircleImageView image_user;
    //MY APPOINTMENTS
    private ArrayList<AppointmentDescription> list_appointments_today;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return view;
    }

    public void init() {
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.home_fragment_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        refreshLayout = view.findViewById(R.id.home_fragment_swipe);
        toolbar = view.findViewById(R.id.home_fragment_toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ///INICIAR ATRIBUTOS DEL PERFIL
        name_user = view.findViewById(R.id.fragment_home_text_user_name);
        image_user = view.findViewById(R.id.fragment_home_circle_view_user);

        String name = sharedPreferences.getString("name", "");
        String url_image_user = sharedPreferences.getString("url_image", "");
        name_user.setText("Hola " + name + " ¿Estas buscando una cita?");
        Picasso.get().load(Constant.URL + url_image_user).into(image_user);


        getSpecialties();
        getMyAPointments();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSpecialties();
                getMyAPointments();
            }
        });
    }

    private void getSpecialties() {
        lis_specialties = new ArrayList<>();
        refreshLayout.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.SPECIALTIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                JSONArray array = new JSONArray(object.getString("specialties"));

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
                                    // System.out.println("objeto   : =>      " + specialty.getUrl_image() + " , " + specialty.getName() + "  , " + specialty.getDescription() + "  ,   " + specialty.getColor() + "  ,   " + specialty.getCreated_at() + "   ,  " + specialty.getStatus());
                                    lis_specialties.add(specialty);

                                }
                                specialtyAdapter = new SpecialtyAdapter(getContext(), lis_specialties);
                                recyclerView.setAdapter(specialtyAdapter);

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
                        refreshLayout.setRefreshing(false);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getMyAPointments() {
        list_appointments_today = new ArrayList<>();
        refreshLayout.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.MY_APPOINTMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                JSONArray array = new JSONArray(object.getString("appointments"));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject specialty_object = array.getJSONObject(i);
                                    AppointmentDescription appointmentDescription = new AppointmentDescription();
                                    appointmentDescription.setId(specialty_object.getInt("id"));
                                    appointmentDescription.setColor(specialty_object.getString("color"));
                                    appointmentDescription.setDate(specialty_object.getString("date"));
                                    appointmentDescription.setObservation(specialty_object.getString("observation"));
                                    appointmentDescription.setStart(specialty_object.getString("start"));
                                    appointmentDescription.setStatus(specialty_object.getString("status"));
                                    appointmentDescription.setEnd(specialty_object.getString("end"));

                                    appointmentDescription.setStatus(specialty_object.getString("status"));
                                    appointmentDescription.setSpecialty(specialty_object.getString("specialty"));
                                    appointmentDescription.setName_p(specialty_object.getString("name_p"));
                                    appointmentDescription.setLast_name_p(specialty_object.getString("last_name_p"));
                                    appointmentDescription.setCi(specialty_object.getString("ci"));
                                    appointmentDescription.setName_d(specialty_object.getString("name_d"));
                                    appointmentDescription.setLast_name_d(specialty_object.getString("last_name_d"));

                                    System.out.println("objeto   : =>      " + appointmentDescription.getColor() + " , " + appointmentDescription.getName_d() + "  , " + appointmentDescription.getObservation());
                                    list_appointments_today.add(appointmentDescription);

                                }
                                // specialtyAdapter = new SpecialtyAdapter(getContext(), lis_specialties);
                                // recyclerView.setAdapter(specialtyAdapter);

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
                        refreshLayout.setRefreshing(false);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}
