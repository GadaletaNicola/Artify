package com.example.artify;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

public class listadapter extends RecyclerView.Adapter<listadapter.ListAdapterHolder> {
    private ArrayList<Percorso> percorsi = null;
    private Context context = null;



    public listadapter(@NonNull ArrayList<Percorso> percorsi,Context context){
            this.percorsi = percorsi;
            this.context = context;
    }


    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listadapeter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder holder, int position) {


        StorageReference gsRef = FirebaseStorage.getInstance().getReferenceFromUrl(percorsi.get(position).getImg());

        gsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.immaginePercorso);

            }
        });
        holder.nomePercorso.setText(percorsi.get(position).getNomePercorso());
    }

    @Override
    public int getItemCount() {
        return percorsi.size();
    }

    static class ListAdapterHolder extends RecyclerView.ViewHolder{
        TextView nomePercorso = null;
        ImageView immaginePercorso = null;
        public ListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            nomePercorso = itemView.findViewById(R.id.NomePercorso);
            immaginePercorso = itemView.findViewById(R.id.LogoPercorso);
        }
    }
}
