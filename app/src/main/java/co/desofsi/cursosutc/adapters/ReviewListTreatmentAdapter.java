package co.desofsi.cursosutc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.desofsi.cursosutc.R;
import co.desofsi.cursosutc.models.DetailTreatment;

public class ReviewListTreatmentAdapter extends RecyclerView.Adapter<ReviewListTreatmentAdapter.ListCategoriesHolder> {

    private Context context;
    private ArrayList<DetailTreatment> list;


    public ReviewListTreatmentAdapter(Context context, ArrayList<DetailTreatment> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ListCategoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_treatment, parent, false);
        return new ListCategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListCategoriesHolder holder, final int position) {

        final DetailTreatment detailTreatment = list.get(position);

        holder.txt_description.setText(detailTreatment.getProcedure());
        holder.txt_price_total.setText("$ " + detailTreatment.getTotal());
        holder.txt_price_unit.setText("$ "+detailTreatment.getPrice());
        holder.txt_cant.setText("" + detailTreatment.getQuantity());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ListCategoriesHolder extends RecyclerView.ViewHolder {

        private TextView txt_description, txt_price_total,txt_price_unit, txt_cant;


        public ListCategoriesHolder(@NonNull View itemView) {
            super(itemView);
            txt_description = itemView.findViewById(R.id.item_revie_order_list_products_description_id);
            txt_price_unit = itemView.findViewById(R.id.item_revie_order_list_products_price_unit_id);
            txt_price_total = itemView.findViewById(R.id.item_revie_order_list_products_price_total_id);
            txt_cant = itemView.findViewById(R.id.item_review_order_list_products_text_cant);

        }


    }
}
