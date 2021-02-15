package co.desofsi.cursosutc.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import co.desofsi.cursosutc.adapters.EducationAdapter;
import co.desofsi.cursosutc.adapters.PeriodAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Education;
import co.desofsi.cursosutc.models.Period;


public class EducationFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Education> list_education;
    private SwipeRefreshLayout refreshLayout;
    private EducationAdapter educationAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;
    JSONArray array;

    public EducationFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_education, container, false);
        init();
        return view;
    }

    private void init(){
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.recyclerEducation);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //LinearLayoutManager linearLayoutManager =  new GridLayoutManager(getContext(),2);

        recyclerView.setLayoutManager(linearLayoutManager);
        refreshLayout = view.findViewById(R.id.swipeEducation);
        getEducationNews();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEducationNews();

            }
        });
    }

    private void getEducationNews() {
        list_education = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        //  System.out.println(Constant.HOME_PERIODS);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.EDUCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array = new JSONArray(object.getString("education"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject education_object = array.getJSONObject(i);
                                    Education education = new Education();
                                    education.setId(education_object.getInt("id"));
                                    education.setAcademic_period_id(education_object.getInt("academic_period_id"));
                                    education.setLevel_id(education_object.getInt("level_id"));
                                    education.setSubject_id(education_object.getInt("subject_id"));
                                    education.setUser_id(education_object.getInt("user_id"));
                                    education.setName(education_object.getString("name"));
                                    education.setDescription(education_object.getString("description"));
                                    education.setCareer(education_object.getString("career"));
                                    education.setUrl_image(education_object.getString("url_image"));
                                    education.setContent(education_object.getString("content"));
                                    education.setStatus(education_object.getInt("status"));
                                    education.setCreated_at(education_object.getString("created_at"));

                                    list_education.add(education);

                                }
                                educationAdapter = new EducationAdapter(getContext(), list_education);
                                recyclerView.setAdapter(educationAdapter);

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