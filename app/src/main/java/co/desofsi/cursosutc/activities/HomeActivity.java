package co.desofsi.cursosutc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.fragments.AccountFragment;
import co.desofsi.cursosutc.fragments.CalendarFragment;
import co.desofsi.cursosutc.fragments.HomeFragment;
import co.desofsi.cursosutc.fragments.NotificationFragment;
import co.desofsi.cursosutc.fragments.TreatmentFragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;


public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private final static int ID_HOME = 1;
    private final static int ID_EXPLORE = 2;
    private final static int ID_MESSAGE = 3;
    private final static int ID_NOTIFICATION = 4;
    private final static int ID_ACCOUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
               window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
              //  window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
               window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTurquezaNormal));
                //getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager = getSupportFragmentManager();


        final TextView tvSelected = findViewById(R.id.tv_selected);
        tvSelected.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-Regular.ttf"));

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home_black_24dp));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_EXPLORE, R.drawable.ic_calendar));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MESSAGE, R.drawable.ic_check_file));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_NOTIFICATION, R.drawable.ic_baseline_payment_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ACCOUNT, R.drawable.ic_account_circle_black_24dp));

       // bottomNavigation.setCount(ID_NOTIFICATION, "115");

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
               // Toast.makeText(HomeActivity.this, "clicked item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
             //   Toast.makeText(HomeActivity.this, "showing item : " + item.getId(), Toast.LENGTH_SHORT).show();

                String name;
                switch (item.getId()) {
                    case ID_HOME:
                        name = "HOME";

                        fragmentManager.beginTransaction().replace(R.id.home_frame_container,new HomeFragment(),HomeFragment.class.getSimpleName()).commit();
                        break;
                    case ID_EXPLORE:
                        name = "EXPLORE";
                        fragmentManager.beginTransaction().replace(R.id.home_frame_container,new CalendarFragment(),CalendarFragment.class.getSimpleName()).commit();

                        break;
                    case ID_MESSAGE:
                        name = "MESSAGE";
                        fragmentManager.beginTransaction().replace(R.id.home_frame_container,new TreatmentFragment(),TreatmentFragment.class.getSimpleName()).commit();

                        break;
                    case ID_NOTIFICATION:
                        name = "NOTIFICATION";
                        fragmentManager.beginTransaction().replace(R.id.home_frame_container,new NotificationFragment(),NotificationFragment.class.getSimpleName()).commit();

                        break;
                    case ID_ACCOUNT:
                        name = "ACCOUNT";
                        fragmentManager.beginTransaction().replace(R.id.home_frame_container,new AccountFragment(),AccountFragment.class.getSimpleName()).commit();

                        break;
                    default:
                        name = "";
                }
                tvSelected.setText(getString(R.string.main_page_selected, name));
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(HomeActivity.this, "reselected item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        //bottomNavigation.setCount(ID_NOTIFICATION, "115");

        bottomNavigation.show(ID_HOME,true);




    }
}
