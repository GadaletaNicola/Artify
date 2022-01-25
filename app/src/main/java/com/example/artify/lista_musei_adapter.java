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

public class lista_musei_adapter extends RecyclerView.Adapter<lista_musei_adapter.ListAdapterHolder> {
    private ArrayList<Museo> musei;
    private Context context;



    public lista_musei_adapter(ArrayList<Museo>musei, Context context){
        this.musei = musei;
        this.context = context;
    }


    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listamuseiadapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder holder, int position) {
        StorageReference gsRef = FirebaseStorage.getInstance().getReferenceFromUrl(musei.get(position).getImg());


        gsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.logoMuseo);

            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        holder.nomeMuseo.setText(musei.get(position).getNome());

        holder.parentLayout.setOnClickListener(view -> {
            Intent switcher  = new Intent(context,MenuPreScelta.class);
            switcher.putExtra("MuseoCliccato",musei.get(position).getNome());
            switcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(switcher);
        });

    }

    @Override
    public int getItemCount() {
        return musei.size();
    }

    static class ListAdapterHolder extends RecyclerView.ViewHolder{
        private TextView nomeMuseo;
        private ImageView logoMuseo;
        private ConstraintLayout parentLayout;

        public ListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            nomeMuseo = itemView.findViewById(R.id.NomeMuseo);
            logoMuseo = itemView.findViewById(R.id.LogoMuseo);
            parentLayout = itemView.findViewById(R.id.CardMuseo);
        }
    }
}
