package co.desofsi.souriapp.init;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import co.desofsi.souriapp.R;
import co.desofsi.souriapp.content.HomeActivity;
import co.desofsi.souriapp.data.Constant;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private View view;
    private TextInputLayout layout_name, layout_last_name, layout_password, layout_cofirm_pass;
    private TextInputEditText txt_name, txt_last_name, txt_password, txt_confirm_pass;
    private Button btn_save;
    private CircleImageView image;
    private TextView txt_selected;
    private ProgressDialog dialog;
    private static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;
    private SharedPreferences userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        init();
    }

    public void init() {
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        layout_name = findViewById(R.id.profile_text_name_layout);
        layout_last_name = findViewById(R.id.profile_text_last_name_layout);
        layout_password = findViewById(R.id.profile_text_password_layout);
        layout_cofirm_pass = findViewById(R.id.profile_text_confirm_password_layout);

        txt_name = findViewById(R.id.profile_text_name);
        txt_last_name = findViewById(R.id.profile_text_last_name);
        txt_password = findViewById(R.id.profile_text_password);
        txt_confirm_pass = findViewById(R.id.profile_text_confirm_password);
        btn_save = findViewById(R.id.profile_btn_save);
        txt_selected = findViewById(R.id.profile_selec_photo);
        image = findViewById(R.id.profile_img_user);
        dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setCancelable(false);

        txt_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_ADD_PROFILE);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    saveProfile();
                }
            }
        });

    }

    private void saveProfile() {
        dialog.setMessage("Actualizando");
        dialog.show();
        final String name = txt_name.getText().toString().trim();
        final String last_name = txt_last_name.getText().toString().trim();
        final String password = txt_password.getText().toString().trim();
        final String confir_password = txt_confirm_pass.getText().toString().trim();

        System.out.println(Constant.LOGIN);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SAVE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {

                                SharedPreferences.Editor editor = userPref.edit();
                                editor.putString("url_image", object.getString("url_image"));
                                editor.apply();
                                startActivity(new Intent(UserProfileActivity.this, HomeActivity.class));
                                finish();
                                Toast.makeText(UserProfileActivity.this, "Actualizado", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UserProfileActivity.this, "Error al guardar", Toast.LENGTH_LONG).show();
                        System.out.println(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("last_name", last_name);
                map.put("password", password);
                map.put("url_image", bitmapToString(bitmap));

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
        requestQueue.add(stringRequest);

    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
          //  System.out.println("ARCHIVO BASE 64  "+Base64.encodeToString(array, Base64.DEFAULT));
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "#";
    }

    public boolean validate() {

        if (txt_name.getText().toString().isEmpty()) {
            layout_name.setErrorEnabled(true);
            layout_name.setError("El nombre es obligatorio");
            return false;
        }
        if (txt_last_name.getText().toString().isEmpty()) {
            layout_last_name.setErrorEnabled(true);
            layout_last_name.setError("El apellido es obligatorio");
            return false;
        }
        if (txt_password.getText().toString().length() < 7) {
            layout_password.setErrorEnabled(true);
            layout_password.setError("La contraseña debe tener al menos 8 carácteres");
            return false;
        }
        if (!txt_confirm_pass.getText().toString().equals(txt_password.getText().toString())) {
            layout_cofirm_pass.setErrorEnabled(true);
            layout_cofirm_pass.setError("Las contraseñas no son iguales");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ADD_PROFILE && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            image.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}