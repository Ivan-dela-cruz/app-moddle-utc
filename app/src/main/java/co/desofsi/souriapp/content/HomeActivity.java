package co.desofsi.souriapp.content;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import co.desofsi.souriapp.R;
import co.desofsi.souriapp.fragments.AccountFragment;
import co.desofsi.souriapp.fragments.CalendarFragment;
import co.desofsi.souriapp.fragments.HomeFragment;
import co.desofsi.souriapp.fragments.NotificationFragment;
import co.desofsi.souriapp.fragments.TreatmentFragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
        fragmentManager = getSupportFragmentManager();


        final TextView tvSelected = findViewById(R.id.tv_selected);
        tvSelected.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-Regular.ttf"));

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home_black_24dp));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_EXPLORE, R.drawable.ic_calendar));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MESSAGE, R.drawable.ic_check_file));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_NOTIFICATION, R.drawable.ic_notifications_black_24dp));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ACCOUNT, R.drawable.ic_account_circle_black_24dp));

        bottomNavigation.setCount(ID_NOTIFICATION, "115");

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

        bottomNavigation.setCount(ID_NOTIFICATION, "115");

        bottomNavigation.show(ID_NOTIFICATION,true);




    }
}
