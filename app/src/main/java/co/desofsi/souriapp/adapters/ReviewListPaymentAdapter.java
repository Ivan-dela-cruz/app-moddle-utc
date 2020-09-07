package co.desofsi.souriapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.desofsi.souriapp.R;
import co.desofsi.souriapp.models.DetailPayment;
import co.desofsi.souriapp.models.DetailTreatment;

public class ReviewListPaymentAdapter extends RecyclerView.Adapter<ReviewListPaymentAdapter.ListCategoriesHolder> {

    private Context context;
    private ArrayList<DetailPayment> list;


    public ReviewListPaymentAdapter(Context context, ArrayList<DetailPayment> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ListCategoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_payment, parent, false);
        return new ListCategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListCategoriesHolder holder, final int position) {

        final DetailPayment detail = list.get(position);

        holder.txt_description.setText(detail.getDescription());
        holder.txt_price_total.setText("$ " + detail.getTotal());
        holder.txt_price_unit.setText("Pago N° "+detail.getDues());
        holder.txt_cant.setText("" + detail.getStatus());

        switch (detail.getStatus()) {
            case "pagado":
                holder.imageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                break;
            case "pendiente":
                holder.imageView.setImageResource(R.drawable.ic_baseline_progress_24);
                break;
            case "anulado":
                holder.imageView.setImageResource(R.drawable.ic_baseline_close_24);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ListCategoriesHolder extends RecyclerView.ViewHolder {

        private TextView txt_description, txt_price_total,txt_price_unit, txt_cant;
        private ImageView imageView;


        public ListCategoriesHolder(@NonNull View itemView) {
            super(itemView);
            txt_description = itemView.findViewById(R.id.item_revie_order_list_products_description_id);
            txt_price_unit = itemView.findViewById(R.id.item_revie_order_list_products_price_unit_id);
            txt_price_total = itemView.findViewById(R.id.item_revie_order_list_products_price_total_id);
            txt_cant = itemView.findViewById(R.id.item_review_order_list_products_text_cant);
            imageView =  itemView.findViewById(R.id.imageView);

        }


    }
}
