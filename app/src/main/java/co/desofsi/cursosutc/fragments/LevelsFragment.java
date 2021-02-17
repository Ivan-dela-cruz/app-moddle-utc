package co.desofsi.cursosutc.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import co.desofsi.cursosutc.activities.LevelsActivity;
import co.desofsi.cursosutc.adapters.CourseAdapter;
import co.desofsi.cursosutc.adapters.LevelAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Course;
import co.desofsi.cursosutc.models.Level;


public class LevelsFragment extends Fragment {
    private View view;
    private ArrayList<Level> list_levels;
    private SwipeRefreshLayout refreshLayout;
    private LevelAdapter levelAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;
    JSONArray array;
    private RelativeLayout empty;
    private RecyclerView recyclerView;

    public LevelsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_levels, container, false);
        init();
        return view;
    }

    public void init(){
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        empty = view.findViewById(R.id.vacio);

        refreshLayout = view.findViewById(R.id.swipeMyLevels);
        toolbar = view.findViewById(R.id.my_levels_fragment_toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);



        recyclerView = view.findViewById(R.id.recyclerMyLevels);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager LayoutManagaer_my_appointments = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(LayoutManagaer_my_appointments);

        getLevels();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLevels();
            }
        });
    }

    private void getLevels() {
        list_levels = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        String url = Constant.MY_LEVELS;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array = new JSONArray(object.getString("levels"));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject level_object = array.getJSONObject(i);

                                    Level level = new Level();
                                    level.setStudent_id(level_object.getInt("student_id"));
                                    level.setLevel_id(level_object.getInt("level_id"));
                                    level.setName(level_object.getString("name"));
                                    //  employee.setUrlImage(level_object.getString("url_image"));

                                    list_levels.add(level);

                                }
                                levelAdapter = new LevelAdapter(getContext(), list_levels);
                                recyclerView.setAdapter(levelAdapter);

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