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

public class lista_opere_adapter extends RecyclerView.Adapter<lista_opere_adapter.ListAdapterHolder>{
    private final ArrayList<Opera> opere;
    private final Context context;
    private final Intent contextIntent;

    public lista_opere_adapter(@NonNull ArrayList<Opera> opere, Context context,Intent contextIntent){
        this.opere = opere;
        this.context = context;
        this.contextIntent = contextIntent;
    }

    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_opere_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder holder, int position) {
        StorageReference gsRef = FirebaseStorage.getInstance().getReferenceFromUrl(opere.get(position).getImg());

        gsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.immagineOpera);

            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        holder.nomeOpera.setText(opere.get(position).getTitolo());
        holder.parentLayout.setOnClickListener(view -> {
            /*
                Implementare onClick:
                Intent swap  = new Intent(context,ListaOpere.class);
                context.startActivity(swap);
            */
        });
    }

    @Override
    public int getItemCount() {
        return opere.size();
    }


    static class ListAdapterHolder extends RecyclerView.ViewHolder {
        private final TextView nomeOpera;
        private final ImageView immagineOpera;
        private final ConstraintLayout parentLayout;

        public ListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            nomeOpera = itemView.findViewById(R.id.NomeOpera);
            immagineOpera = itemView.findViewById(R.id.LogoOpera);
            parentLayout = itemView.findViewById(R.id.CardOpera);

        }
    }
}
