package co.desofsi.souriapp.adapters;

import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import co.desofsi.souriapp.R;
import co.desofsi.souriapp.data.Constant;
import co.desofsi.souriapp.models.Specialty;
import de.hdodenhof.circleimageview.CircleImageView;

public class SpecialtyAdapter extends RecyclerView.Adapter<SpecialtyAdapter.SpecialtyHolder>  {

    private Context context;
    private ArrayList<Specialty> list;

    public SpecialtyAdapter(Context context, ArrayList<Specialty> list) {
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
    public void onBindViewHolder(@NonNull SpecialtyHolder holder, int position) {

        Specialty specialty = list.get(position);

       // Picasso.get().load(Constant.URL+"img/users/"+specialty.getDoctor().getUrl_image()).into(holder.image_doctor);
        System.out.println(Constant.URL+specialty.getUrl_image());
       Picasso.get().load(Constant.URL+specialty.getUrl_image()).into(holder.image_specialty);
        //Picasso.get().load("https://i.imgur.com/tGbaZCY.jpg").into(holder.imageView_specialty);
        holder.txt_name_specialty.setText(specialty.getName());
        holder.text_status_specialty.setText(specialty.getDescription());

        holder.cardView.setCardBackgroundColor(Color.parseColor(specialty.getColor()));
        holder.cardView.setRadius(40);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class SpecialtyHolder extends RecyclerView.ViewHolder{

        private TextView txt_name_specialty,text_status_specialty;
        private CircleImageView image_specialty;
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
