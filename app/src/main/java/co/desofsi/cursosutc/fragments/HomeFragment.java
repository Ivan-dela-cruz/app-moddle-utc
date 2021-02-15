package co.desofsi.cursosutc.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.activities.TasksActivity;
import co.desofsi.cursosutc.adapters.MyAppointmentAdapter;
import co.desofsi.cursosutc.adapters.PeriodAdapter;
import co.desofsi.cursosutc.adapters.SpecialtyAdapter;
import co.desofsi.cursosutc.activities.HomeActivity;
import co.desofsi.cursosutc.adapters.TaskAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.init.AuthActivity;
import co.desofsi.cursosutc.init.UserProfileActivity;
import co.desofsi.cursosutc.models.AppointmentDescription;
import co.desofsi.cursosutc.models.Period;
import co.desofsi.cursosutc.models.Specialty;
import co.desofsi.cursosutc.models.Task;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView,recyclerViewTaskToday;
    private ArrayList<Period> list_periods;
    private ArrayList<Task> list_tasks;
    private SwipeRefreshLayout refreshLayout;
    private PeriodAdapter periodAdapter;
    private TaskAdapter taskAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    ///atrututos del perfil

    private TextView name_user;
    private CircleImageView image_user;
    private ImageButton btn_close;
    //MY APPOINTMENTS
  //  private ArrayList<AppointmentDescription> list_appointments_today;
 //   private RecyclerView my_appointments_recycler;
    ///json array
    JSONArray array, array_tasks;

    private RelativeLayout empty;
    private ProgressDialog dialog;

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
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.home_fragment_recycler);
        recyclerViewTaskToday =view.findViewById(R.id.recyclerViewTaskToday);
        recyclerView.setHasFixedSize(true);
        recyclerViewTaskToday.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);


        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerViewTaskToday.setLayoutManager(verticalLayoutManagaer);

        refreshLayout = view.findViewById(R.id.home_fragment_swipe);
        toolbar = view.findViewById(R.id.home_fragment_toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        empty = view.findViewById(R.id.vacio);

        btn_close = view.findViewById(R.id.home_fragment_btn_exit);
        ///INICIAR ATRIBUTOS DEL PERFIL
        name_user = view.findViewById(R.id.fragment_home_text_user_name);
        image_user = view.findViewById(R.id.fragment_home_circle_view_user);
        ///RECYCLER MY_APPOINTMEST INIT
      //  my_appointments_recycler = view.findViewById(R.id.home_fragment_recycler_my_appointments);
       // my_appointments_recycler.setHasFixedSize(true);
        //LinearLayoutManager LayoutManagaer_my_appointments = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
       // my_appointments_recycler.setLayoutManager(LayoutManagaer_my_appointments);

        String name = sharedPreferences.getString("name", "");
        String url_image_user = sharedPreferences.getString("url_image", "");
        name_user.setText("Hola " + name + "   ¿Estas buscando un curso?");
        Picasso.get().load(Constant.URL + url_image_user).into(image_user);

        getPeriods();
        getTasks();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPeriods();
               getTasks();

            }
        });
        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(((HomeActivity) getContext()));
                builder.setMessage("¿Desea cerrar sesión?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    public void logout() {
        dialog.setMessage("Cerrando sesión");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                startActivity(new Intent(((HomeActivity) getContext()), AuthActivity.class));
                                ((HomeActivity) getContext()).finish();
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Las credenciales no coinciden", Toast.LENGTH_LONG).show();
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

    private void getPeriods() {
        list_periods = new ArrayList<>();
        refreshLayout.setRefreshing(true);
      //  System.out.println(Constant.HOME_PERIODS);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.HOME_PERIODS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array = new JSONArray(object.getString("periods"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject period_object = array.getJSONObject(i);
                                    Period period = new Period();
                                    period.setColor(period_object.getString("color"));
                                    period.setEnd_date(period_object.getString("end_date"));
                                    period.setStart_date(period_object.getString("start_date"));
                                    period.setId(period_object.getInt("id"));
                                    period.setName(period_object.getString("name"));
                                    period.setStatus(period_object.getString("status"));
                                    period.setUrl_image(period_object.getString("url_image"));

                                    list_periods.add(period);

                                }
                                periodAdapter = new PeriodAdapter(getContext(), list_periods);
                                recyclerView.setAdapter(periodAdapter);

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

    private void getTasks() {
        list_tasks = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        String url = Constant.TASKS_TODAY;
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
                                taskAdapter = new TaskAdapter(getContext(), list_tasks);
                                recyclerViewTaskToday.setAdapter(taskAdapter);

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
