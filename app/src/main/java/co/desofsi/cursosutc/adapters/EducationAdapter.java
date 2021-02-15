package co.desofsi.cursosutc.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import co.desofsi.cursosutc.R;

import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.DateClass;
import co.desofsi.cursosutc.models.Education;


public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationHolder> {

    private Context context;
    private ArrayList<Education> list_education;

    public EducationAdapter(Context context, ArrayList<Education> list) {
        this.context = context;
        this.list_education = list;
    }

    @NonNull
    @Override
    public EducationAdapter.EducationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_education, parent, false);

        return new EducationAdapter.EducationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EducationAdapter.EducationHolder holder, final int position) {

        final Education education = list_education.get(position);
        String urlImage = Constant.URL + education.getUrl_image();
        DateClass dateClass = new DateClass();
        String date = dateClass.dateFormatHuman(education.getCreated_at());
        Picasso.get().load(urlImage).into(holder.imageEC);
        holder.lblCreateEC.setText(date);
        holder.lblTitleEC.setText(education.getName());
        holder.lblDescriptionEC.setText(education.getDescription());
        holder.cardViewEducation.setRadius(30);

        holder.cardViewEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constant.URL+"dashboard/continuing-education/list";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_education.size();
    }


    class EducationHolder extends RecyclerView.ViewHolder {

        private TextView lblTitleEC, lblDescriptionEC,lblCreateEC;
        private ImageView imageEC;
        private CardView cardViewEducation;


        public EducationHolder(@NonNull View itemView) {
            super(itemView);
            cardViewEducation = itemView.findViewById(R.id.cardViewEducation);
            imageEC = itemView.findViewById(R.id.imageEC);
            lblTitleEC = itemView.findViewById(R.id.lblTitleEC);
            lblDescriptionEC = itemView.findViewById(R.id.lblDescriptionEC);
            lblCreateEC = itemView.findViewById(R.id.lblCreateEC);

        }

    }

}