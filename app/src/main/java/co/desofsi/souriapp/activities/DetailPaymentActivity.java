package co.desofsi.souriapp.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import co.desofsi.souriapp.R;
import co.desofsi.souriapp.adapters.ReviewListPaymentAdapter;
import co.desofsi.souriapp.adapters.ReviewListTreatmentAdapter;
import co.desofsi.souriapp.data.Constant;
import co.desofsi.souriapp.models.DateClass;
import co.desofsi.souriapp.models.DetailPayment;
import co.desofsi.souriapp.models.DetailTreatment;
import co.desofsi.souriapp.models.Payments;
import co.desofsi.souriapp.models.Treatment;

public class DetailPaymentActivity extends AppCompatActivity {

    private Payments payments;
    private ImageButton btn_download;
    private static final int PERMISSION_STORAGE_CODE = 1000;


    private ArrayList<DetailPayment> lis_detail;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;

    private ImageButton btn_home;
    private TextView txt_treatment_number, txt_treatment_customer, txt_treatment_data, txt_treatment_treatment, txt_treatment_total, txt_status;
    private Button btn_complete, btn_map;
    private ScrollView scrollView;
    private DateClass dateClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_payment);
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //  window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTurquezaNormal));
                //getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        payments = (Payments) getIntent().getExtras().getSerializable("payments");
        //Toast.makeText(DetailPaymentActivity.this, "" + payments.getUrl_file(), Toast.LENGTH_SHORT).show();
        init();
        eventsButtons();
        loadReviewtreatment();

        getOrdersDetail();


    }

    public void init() {
        btn_download = (ImageButton) findViewById(R.id.detail_btn_download);

        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        dateClass = new DateClass();
        txt_treatment_number = findViewById(R.id.deatil_detail_treatment_txt_treatment_number);
        txt_treatment_customer = findViewById(R.id.deatil_detail_treatment_txt_customer);
        txt_treatment_data = findViewById(R.id.deatil_detail_treatment_txt_date);
        txt_treatment_treatment = findViewById(R.id.deatil_detail_treatment_txt_treatment);
        txt_treatment_total = findViewById(R.id.deatil_detail_treatment_txt_total);
        btn_home = findViewById(R.id.deatil_detail_treatment_btn_back);
        btn_map = findViewById(R.id.deatil_detail_treatment_btn_map);
        scrollView = findViewById(R.id.deatil_detail_treatment_scroll);
        recyclerView = findViewById(R.id.deatil_detail_treatment_recycler);
        txt_status = findViewById(R.id.deatil_detail_treatment_txt_status);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailPaymentActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


    }

    private void getOrdersDetail() {
        lis_detail = new ArrayList<>();
        String url = Constant.PAYMENT_DETAIL + "/" + payments.getId();
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                JSONArray array = new JSONArray(object.getString("detail"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject type_object = array.getJSONObject(i);

                                    DetailPayment detail = new DetailPayment();
                                    detail.setId(type_object.getInt("id"));
                                    detail.setDescription(type_object.getString("description"));
                                    detail.setDate_pay(type_object.getString("date_pay"));
                                    detail.setDues(type_object.getInt("number_dues"));
                                    detail.setIs_check(type_object.getInt("ischeck"));
                                    detail.setStatus(type_object.getString("status"));
                                    detail.setTotal(type_object.getDouble("total"));
                                    lis_detail.add(detail);
                                }

                                ReviewListPaymentAdapter listAdapter = new ReviewListPaymentAdapter(DetailPaymentActivity.this, lis_detail);
                                recyclerView.setAdapter(listAdapter);

                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailPaymentActivity.this);
        requestQueue.add(stringRequest);
    }

    public void loadReviewtreatment() {

        txt_treatment_total.setText("$ " + payments.getPrice_total());
        txt_treatment_treatment.setText(payments.getName_d() + " " + payments.getLast_name_d());
        txt_treatment_customer.setText(payments.getName_p() + " " + payments.getLast_name_p());
        txt_treatment_data.setText(dateClass.dateFormatHuman(payments.getUpdated_at()));
        txt_treatment_number.setText("# " + payments.getId());
        txt_status.setText(payments.getStatus());
        scrollView.pageScroll(View.FOCUS_UP);

    }

    public void eventsButtons() {
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_STORAGE_CODE);

                    } else {
                        startDownLoad();
                    }


                } else {
                    startDownLoad();
                }
            }
        });
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_STORAGE_CODE);

                    } else {
                        startDownLoad();
                    }


                } else {
                    startDownLoad();
                }
            }
        });
    }

    public void startDownLoad() {
        String url = Constant.URL + payments.getUrl_file();
        String tempTitle = payments.getName_p();
        System.out.println(url);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        /*request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Descargar");
        request.setDescription("Descargando comprobante de orden");
        request.allowScanningByMediaScanner();
*/
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle("Descargar") //Download Manager Title
                .setDescription("Descargando comprobante")
                .setMimeType("application/pdf")
                .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        tempTitle + ".pdf"
                );

        downloadManager.enqueue(request);
    }
}