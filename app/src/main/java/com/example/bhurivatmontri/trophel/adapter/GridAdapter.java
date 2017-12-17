package com.example.bhurivatmontri.trophel.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.Camera;
import com.example.bhurivatmontri.trophel.Home;
import com.example.bhurivatmontri.trophel.R;

import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    List<EndangeredItem> mItems;
    public Context mContext;
    public Fragment replaceFragment;

    public GridAdapter() {
        super();
      //  this.mContext = context;
        mItems = new ArrayList<EndangeredItem>();
        EndangeredItem nama = new EndangeredItem();
        nama.setName("AngKhang");
        nama.setThumbnail(R.drawable.att_angkhang);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("DoilLuangchiangdao");
        nama.setThumbnail(R.drawable.att_doiluangchiangdao);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("DoiPhaHomPok");
        nama.setThumbnail(R.drawable.att_doiphahompok);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("Kewmaepan");
        nama.setThumbnail(R.drawable.att_kewmaepan);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("KunChangKian");
        nama.setThumbnail(R.drawable.att_kunchangkian);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("Monjong");
        nama.setThumbnail(R.drawable.att_monjong);
        mItems.add(nama);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_attraction, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final EndangeredItem nature = mItems.get(i);
        viewHolder.tvspecies.setText(nature.getName());
        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
        viewHolder.tvspecies.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(), "-->" + nature.getName() + "<--", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(v.getContext(), Camera.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgThumbnail;
        public TextView tvspecies;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_Attraction);
            tvspecies = (TextView) itemView.findViewById(R.id.name_Attraction);
            tvspecies.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(itemView.getContext(), "default", Toast.LENGTH_SHORT).show();
        }
    }

    public static interface AdapterCallback {
        void onMethodCallback();
    }
}