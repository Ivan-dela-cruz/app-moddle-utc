package co.desofsi.cursosutc.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import co.desofsi.cursosutc.R;

import co.desofsi.cursosutc.activities.CoursesActivity;
import co.desofsi.cursosutc.activities.DetailTaskActivity;
import co.desofsi.cursosutc.activities.TasksActivity;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private Context context;
    private ArrayList<Task> list_task;

    public TaskAdapter(Context context, ArrayList<Task> list) {
        this.context = context;
        this.list_task = list;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_tasks, parent, false);

        return new TaskAdapter.TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.TaskHolder holder, final int position) {

        final Task task = list_task.get(position);

        int[] images = {
                R.drawable.bg1,
                R.drawable.bg2,
                R.drawable.bg3,
                R.drawable.bg4,
                R.drawable.bg5,
                R.drawable.bg6,
                R.drawable.bg7,
                R.drawable.bg8,
                R.drawable.bg9,
                R.drawable.bg10,
                R.drawable.bg11,
                R.drawable.bg12,
                R.drawable.bg13,
                R.drawable.bg14,
                R.drawable.bg15,
                R.drawable.bg16,
                R.drawable.bg17,
                R.drawable.bg18,
                R.drawable.bg19,
        };

        Random rand = new Random();

        holder.imageTask.setImageResource(images[rand.nextInt(images.length)]);
        holder.imageStatusTask.setImageResource(holder.statusImage(task.getStatus()));
        holder.lblTitleTask.setText(task.getName());
        holder.lblStartDate.setText(task.getStart_date());
        holder.lblEndDate.setText(task.getEnd_date());
        holder.lblEndTime.setText(task.getEnd_time());
        holder.cardViewTask.setRadius(30);

        //  holder.cardViewTask.setCardBackgroundColor(Color.parseColor("#"+colors[rand.nextInt(colors.length)]));
        //holder.cardViewTask.setCardBackgroundColor(Color.parseColor("#" + holder.statusColor(task.getStatus())));

        holder.cardViewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.TASK_ID = task.getId();
                Constant.NAME_TASK = task.getName();
                Constant.DESCRIPTION_TASK = task.getDescription();
                Constant.START_DATE_TASK = task.getStart_date();
                Constant.END_DATE_TASK = task.getEnd_date();
                Constant.END_TIME_TASK = task.getEnd_time();
                Constant.STATUS_TASK_DT = task.getStatus();

                Intent intent = new Intent((TasksActivity) context, DetailTaskActivity.class);
                context.startActivity(intent);
                // Toast.makeText(context, "task_id:  " + task.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.cardViewTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Constant.TASK_ID = task.getId();
                holder.dialogInfoTask();

                //  String start_date = dateFormat.format(task.getStart_date());
                //load data task
                holder.dialogNameTask.setText(task.getName());
                holder.dialogStartDateTask.setText(task.getStart_date());
                holder.dialogEndDateTask.setText(task.getEnd_date());
                holder.dialogTimeTask.setText(task.getEnd_time());
                holder.dialogStatusTask.setText(task.getStatus());
                holder.dialogFilesTask.setText(String.valueOf(task.getFiles()));

                //  Toast.makeText(context, "LONG task_id:  " + task.getId(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_task.size();
    }


    class TaskHolder extends RecyclerView.ViewHolder {

        private TextView lblTitleTask, lblStartDate, lblEndDate, lblEndTime;
        private ImageView imageTask, imageStatusTask;
        private CardView cardViewTask;

        //dialog info task
        TextView dialogNameTask, dialogFilesTask, dialogStartDateTask, dialogEndDateTask, dialogTimeTask, dialogStatusTask;
        ImageButton dialogCloseBtnTask;


        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            cardViewTask = itemView.findViewById(R.id.cardViewTask);
            imageTask = itemView.findViewById(R.id.imageTask);
            imageStatusTask = itemView.findViewById(R.id.imageStatusTask);
            lblTitleTask = itemView.findViewById(R.id.lblTitleTask);
            lblStartDate = itemView.findViewById(R.id.lblStartDate);
            lblEndDate = itemView.findViewById(R.id.lblEndDate);
            lblEndTime = itemView.findViewById(R.id.lblEndTime);


        }

        public String statusColor(String status) {
            String color = "";
            if (status.equals("Abierto")) {
                color = "64B5F6";
            } else if (status.equals("Cancelado")) {
                color = "E57373";
            } else if (status.equals("Finalizado")) {
                color = "81C784";
            }
            return color;
        }

        public int statusImage(String status) {
            int image = 0;
            if (status.equals("Abierto")) {
                image = R.drawable.ic_pending;
            } else if (status.equals("Cancelado")) {
                image = R.drawable.ic_cancel;
            } else if (status.equals("Finalizado")) {
                image = R.drawable.ic_completed;
            } else if (status.equals("Atrasado")) {
                image = R.drawable.ic_overdue;
            }
            return image;
        }

        public void dialogInfoTask() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_task_info, null);
            builder.setView(view);
            initDialog(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            //  dialog.setCanceledOnTouchOutside(false);
            dialogCloseBtnTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        public void initDialog(View v) {
            dialogNameTask = (TextView) v.findViewById(R.id.dialogNameTask);
            dialogStartDateTask = (TextView) v.findViewById(R.id.dialogStartDateTask);
            dialogEndDateTask = (TextView) v.findViewById(R.id.dialogEndDateTask);
            dialogTimeTask = (TextView) v.findViewById(R.id.dialogTimeTask);
            dialogStatusTask = (TextView) v.findViewById(R.id.dialogStatusTask);
            dialogFilesTask = (TextView) v.findViewById(R.id.dialogFilesTask);
            dialogCloseBtnTask = (ImageButton) v.findViewById(R.id.dialogCloseBtnTask);
        }

    }

}