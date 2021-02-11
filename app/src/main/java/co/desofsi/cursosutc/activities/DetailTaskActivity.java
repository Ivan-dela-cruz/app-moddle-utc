package co.desofsi.cursosutc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.adapters.FileAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailTaskActivity extends AppCompatActivity {
    private static final int PERMISSIONS_STORAGE_CODE = 1000;
    TextView lblTitleDT, lblDescriptionDT, lblStartDateDT, lblEndDateDT, lblEndTimeDT, lblStatusDT;
    CircleImageView btnBackDetailTask;
    RecyclerView recyclerView;
    private ArrayList<File> list_files;
    private SharedPreferences sharedPreferences;
    JSONArray array;
    private FileAdapter fileAdapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSIONS_STORAGE_CODE);
            } else {
                //downloading
            }
        } else {
            // not permissions
        }

        init();
        final Intent intent = new Intent(DetailTaskActivity.this, TasksActivity.class);
        btnBackDetailTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void init() {
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnBackDetailTask = (CircleImageView) findViewById(R.id.btnBackDetailTask);
        lblTitleDT = (TextView) findViewById(R.id.lblTitleDT);
        lblDescriptionDT = (TextView) findViewById(R.id.lblDescriptionDT);
        lblStartDateDT = (TextView) findViewById(R.id.lblStartDateDT);
        lblEndDateDT = (TextView) findViewById(R.id.lblEndDateDT);
        lblEndTimeDT = (TextView) findViewById(R.id.lblEndTimeDT);
        lblStatusDT = (TextView) findViewById(R.id.lblStatusDT);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerFilesDT);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeDetailTask);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailTaskActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        getDetailTask();

    }

    private void getDetailTask() {
        lblTitleDT.setText(Constant.NAME_TASK);
        lblDescriptionDT.setText(Constant.DESCRIPTION_TASK);
        lblStartDateDT.setText(Constant.START_DATE_TASK);
        lblEndDateDT.setText(Constant.END_DATE_TASK);
        lblEndTimeDT.setText(Constant.END_TIME_TASK);
        lblStatusDT.setText(Constant.STATUS_TASK_DT);
         list_files = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        String url = Constant.FILES + Constant.TASK_ID;

        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                array = new JSONArray(object.getString("files"));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject task_object = array.getJSONObject(i);

                                    File file = new File();
                                    file.setId(task_object.getInt("id"));
                                    file.setName(task_object.getString("name"));
                                    file.setFilename(task_object.getString("filename"));
                                    file.setUrl_file(task_object.getString("url_file"));

                                    list_files.add(file);
                                }
                                fileAdapter = new FileAdapter(DetailTaskActivity.this, list_files);
                                recyclerView.setAdapter(fileAdapter);

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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailTaskActivity.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //init download
                }else{
                    Toast.makeText(this, "¡Sin permisos!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}