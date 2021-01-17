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

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.activities.LevelsActivity;
import co.desofsi.cursosutc.activities.SubjectsActivity;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Level;

import java.util.ArrayList;
import java.util.Random;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelHolder> {

    private Context context;
    private ArrayList<Level> list_level;

    public LevelAdapter(Context context, ArrayList<Level> list) {
        this.context = context;
        this.list_level = list;
    }

    @NonNull
    @Override
    public LevelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_levels, parent, false);

        return new LevelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelHolder holder, final int position) {

        final Level level = list_level.get(position);
        int[] images = {R.drawable.l1, R.drawable.l2, R.drawable.l3, R.drawable.l4, R.drawable.l5, R.drawable.l6, R.drawable.l7, R.drawable.l8, R.drawable.l9, R.drawable.l10, R.drawable.l11, R.drawable.l12, R.drawable.l13, R.drawable.l14, R.drawable.l15};
        Random rand = new Random();

        holder.imgLevel.setImageResource(images[rand.nextInt(images.length)]);

        // Picasso.get().load(Constant.URL+"img/users/"+specialty.getDoctor().getUrl_image()).into(holder.image_doctor);
        //  Picasso.get().load(Constant.URL+employee.getUrlImage()).into(holder.imageEmployee); //descomentar en produccion
        //Picasso.get().load(employee.getUrlImage()).into(holder.imageEmployee);
        // Picasso.get().load("https://i.imgur.com/tGbaZCY.jpg").into(holder.image_estate);
        holder.lblNameLevel.setText(level.getName());
        holder.cardViewLevel.setRadius(40);

        holder.cardViewLevel.setCardBackgroundColor(Color.parseColor("#40f5f5f5"));
        holder.cardViewLevel.setBackgroundResource(R.drawable.grey_gradient);
        holder.cardViewLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.LEVEL_ID = level.getLevel_id();
                Intent intent = new Intent((LevelsActivity) context, SubjectsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_level.size();
    }


    class LevelHolder extends RecyclerView.ViewHolder {

        private TextView lblNameLevel;
        private ImageView imgLevel;
        private CardView cardViewLevel;


        public LevelHolder(@NonNull View itemView) {
            super(itemView);
            cardViewLevel = itemView.findViewById(R.id.cardViewLevel);
            imgLevel = itemView.findViewById(R.id.imgLevel);
            lblNameLevel = itemView.findViewById(R.id.lblNameLevel);

        }
    }
}