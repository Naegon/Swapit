package com.swapit.swap_it.SoutienJava;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swapit.swap_it.R;

import java.util.List;

public class RecyclerSoutienAdapter extends RecyclerView.Adapter<RecyclerSoutienAdapter.MyViewHolder> {
    Context mContext;
    List<Soutien> myData;
    Dialog myDialog;

    public RecyclerSoutienAdapter(Context mContext, List<Soutien> mData) {
        this.mContext =mContext;
        this.myData = mData;
    }


    @Override
    public RecyclerSoutienAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_soutien, parent, false);
        final RecyclerSoutienAdapter.MyViewHolder vHolder = new RecyclerSoutienAdapter.MyViewHolder(v);

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_soutien);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vHolder.item_soutien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView dialog_titre_tv = (TextView) myDialog.findViewById(R.id.dialog_titre);
                TextView dialog_nom_tv = (TextView) myDialog.findViewById(R.id.dialog_auteur);
                TextView dialog_date_tv = (TextView) myDialog.findViewById(R.id.dialog_date);
                TextView dialog_swap_tv = (TextView) myDialog.findViewById(R.id.textView_dialog_swap);
                TextView dialog_description_tv = (TextView) myDialog.findViewById(R.id.dialog_description);
                dialog_titre_tv.setText(myData.get(vHolder.getAdapterPosition()).getTitre());
                dialog_nom_tv.setText(myData.get(vHolder.getAdapterPosition()).getNom());
                dialog_date_tv.setText(myData.get(vHolder.getAdapterPosition()).getDate());
                dialog_swap_tv.setText(myData.get(vHolder.getAdapterPosition()).getSwap());
                dialog_description_tv.setText(myData.get(vHolder.getAdapterPosition()).getDescription());
                myDialog.show();
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerSoutienAdapter.MyViewHolder holder, int position) {
        holder.tv_titre.setText((myData).get(position).getTitre());
        holder.tv_nom.setText((myData).get(position).getNom());
        holder.tv_date.setText((myData).get(position).getDate());
        holder.tv_swap.setText((myData).get(position).getSwap());
        holder.img_cat.setImageResource((myData).get(position).getCat());
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout item_soutien;
        private TextView tv_titre;
        private TextView tv_nom;
        private TextView tv_date;
        private TextView tv_swap;
        private ImageView img_cat;

        public MyViewHolder(View itemView) {
            super(itemView);

            item_soutien = (LinearLayout) itemView.findViewById(R.id.soutien_item);
            tv_titre = (TextView) itemView.findViewById(R.id.titreAnnonce);
            tv_nom = (TextView) itemView.findViewById(R.id.auteurAnnonce);
            tv_date = (TextView) itemView.findViewById(R.id.dateAnnonce);
            tv_swap = (TextView) itemView.findViewById(R.id.textView_soutien_swap);
            img_cat = (ImageView) itemView.findViewById(R.id.cat_annonce_img);
        }
    }
}
