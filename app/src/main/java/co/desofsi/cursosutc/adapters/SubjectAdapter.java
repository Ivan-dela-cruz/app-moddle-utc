package co.desofsi.cursosutc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.desofsi.cursosutc.R;

import co.desofsi.cursosutc.activities.CoursesActivity;
import co.desofsi.cursosutc.activities.LevelsActivity;
import co.desofsi.cursosutc.activities.SubjectsActivity;
import co.desofsi.cursosutc.data.Constant;

import co.desofsi.cursosutc.models.Subject;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {

    private Context context;
    private ArrayList<Subject> list_subject;

    public SubjectAdapter(Context context, ArrayList<Subject> list) {
        this.context = context;
        this.list_subject = list;
    }

    @NonNull
    @Override
    public SubjectAdapter.SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyvler_subjects, parent, false);

        return new SubjectAdapter.SubjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.SubjectHolder holder, final int position) {

        final Subject subject = list_subject.get(position);

        // Picasso.get().load(Constant.URL+"img/users/"+specialty.getDoctor().getUrl_image()).into(holder.image_doctor);
        //  Picasso.get().load(Constant.URL+employee.getUrlImage()).into(holder.imageEmployee); //descomentar en produccion
        //Picasso.get().load(employee.getUrlImage()).into(holder.imageEmployee);
        // Picasso.get().load("https://i.imgur.com/tGbaZCY.jpg").into(holder.image_estate);
        holder.lblNameSubject.setText(subject.getName());
        holder.cardViewSubject.setRadius(40);


        holder.cardViewSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.SUBJECT_ID = subject.getSubject_id();
                Constant.PERIOD_ID = subject.getPeriod_id();
                Intent intent = new Intent((SubjectsActivity)context, CoursesActivity.class);
                context.startActivity(intent);
             }
        });

    }

    @Override
    public int getItemCount() {
        return list_subject.size();
    }


    class SubjectHolder extends RecyclerView.ViewHolder {

        private TextView lblNameSubject;
        private ImageView imgSubject;
        private CardView cardViewSubject;


        public SubjectHolder(@NonNull View itemView) {
            super(itemView);
            cardViewSubject = itemView.findViewById(R.id.cardViewSubject);
            imgSubject = itemView.findViewById(R.id.imgSubject);
            lblNameSubject = itemView.findViewById(R.id.lblNameSubject);

        }
    }
}
