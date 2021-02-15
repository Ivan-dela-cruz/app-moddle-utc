package co.desofsi.cursosutc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.SubmitProcessButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.adapters.FileAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.data.FilePath;
import co.desofsi.cursosutc.data.ProgressGenerator;
import co.desofsi.cursosutc.models.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.view.View.GONE;

public class DetailTaskActivity extends AppCompatActivity implements ProgressGenerator.OnCompleteListener, View.OnClickListener {
    private static final int PERMISSIONS_STORAGE_CODE = 1000;
    private static final int ALL_FILE_REQUEST = 102;
    TextView lblTitleDT, lblDescriptionDT, lblStartDateDT, lblEndDateDT, lblEndTimeDT, lblStatusDT, lblSelectedFile;
    CircleImageView btnBackDetailTask;
    RecyclerView recyclerView, recyclerFilesDTDELIVERY;
    private ArrayList<File> list_files, list_files_delivery;
    private SharedPreferences sharedPreferences;
    JSONArray array, array_files_delivery;
    private FileAdapter fileAdapter, fileAdapterDelivery;
    private SwipeRefreshLayout refreshLayout;
    ActionProcessButton btnSendTask;
    ProgressGenerator progressGenerator;
    Button btnSelectedFile;
    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    String all_file_path;
    EditText textArea;
    LinearLayout layoutDelivery;
    ProgressDialog progress;

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
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSIONS_STORAGE_CODE);
            } else {
                //downloading
            }
        } else {/* not permissions*/}
        init();

    }

    @Override
    public void onComplete() {
        //Toast.makeText(this, R.string.Loading_Complete, Toast.LENGTH_LONG).show();
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
        lblSelectedFile = (TextView) findViewById(R.id.lblSelectedFile);
        textArea = (EditText) findViewById(R.id.textArea_information);
        layoutDelivery = (LinearLayout) findViewById(R.id.layoutDelivery);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerFilesDT);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeDetailTask);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailTaskActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        btnSendTask = (ActionProcessButton) findViewById(R.id.btnSendTask);
        btnSelectedFile = (Button) findViewById(R.id.btnSelectedFile);

        recyclerFilesDTDELIVERY = (RecyclerView) findViewById(R.id.recyclerFilesDTDELIVERY);
        LinearLayoutManager linearLayoutManagerDelivery = new LinearLayoutManager(DetailTaskActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerFilesDTDELIVERY.setLayoutManager(linearLayoutManagerDelivery);


        if (Constant.DELIVERY_COUNT > 0) {
            layoutDelivery.setVisibility(GONE);
            Toast.makeText(DetailTaskActivity.this, "Tarea entregada.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(DetailTaskActivity.this, "Tarea pendiente.", Toast.LENGTH_LONG).show();
        }

        getDetailTask();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetailTask();
            }
        });

        setOnClickListeners();

        progressGenerator = new ProgressGenerator(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean(EXTRAS_ENDLESS_MODE)) {
            btnSendTask.setMode(ActionProcessButton.Mode.ENDLESS);
        } else {
            btnSendTask.setMode(ActionProcessButton.Mode.PROGRESS);
        }

    }

    void setOnClickListeners() {
        btnSendTask.setOnClickListener(this);
        btnBackDetailTask.setOnClickListener(this);
        btnSelectedFile.setOnClickListener(this);
    }


    private void getDetailTask() {
        lblTitleDT.setText(Constant.NAME_TASK);
        lblDescriptionDT.setText(Constant.DESCRIPTION_TASK);
        lblStartDateDT.setText(Constant.START_DATE_TASK);
        lblEndDateDT.setText(Constant.END_DATE_TASK);
        lblEndTimeDT.setText(Constant.END_TIME_TASK);
        lblStatusDT.setText(Constant.STATUS_TASK_DT);
        lblStatusDT.setTextColor(Color.parseColor("#" + statusColor(Constant.STATUS_TASK_DT)));

        list_files = new ArrayList<>();
        list_files_delivery = new ArrayList<>();
        refreshLayout.setRefreshing(true);
        String url = Constant.FILES + Constant.TASK_ID;
        //  System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                //files
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

                                //files deliveries
                                array_files_delivery = new JSONArray(object.getString("files_deliveries"));

                                if (array_files_delivery != null) {
                                    for (int i = 0; i < array_files_delivery.length(); i++) {
                                        JSONObject delivery_object = array_files_delivery.getJSONObject(i);

                                        File file_delivery = new File();
                                        file_delivery.setId(delivery_object.getInt("id"));
                                        file_delivery.setName(delivery_object.getString("name"));
                                        file_delivery.setFilename(delivery_object.getString("filename"));
                                        file_delivery.setUrl_file(delivery_object.getString("url_file"));
                                        list_files_delivery.add(file_delivery);
                                    }
                                    fileAdapterDelivery = new FileAdapter(DetailTaskActivity.this, list_files_delivery);
                                    recyclerFilesDTDELIVERY.setAdapter(fileAdapterDelivery);
                                }
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

    public String statusColor(String status) {
        String color = "";
        if (status.equals("Abierto")) {
            color = "64B5F6";
        } else if (status.equals("Cancelado")) {
            color = "E57373";
        } else if (status.equals("Atrasado")) {
            color = "E57373";
        } else if (status.equals("Finalizado")) {
            color = "81C784";
        }
        return color;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //init download
                } else {
                    Toast.makeText(this, "¡Sin permisos!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackDetailTask:
                final Intent intent;
                if (Constant.COURSE_ID == 0) {
                    intent = new Intent(DetailTaskActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                } else {
                    intent = new Intent(DetailTaskActivity.this, TasksActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }

                startActivity(intent);
                break;
            case R.id.btnSelectedFile:
                filePicker();
                break;
            case R.id.btnSendTask:
                if (all_file_path != null) {
                    UploadTask uploadTask = new UploadTask();
                    uploadTask.execute(new String[]{all_file_path});
                    progress = new ProgressDialog(DetailTaskActivity.this);
                    progress.setMessage("Enviando...");
                    progress.show();
                    progress.setCanceledOnTouchOutside(false);
                } else {
                    Toast.makeText(DetailTaskActivity.this, "Seleccione un archivo.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void filePicker() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione su tarea para subir"), ALL_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ALL_FILE_REQUEST) {
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                String paths = FilePath.getFilePath(DetailTaskActivity.this, uri);
                Log.d("File Path : ", "" + paths);
                if (paths != null) {
                    lblSelectedFile.setText("" + new java.io.File(paths).getName());
                }
                all_file_path = paths;
            }
        }
    }

    public class UploadTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) { //cambiar a tipo array
            super.onPostExecute(s);

            if (s != null) {
                getDetailTask();
                progressGenerator.start(btnSendTask);
                btnSendTask.setEnabled(false);
                layoutDelivery.setVisibility(GONE);
            } else {
                //  Toast.makeText(DetailTaskActivity.this, "File Upload Failed", Toast.LENGTH_SHORT).show();
            }
            //   progressBar.setVisibility(View.GONE);
            progress.hide();
        }

        @Override
        protected String doInBackground(String... strings) {

            java.io.File file1 = new java.io.File(strings[0]);
            String url = Constant.DELIVERY_TASK;
            ;
            System.out.println(url);
            String token = sharedPreferences.getString("token", "");
            String description = textArea.getText().toString();

            try {
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("files1", file1.getName(), RequestBody.create(MediaType.parse("*/*"), file1))
                        .addFormDataPart("description", description)
                        .addFormDataPart("course_id", String.valueOf(Constant.COURSE_ID))
                        .addFormDataPart("task_id", String.valueOf(Constant.TASK_ID))
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .addHeader("Authorization", "Bearer " + token)
                        .post(requestBody)
                        .build();

                OkHttpClient okHttpClient = new OkHttpClient();
                okhttp3.Response response = okHttpClient.newCall(request).execute();
                if (response != null && response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}