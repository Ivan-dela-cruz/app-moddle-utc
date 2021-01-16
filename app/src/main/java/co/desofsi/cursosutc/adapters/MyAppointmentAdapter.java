package co.desofsi.cursosutc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.AppointmentDescription;

public class MyAppointmentAdapter extends RecyclerView.Adapter<MyAppointmentAdapter.SpecialtyHolder> {

    private Context context;
    private ArrayList<AppointmentDescription> list;

    public MyAppointmentAdapter(Context context, ArrayList<AppointmentDescription> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SpecialtyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment__recycler_my_appointments, parent, false);
        return new SpecialtyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialtyHolder holder, final int position) {

        final AppointmentDescription appointmentDescription = list.get(position);
        String pattern = "yyyy-MM-dd";
        String data_appointment = appointmentDescription.getDate().substring(0, 10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Date date = simpleDateFormat.parse(data_appointment);
            SimpleDateFormat format = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            String date_for_human = format.format(date);
            int hour = Integer.parseInt(appointmentDescription.getStart().substring(11, 13));
            String pref = " AM";
            if (hour > 12) {
                pref = " PM";
            }

            holder.txt_name_specialty.setText(appointmentDescription.getSpecialty());
            holder.txt_name_doctor.setText("Dr. " + appointmentDescription.getName_d()
                    + " " + appointmentDescription.getLast_name_d());
            holder.txt_start.setText(date_for_human + " " + appointmentDescription.getStart().substring(11, 16) + pref);
            holder.txt_reason.setText(appointmentDescription.getReason());
            Picasso.get().load(Constant.URL + appointmentDescription.getUrl_image_d()).into(holder.image_doctor);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // holder.cardView.setCardBackgroundColor(Color.parseColor(appointmentDescription.getColor()));
        // holder.cardView.setRadius(40);

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(((HomeActivity)context), AppointmentActivity.class);
                intent.putExtra("specialty",specialty);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class SpecialtyHolder extends RecyclerView.ViewHolder {

        private TextView txt_name_doctor, txt_name_specialty, txt_start, txt_reason;
        private ImageView image_doctor;
        private CardView cardView;


        public SpecialtyHolder(@NonNull View itemView) {
            super(itemView);
            txt_name_doctor = itemView.findViewById(R.id.recycler_doctor_name_my_appointment);
            txt_name_specialty = itemView.findViewById(R.id.recycler_specialty_name_my_appointment);
            txt_start = itemView.findViewById(R.id.recycler_start__my_appointment);
            txt_reason = itemView.findViewById(R.id.recycler_reason_my_appointment);
            image_doctor = itemView.findViewById(R.id.recycler_image_my_appointment);
            cardView = itemView.findViewById(R.id.recylcer_card_view_my_appointment);


        }
    }
}
