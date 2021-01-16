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

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.models.Level;

import java.util.ArrayList;

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

        final Level employee = list_level.get(position);

        // Picasso.get().load(Constant.URL+"img/users/"+specialty.getDoctor().getUrl_image()).into(holder.image_doctor);

        //  Picasso.get().load(Constant.URL+employee.getUrlImage()).into(holder.imageEmployee); //descomentar en produccion
        //Picasso.get().load(employee.getUrlImage()).into(holder.imageEmployee);
        // Picasso.get().load("https://i.imgur.com/tGbaZCY.jpg").into(holder.image_estate);
        holder.lblNameLevel.setText(employee.getName());

        //   holder.cardView.setCardBackgroundColor(Color.parseColor(String.valueOf(R.color.colorRed)));
        holder.cardViewLevel.setRadius(40);

       /* holder.cardViewEstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((HomeActivity)context, MenuEstatesActivity.class);
                context.startActivity(intent);
            }
        });*/
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