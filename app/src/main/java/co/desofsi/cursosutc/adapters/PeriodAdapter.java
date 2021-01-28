package co.desofsi.cursosutc.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.activities.AppointmentActivity;
import co.desofsi.cursosutc.activities.HomeActivity;
import co.desofsi.cursosutc.activities.LevelsActivity;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Level;
import co.desofsi.cursosutc.models.Period;
import co.desofsi.cursosutc.models.Specialty;

public class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.SpecialtyHolder>  {

    private Context context;
    private ArrayList<Period> list;

    public PeriodAdapter(Context context, ArrayList<Period> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SpecialtyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment__recycler_specialty,parent,false);
       return new SpecialtyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialtyHolder holder, final int position) {

        final Period period = list.get(position);
        int[] images = {R.drawable.l1, R.drawable.l2, R.drawable.l3, R.drawable.l4, R.drawable.l5, R.drawable.l6, R.drawable.l7, R.drawable.l8, R.drawable.l9, R.drawable.l10, R.drawable.l11, R.drawable.l12, R.drawable.l13, R.drawable.l14, R.drawable.l15};
        Random rand = new Random();

     //   Picasso.get().load(Constant.URL+period.getUrl_image()).into(holder.image_specialty);
        holder.imagePeriod.setImageResource(images[rand.nextInt(images.length)]);
        holder.txtNamePeriod.setText(period.getName());
        holder.txtEndDatePeriod.setText(period.getEnd_date());
        holder.cardView.setCardBackgroundColor(Color.parseColor(period.getColor()));
        holder.cardView.setRadius(40);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.PERIOD_ID = period.getId();
                Intent intent =  new Intent(((HomeActivity)context), LevelsActivity.class);
                context.startActivity(intent);
            //    Toast.makeText(context, "clicked item : ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class SpecialtyHolder extends RecyclerView.ViewHolder{

        private TextView txtNamePeriod,txtEndDatePeriod;
        private ImageView imagePeriod;
        private CardView cardView;


        public SpecialtyHolder(@NonNull View itemView) {
            super(itemView);
            txtNamePeriod = itemView.findViewById(R.id.txtNamePeriod);
            txtEndDatePeriod= itemView.findViewById(R.id.txtEndDatePeriod);
            imagePeriod = itemView.findViewById(R.id.imagePeriod);
            cardView = itemView.findViewById(R.id.recylcer_card_view);


        }
    }
}
