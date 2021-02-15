package co.desofsi.cursosutc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.adapters.CourseAdapter;
import co.desofsi.cursosutc.adapters.TaskAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Course;
import co.desofsi.cursosutc.models.Task;
import de.hdodenhof.circleimageview.CircleImageView;

public class TasksActivity extends AppCompatActivity implements View.OnClickListener {
    CircleImageView btnBackTasks;
    RecyclerView recyclerView;
    private ArrayList<Task> list_tasks;
    private SharedPreferences sharedPreferences;
    JSONArray array;
    private TaskAdapter taskAdapter;
    private SwipeRefreshLayout refreshLayout;

    CardView cardViewOpen, cardViewCancel, cardViewFinish, cardViewOverdue;
    TextView lblOpen, lblCancel, lblFinish, lblOverdue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
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

        btnBackTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                if (Constant.SUBJECT_ID == 0) {
                    intent = new Intent(TasksActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                } else {
                    intent = new Intent(TasksActivity.this, CoursesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
            }
        });
    }

    private void init() {
//
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnBackTasks = (CircleImageView) findViewById(R.id.btnBackTasks);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerTasks);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeTasks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TasksActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getTasks("Abierto");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTasks(Constant.STATUS_TASK);
            }
        });

        //card filter
        cardViewOpen = (CardView) findViewById(R.id.cardViewOpen);
        cardViewCancel = (CardView) findViewById(R.id.cardViewCancel);
        cardViewFinish = (CardView) findViewById(R.id.cardViewFinish);
        cardViewOverdue = (CardView) findViewById(R.id.cardViewOverdue);

        lblOpen = (TextView) findViewById(R.id.lblOpen);
        lblCancel = (TextView) findViewById(R.id.lblCancel);
        lblFinish = (TextView) findViewById(R.id.lblFinish);
        lblOverdue = (TextView) findViewById(R.id.lblOverdue);

        setOnClickListeners();
    }

    void setOnClickListeners() {
        cardViewOpen.setOnClickListener(this);
        cardViewCancel.setOnClickListener(this);
        cardViewFinish.setOnClickListener(this);
        cardViewOverdue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardViewOpen:
                addTheme(cardViewOpen, lblOpen, "#64B5F6");
                removeTheme(cardViewCancel, lblCancel, "#FFFFFF");
                removeTheme(cardViewFinish, lblFinish, "#FFFFFF");
                removeTheme(cardViewOverdue, lblOverdue, "#FFFFFF");
                Constant.STATUS_TASK = "Abierto";
                getTasks("Abierto");
                //Toast.makeText(this, "Abierto", Toast.LENGTH_LONG).show();
                break;
            case R.id.cardViewCancel:
                addTheme(cardViewCancel, lblCancel, "#E57373");
                removeTheme(cardViewOpen, lblOpen, "#FFFFFF");
                removeTheme(cardViewFinish, lblFinish, "#FFFFFF");
                removeTheme(cardViewOverdue, lblOverdue, "#FFFFFF");
                Constant.STATUS_TASK = "Cancelado";
                getTasks("Cancelado");
                //Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                break;
            case R.id.cardViewFinish:
                addTheme(cardViewFinish, lblFinish, "#81C784");
                removeTheme(cardViewOpen, lblOpen, "#FFFFFF");
                removeTheme(cardViewCancel, lblCancel, "#FFFFFF");
                removeTheme(cardViewOverdue, lblOverdue, "#FFFFFF");
                Constant.STATUS_TASK = "Finalizado";
                getTasks("Finalizado");
                //Toast.makeText(this, "Finalizado", Toast.LENGTH_LONG).show();
                break;
            case R.id.cardViewOverdue:
                addTheme(cardViewOverdue, lblOverdue, "#E57373");
                removeTheme(cardViewOpen, lblOpen, "#FFFFFF");
                removeTheme(cardViewCancel, lblCancel, "#FFFFFF");
                removeTheme(cardViewFinish, lblFinish, "#FFFFFF");
                Constant.STATUS_TASK = "Atrasado";
                getTasks("Atrasado");
                //Toast.makeText(this, "Finalizado", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void addTheme(CardView cardView, TextView textView, String color) {
        cardView.setCardBackgroundColor(Color.parseColor(color));
        textView.setTextColor(Color.parseColor("#FFFFFF"));
    }

    public void removeTheme(CardView cardView, TextView textView, String color) {
        cardView.setCardBackgroundColor(Color.parseColor(color));
        textView.setTextColor(Color.parseColor("#898989"));
    }


    private void getTasks(String status_task) {
        list_tasks = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        String url = Constant.TASKS + Constant.COURSE_ID + "/" + status_task;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array = new JSONArray(object.getString("tasks"));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject task_object = array.getJSONObject(i);

                                    Task task = new Task();
                                    task.setId(task_object.getInt("id"));
                                    task.setName(task_object.getString("name"));
                                    task.setDescription(task_object.getString("description"));
                                    task.setStart_date(task_object.getString("start_date"));
                                    task.setEnd_date(task_object.getString("end_date"));
                                    task.setUrl_file(task_object.getString("url_file"));
                                    task.setEnd_time(task_object.getString("end_time"));
                                    task.setStatus(task_object.getString("status"));
                                    task.setCourse_id(task_object.getInt("course_id"));
                                    task.setDeliveries(task_object.getInt("deliveries"));
                                    task.setFiles(task_object.getInt("files"));
                                    list_tasks.add(task);
                                }
                                taskAdapter = new TaskAdapter(TasksActivity.this, list_tasks);
                                recyclerView.setAdapter(taskAdapter);

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
        RequestQueue requestQueue = Volley.newRequestQueue(TasksActivity.this);
        requestQueue.add(stringRequest);

    }
}