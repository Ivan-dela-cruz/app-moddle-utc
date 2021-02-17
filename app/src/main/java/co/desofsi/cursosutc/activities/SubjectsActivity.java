package co.desofsi.cursosutc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.desofsi.cursosutc.R;


import co.desofsi.cursosutc.adapters.LevelAdapter;
import co.desofsi.cursosutc.adapters.SubjectAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Level;
import co.desofsi.cursosutc.models.Subject;
import de.hdodenhof.circleimageview.CircleImageView;


public class SubjectsActivity extends AppCompatActivity {
    CircleImageView btnBackSubjects;
    RecyclerView recyclerView;
    private ArrayList<Subject> list_subjects;
    private SharedPreferences sharedPreferences;
    JSONArray array;
    private SubjectAdapter subjectAdapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        try {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                //   window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTurquezaNormal));
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();

        btnBackSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent ;
                if (Constant.PERIOD_ID == 0) {
                    intent = new Intent(SubjectsActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                } else {
                    intent = new Intent(SubjectsActivity.this, LevelsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
            }
        });
    }

    private void init() {
//
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnBackSubjects = (CircleImageView) findViewById(R.id.btnBackSubjects);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerSubjects);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeSubjects);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SubjectsActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getSubjects();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSubjects();
            }
        });
    }
    private void getSubjects() {
        list_subjects = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        String url = Constant.SUBJECTS+Constant.LEVEL_ID;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array = new JSONArray(object.getString("subjects"));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject level_object = array.getJSONObject(i);

                                    Subject subject = new Subject();
                                    subject.setPeriod_id(level_object.getInt("period_id"));
                                    subject.setStudent_id(level_object.getInt("student_id"));
                                    subject.setSubject_id(level_object.getInt("subject_id"));
                                    subject.setName(level_object.getString("name"));
                                    //  employee.setUrlImage(level_object.getString("url_image"));

                                    list_subjects.add(subject);

                                }
                                subjectAdapter = new SubjectAdapter(SubjectsActivity.this, list_subjects);
                                recyclerView.setAdapter(subjectAdapter);

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
        RequestQueue requestQueue = Volley.newRequestQueue(SubjectsActivity.this);
        requestQueue.add(stringRequest);

    }

}