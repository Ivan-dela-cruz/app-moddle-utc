package co.desofsi.souriapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import co.desofsi.souriapp.R;
import co.desofsi.souriapp.adapters.SpecialtyAdapter;
import co.desofsi.souriapp.content.HomeActivity;
import co.desofsi.souriapp.data.Constant;
import co.desofsi.souriapp.models.Specialty;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Specialty> lis_specialties;
    private SwipeRefreshLayout refreshLayout;
    private SpecialtyAdapter specialtyAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = view.findViewById(R.id.home_fragment_swipe);
        toolbar = view.findViewById(R.id.home_fragment_toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);


        getSpecialties();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSpecialties();
            }
        });
    }

    private void getSpecialties() {
        lis_specialties = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        System.out.println(Constant.LOGIN);
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
                                    System.out.println("objeto   : =>      " + specialty.getUrl_image() + " , " + specialty.getName() + "  , " + specialty.getDescription() + "  ,   " + specialty.getColor() + "  ,   " + specialty.getCreated_at() + "   ,  " + specialty.getStatus());
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


}
