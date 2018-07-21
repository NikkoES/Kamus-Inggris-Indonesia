package io.github.nikkoes.kamusinggrisindonesia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.nikkoes.kamusinggrisindonesia.R;
import io.github.nikkoes.kamusinggrisindonesia.model.Kamus;

public class KamusAdapter extends RecyclerView.Adapter<KamusAdapter.KamusHolder> {

    private ArrayList<Kamus> mData = new ArrayList<>();
    private Context context;

    public KamusAdapter(Context context) {
        this.context = context;
    }

    public void addItem(ArrayList<Kamus> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void replaceItem(ArrayList<Kamus> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public KamusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kamus, parent, false);
        return new KamusHolder(view);
    }

    @Override
    public void onBindViewHolder(KamusHolder holder, int position) {
        holder.txtKata.setText(mData.get(position).getKata());
        holder.txtDeskripsi.setText(mData.get(position).getDeskripsi());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class KamusHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_kata)
        TextView txtKata;
        @BindView(R.id.txt_deskripsi)
        TextView txtDeskripsi;

        public KamusHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
