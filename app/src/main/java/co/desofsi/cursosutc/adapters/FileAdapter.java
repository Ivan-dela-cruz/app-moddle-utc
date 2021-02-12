package co.desofsi.cursosutc.adapters;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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

import java.util.ArrayList;
import java.util.regex.Pattern;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.activities.DetailTaskActivity;
import co.desofsi.cursosutc.data.Constant;
import co.desofsi.cursosutc.models.File;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileHolder> {


    private Context context;
    private ArrayList<File> list_files;

    public FileAdapter(Context context, ArrayList<File> list) {
        this.context = context;
        this.list_files = list;
    }

    @NonNull
    @Override
    public FileAdapter.FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_files_task, parent, false);

        return new FileAdapter.FileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileAdapter.FileHolder holder, final int position) {

        final File file = list_files.get(position);
        holder.setFileIcon(Constant.URL + file.getUrl_file());
        holder.lblFileName.setText(file.getName());
        holder.cardViewFile.setRadius(30);

        holder.cardViewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constant.URL + file.getUrl_file();
                holder.startDownloading(url, file.getName());
                Toast.makeText(context, "Descargando...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_files.size();
    }


    class FileHolder extends RecyclerView.ViewHolder {

        private TextView lblFileName;
        private CardView cardViewFile;
        ImageView iconFiles;

        TextView dialogNameTask, dialogFilesTask, dialogStartDateTask, dialogEndDateTask, dialogTimeTask, dialogStatusTask;
        ImageButton dialogCloseBtnTask;

        public FileHolder(@NonNull View itemView) {
            super(itemView);
            cardViewFile = itemView.findViewById(R.id.cardViewFile);
            lblFileName = itemView.findViewById(R.id.lblFileName);
            iconFiles = itemView.findViewById(R.id.iconFiles);
        }

        public void startDownloading(String url, String filename) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle(filename);
            request.setDescription("Descargando...");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + System.currentTimeMillis());

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }

        private void setFileIcon(String url) {
            StringBuilder sBuilder = new StringBuilder(url);
            String reverseURL = sBuilder.reverse().toString();
            String[] splitURL = reverseURL.split(Pattern.quote("."));
            StringBuilder reverseEXT = new StringBuilder(splitURL[0]);
            String ext = reverseEXT.reverse().toString();

            System.out.println("EXT: " + ext);

            if (ext.equals("docx") | ext.equals("doc")) {
                iconFiles.setImageResource(R.drawable.ic_doc);
            } else if (ext.equals("pptx") | ext.equals("ppt")) {
                iconFiles.setImageResource(R.drawable.ic_ppt);
            } else if (ext.equals("csv") | ext.equals("xlsx") | ext.equals("xls")) {
                iconFiles.setImageResource(R.drawable.ic_xls);
            } else if (ext.equals("png") | ext.equals("jpg") | ext.equals("jpeg")) {
                iconFiles.setImageResource(R.drawable.ic_pictures);
            } else if (ext.equals("pdf")) {
                iconFiles.setImageResource(R.drawable.ic_pdf);
            } else {
                iconFiles.setImageResource(R.drawable.ic_clip);
            }
        }


    }

}