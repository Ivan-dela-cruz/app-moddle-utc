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

import java.util.ArrayList;
import java.util.Random;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.activities.CoursesActivity;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Course;

public class CourseAdapter  extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

    private Context context;
    private ArrayList<Course> list_course;

    public CourseAdapter(Context context, ArrayList<Course> list) {
        this.context = context;
        this.list_course = list;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_courses, parent, false);

        return new CourseAdapter.CourseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, final int position) {

        final Course course = list_course.get(position);

        // Picasso.get().load(Constant.URL+"img/users/"+specialty.getDoctor().getUrl_image()).into(holder.image_doctor);
        //  Picasso.get().load(Constant.URL+employee.getUrlImage()).into(holder.imageEmployee); //descomentar en produccion
        //Picasso.get().load(employee.getUrlImage()).into(holder.imageEmployee);
        // Picasso.get().load("https://i.imgur.com/tGbaZCY.jpg").into(holder.image_estate);
        int[] images = {R.drawable.l1, R.drawable.l2, R.drawable.l3, R.drawable.l4, R.drawable.l5, R.drawable.l6, R.drawable.l7, R.drawable.l8, R.drawable.l9, R.drawable.l10, R.drawable.l11, R.drawable.l12, R.drawable.l13, R.drawable.l14, R.drawable.l15};
        Random rand = new Random();

        holder.imgCourse.setImageResource(images[rand.nextInt(images.length)]);
        holder.lblNameCourse.setText(course.getName() +" "+course.getLast_name());
        holder.lblTitleCourse.setText(course.getTitle());
        holder.cardViewCourse.setRadius(40);
        holder.cardViewCourse.setCardBackgroundColor(Color.parseColor("#40f5f5f5"));

        holder.cardViewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "teacher_id:  " + course.getTeacher_id(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_course.size();
    }


    class CourseHolder extends RecyclerView.ViewHolder {

        private TextView lblNameCourse, lblTitleCourse;
        private ImageView imgCourse;
        private CardView cardViewCourse;


        public CourseHolder(@NonNull View itemView) {
            super(itemView);
            cardViewCourse = itemView.findViewById(R.id.cardViewCourse);
            imgCourse = itemView.findViewById(R.id.imgCourse);
            lblNameCourse = itemView.findViewById(R.id.lblNameCourse);
            lblTitleCourse = itemView.findViewById(R.id.lblTitleCourse);

        }
    }
}
