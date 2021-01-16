package co.desofsi.cursosutc.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.activities.AppointmentActivity;
import co.desofsi.cursosutc.activities.HomeActivity;
import co.desofsi.cursosutc.data.Constant;
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
        Picasso.get().load(Constant.URL+period.getUrl_image()).into(holder.image_specialty);
        holder.txt_name_specialty.setText(period.getName());
        holder.text_status_specialty.setText(period.getEnd_date());
        holder.cardView.setCardBackgroundColor(Color.parseColor(period.getColor()));
        holder.cardView.setRadius(40);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(((HomeActivity)context), AppointmentActivity.class);
                intent.putExtra("specialty",period);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class SpecialtyHolder extends RecyclerView.ViewHolder{

        private TextView txt_name_specialty,text_status_specialty;
        private ImageView image_specialty;
        private CardView cardView;


        public SpecialtyHolder(@NonNull View itemView) {
            super(itemView);
            txt_name_specialty = itemView.findViewById(R.id.recycler_specialty_name);
            text_status_specialty= itemView.findViewById(R.id.recycler_specialty_status);
            image_specialty = itemView.findViewById(R.id.recycler_specialty_image);
            cardView = itemView.findViewById(R.id.recylcer_card_view);


        }
    }
}
