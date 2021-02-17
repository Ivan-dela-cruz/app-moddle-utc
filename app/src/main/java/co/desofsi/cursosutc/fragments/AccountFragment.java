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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.activities.HomeActivity;
import co.desofsi.cursosutc.activities.PasswordActivity;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.init.AuthActivity;
import co.desofsi.cursosutc.init.UserProfileActivity;
import co.desofsi.cursosutc.models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    private View view;
    private ProgressDialog dialog;
    SharedPreferences sharedPreferences;
    private User user_login;

    private ImageButton btn_edit, btn_close, btn_pass;
    private CircleImageView img_user;
    private TextView txt_names, txt_email, txt_ci, txt_birth, txt_passport, txt_province, txt_instruction, txt_marital_status, txt_phone, txt_canton, txt_parish, txt_address, txt_treatments, txt_appointments;


    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        init();
        eventsButtons();
        getProfile();
        return view;
    }

    private void init() {
        dialog = new ProgressDialog(getContext());
        user_login = new User();
        dialog.setCancelable(false);
        sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        btn_close = view.findViewById(R.id.account_fragment_btn_close);
        btn_edit = view.findViewById(R.id.account_fragment_btn_edit);
        btn_pass = view.findViewById(R.id.account_fragment_btn_password);
        txt_names = view.findViewById(R.id.account_fragment_txt_names);
        txt_email = view.findViewById(R.id.account_fragment_txt_email);
        txt_address = view.findViewById(R.id.account_fragment_txt_address);
        txt_passport = view.findViewById(R.id.account_fragment_txt_passport);
        txt_marital_status = view.findViewById(R.id.account_fragment_txt_marital_status);
        txt_instruction = view.findViewById(R.id.account_fragment_txt_instruction);
        txt_phone = view.findViewById(R.id.account_fragment_txt_phone);
        txt_province = view.findViewById(R.id.account_fragment_txt_province);
        txt_canton = view.findViewById(R.id.account_fragment_txt_canton);
        txt_parish = view.findViewById(R.id.account_fragment_txt_parish);
        txt_birth = view.findViewById(R.id.account_fragment_txt_date);
        txt_ci = view.findViewById(R.id.account_fragment_txt_ci);
        //  txt_treatments = view.findViewById(R.id.account_fragment_txt_count_treatment);
        // txt_appointments = view.findViewById(R.id.account_fragment_txt_count_appointments);
        img_user = view.findViewById(R.id.account_fragment_img_user);

    }

    public void eventsButtons() {
        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PasswordActivity.class);
                startActivity(intent);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
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

    public void getProfile() {
        dialog.setMessage("Cargando");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                JSONObject profile = object.getJSONObject("profile");
                                JSONObject user = object.getJSONObject("user");

                                /*
                                        "birth_date": "1997-09-10",
                                        "gender": "masculino",
                                        "address": "San Felipe",
                                        "province": "Cotopaxi",
                                        "city": "Latacunga",
                                        "phone": "0958495039",
                                        "phone_2": null,
                                        "url_image": "img/users/1597877040.jpeg",
                                        "status": "activo",
                                        "email": "chiluisa982@gmail.com",
                                        "instruction": "secundaria",
                                        "marital_status": "Soltero(a)",
                                        "job": "Estudiante",



                                 */

                                User user_profile = new User();
                                user_profile.setId(profile.getInt("id"));
                                user_profile.setId_user(profile.getString("user_id"));
                                user_profile.setDni(profile.getString("dni"));
                                user_profile.setPassport(profile.getString("passport"));
                                user_profile.setName(profile.getString("name"));
                                user_profile.setLast_name(profile.getString("last_name"));
                                user_profile.setInstruction(profile.getString("instruction"));
                                user_profile.setMarital_status(profile.getString("marital_status"));
                                user_profile.setAddress(profile.getString("address"));
                                user_profile.setPhone(profile.getString("phone"));
                                user_profile.setEmail(profile.getString("email"));
                                user_profile.setUrl_image(user.getString("url_image"));
                                user_profile.setBirth_date(profile.getString("birth_date"));
                                user_profile.setGender(profile.getString("gender"));

                                user_profile.setProvince(profile.getString("province"));
                                user_profile.setCanton(profile.getString("canton"));
                                user_profile.setParish(profile.getString("parish"));
                                //   user_profile.setLevels(object.getInt("treatments"));
                                //  user_profile.setCourses(object.getInt("appointments"));


                                user_login = user_profile;
                                loadProfile(user_profile);

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

    public void loadProfile(User user) {
        String url_image = sharedPreferences.getString("url_image", "");
        String passport = "- - -";
        if (!user.getPassport().equals("")) {
            passport = user.getPassport();
        }

        txt_names.setText(user.getName() + " " + user.getLast_name());
        txt_ci.setText(user.getDni());
        txt_address.setText(user.getAddress());
        txt_email.setText(user.getEmail());
        txt_passport.setText(passport);
        txt_instruction.setText(user.getInstruction());
        txt_marital_status.setText(user.getMarital_status());
        txt_phone.setText(user.getPhone());
        txt_province.setText(user.getProvince());
        txt_canton.setText(user.getCanton());
        txt_parish.setText(user.getParish());
        txt_birth.setText(user.getBirth_date());
//        txt_appointments.setText("" + user.getLevels() + " en total");
  //      txt_treatments.setText("" + user.getCourses() + " en total");

        if (!user.getUrl_image().equals("")) {
            Picasso.get().load(Constant.URL + user.getUrl_image()).into(img_user);
        }

    }


}
