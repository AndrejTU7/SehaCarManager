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
import com.studioadriatic.sehacarmanager.models.LocationModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Andrej on 15.2.2016..
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
    private Context mContext;
    private List<LocationModel> mLocationModelList;
    private onLocationClickListener mOnLocationClickListener;

    public LocationAdapter(Context context, List<LocationModel> locationModelList) {
        this.mContext = context;
        this.mLocationModelList = locationModelList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.universal_adapter_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(LocationAdapter.ViewHolder holder, int position) {
        final LocationModel model = mLocationModelList.get(position);
        holder.subtitle.setText(model.getAddress());
        holder.title.setText(model.getName());
        holder.image.setImageResource(R.drawable.def_location);

        holder.item_root.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnLocationClickListener != null) {
                mOnLocationClickListener.onLocationClick(model);
            }
        }});
    }





    @Override
    public int getItemCount() {
        return mLocationModelList.size();
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
    public void setOnLocationClickListener(onLocationClickListener mOnLocationClickListener) {
        this.mOnLocationClickListener = mOnLocationClickListener;
    }

    public interface onLocationClickListener {
        void onLocationClick(LocationModel locationModel);
    }
}









