package com.studioadriatic.sehacarmanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.studioadriatic.sehacarmanager.R;
import com.studioadriatic.sehacarmanager.models.Car;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by andrej.tkalec1991@gmail.com
 */
public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {


    private Context mContext;
    private List<Car> mCarList;
    private onCarClickListener mOnCarClickListener;

    public CarsAdapter(Context context, List<Car> carList) {
        this.mContext = context;
        this.mCarList = carList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.universal_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Car item = mCarList.get(position);
        holder.title.setText(item.getName());
        String registration = item.getRegistration();
        if (!registration.equals("")) {
            holder.subtitle.setText(registration);
        }
        holder.image.setImageResource(R.drawable.image_car);
        holder.item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCarClickListener != null) {
                    mOnCarClickListener.onCarClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCarList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.civ_image)
        CircularImageView image;
        @Bind(R.id.tv_title)
        TextView title;
        @Bind(R.id.tv_subtitle)
        TextView subtitle;
        @Bind(R.id.item_root)
        RelativeLayout item_root;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnCarClickListener(onCarClickListener mOnCarClickListener) {
        this.mOnCarClickListener = mOnCarClickListener;
    }

    public interface onCarClickListener {
        void onCarClick(Car car);
    }
}
