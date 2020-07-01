package co.desofsi.souriapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import co.desofsi.souriapp.R;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    public ViewPagerAdapter(Context context){
        this.context = context;
    }
    private int images[] = {
            R.drawable.d1,
            R.drawable.d2,
            R.drawable.d3,
    };
    private String titles[] = {
            "Atención",
            "Citas",
            "Operacion"
    };
    private String descriptions[] = {
            "Quédate en casa si te sientes mal. Si tienes fiebre, tos y dificultad para respirar, busca atención médica.",
            "La odontología es una de las ciencias de la salud que se encarga del diagnóstico, tratamiento y prevención.",
            "La odontología es una de las ciencias de la salud que se encarga del diagnóstico, tratamiento y prevención."
    };

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==(LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_pager,container,false);
        ImageView imageView = view.findViewById(R.id.img_view_pager);
        TextView title = view.findViewById(R.id.text_title_view_pager);
        TextView description = view.findViewById(R.id.text_description_view_pager);
        imageView.setImageResource(images[position]);
        title.setText(titles[position]);
        description.setText(descriptions[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
