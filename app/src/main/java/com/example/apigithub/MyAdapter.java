package com.example.apigithub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<UserGit> ListaUsers;
    private Context context;

    // constructor
    public MyAdapter(ArrayList<UserGit> ListaUsers, Context context){
        this.ListaUsers = ListaUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserGit userGit= ListaUsers.get(position);
        holder.item_nome.setText(userGit.getName());
        holder.item_Login.setText(userGit.getLogin());
    }

    @Override
    public int getItemCount() {
        return ListaUsers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView item_Login;
        public TextView item_nome;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            item_Login = itemLayoutView.findViewById(R.id.item_Login);
            item_nome = itemLayoutView.findViewById(R.id.item_nome);
        }
    }
}
