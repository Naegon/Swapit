package com.swapit.swap_it.ServiceJava;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swapit.swap_it.R;

import java.util.List;


public class RecyclerServiceAdapter extends RecyclerView.Adapter<RecyclerServiceAdapter.MyViewHolder> {

    Context mContext;
    List<Service> myData;
    Dialog myDialog;

    public RecyclerServiceAdapter(Context mContext, List<Service> mData) {
        this.mContext =mContext;
        this.myData = mData;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_service, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_service);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vHolder.item_service.setOnClickListener(new View.OnClickListener() {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_titre.setText((myData).get(position).getTitre());
        holder.tv_nom.setText((myData).get(position).getNom());
        holder.tv_date.setText((myData).get(position).getDate());
        holder.tv_swap.setText((myData).get(position).getSwap());

    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout item_service;
        private TextView tv_titre;
        private TextView tv_nom;
        private TextView tv_date;
        private TextView tv_swap;

        public MyViewHolder(View itemView) {
            super(itemView);

            item_service = (LinearLayout) itemView.findViewById(R.id.service_item);
            tv_titre = (TextView) itemView.findViewById(R.id.titreAnnonce);
            tv_nom = (TextView) itemView.findViewById(R.id.auteurAnnonce);
            tv_date = (TextView) itemView.findViewById(R.id.dateAnnonce);
            tv_swap = (TextView) itemView.findViewById(R.id.textView_service_swap);
        }
    }
}
