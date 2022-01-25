package com.example.artify;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

public class listadapter extends RecyclerView.Adapter<listadapter.ListAdapterHolder> {
    private final ArrayList<Zone> zone;
    private final Context context;
    private final Intent contextIntent;


    public listadapter(@NonNull ArrayList<Zone> zone, Context context,Intent contextIntent){
            this.zone = zone;
            this.context = context;
            this.contextIntent = contextIntent;
    }


    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listadapeter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder holder, int position) {
        StorageReference gsRef = FirebaseStorage.getInstance().getReferenceFromUrl(zone.get(position).getImg());


        gsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.immagineZona);

            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        holder.nomeZona.setText(zone.get(position).getNomeZona());

        holder.parentLayout.setOnClickListener(view -> {
            Intent switcher  = new Intent(context,ListaOpere.class);
            switcher.putExtra("TipoPercorso", contextIntent.getStringExtra("TipoPercorso"));
            switcher.putExtra("ZonaCliccata",zone.get(position).getNomeZona());
            switcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(switcher);
        });

    }

    @Override
    public int getItemCount() {
        return zone.size();
    }


    static class ListAdapterHolder extends RecyclerView.ViewHolder{
        private final TextView nomeZona;
        private final ImageView immagineZona;
        private final ConstraintLayout parentLayout;

        public ListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            nomeZona = itemView.findViewById(R.id.NomeZona);
            immagineZona = itemView.findViewById(R.id.LogoZona);
            parentLayout = itemView.findViewById(R.id.CardZona);

        }


    }

}
