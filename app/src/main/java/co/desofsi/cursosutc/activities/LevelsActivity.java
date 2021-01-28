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
import java.util.Random;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.adapters.LevelAdapter;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Level;
import de.hdodenhof.circleimageview.CircleImageView;

public class LevelsActivity extends AppCompatActivity {

    CircleImageView btnBackLevels;
    RecyclerView recyclerView;
    private ArrayList<Level> list_levels;
    private SharedPreferences sharedPreferences;
    JSONArray array;
    private LevelAdapter levelAdapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
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

        //back to menu estates
        final Intent intent = new Intent(LevelsActivity.this, HomeActivity.class);
        btnBackLevels.setOnClickListener(new View.OnClickListener() {
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
        btnBackLevels = (CircleImageView) findViewById(R.id.btnBackLevels);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerLevels);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLevels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LevelsActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
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
        String url = Constant.LEVELS+Constant.PERIOD_ID;
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
                                levelAdapter = new LevelAdapter(LevelsActivity.this, list_levels);
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
        RequestQueue requestQueue = Volley.newRequestQueue(LevelsActivity.this);
        requestQueue.add(stringRequest);

    }
}