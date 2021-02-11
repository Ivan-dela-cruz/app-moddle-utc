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
import co.desofsi.cursosutc.adapters.CourseAdapter;
import co.desofsi.cursosutc.adapters.SubjectAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Course;
import co.desofsi.cursosutc.models.Subject;
import de.hdodenhof.circleimageview.CircleImageView;

public class CoursesActivity extends AppCompatActivity {

    CircleImageView btnBackCourses;
    RecyclerView recyclerView;
    private ArrayList<Course> list_courses;
    private SharedPreferences sharedPreferences;
    JSONArray array;
    private CourseAdapter courseAdapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
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

        final Intent intent = new Intent(CoursesActivity.this, SubjectsActivity.class);
        btnBackCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void init() {
//
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnBackCourses = (CircleImageView) findViewById(R.id.btnBackCourses);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerCourses);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeCourses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CoursesActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getCourses();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCourses();
            }
        });
    }

    private void getCourses() {
        list_courses = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        String url = Constant.COURSES+Constant.SUBJECT_ID+"/"+Constant.PERIOD_ID;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array = new JSONArray(object.getString("courses"));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject level_object = array.getJSONObject(i);

                                    Course course = new Course();
                                    course.setId(level_object.getInt("id"));
                                    course.setTitle(level_object.getString("title"));
                                    course.setName(level_object.getString("name"));
                                    course.setLast_name(level_object.getString("last_name"));
                                    course.setDescription(level_object.getString("description"));
                                    course.setUrl_image(level_object.getString("url_image"));
                                    course.setTeacher_id(level_object.getInt("teacher_id"));
                                    course.setSubject_id(level_object.getInt("subject_id"));

                                    //  employee.setUrlImage(level_object.getString("url_image"));

                                    list_courses.add(course);

                                }
                                courseAdapter = new CourseAdapter(CoursesActivity.this, list_courses);
                                recyclerView.setAdapter(courseAdapter);

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
        RequestQueue requestQueue = Volley.newRequestQueue(CoursesActivity.this);
        requestQueue.add(stringRequest);

    }
}