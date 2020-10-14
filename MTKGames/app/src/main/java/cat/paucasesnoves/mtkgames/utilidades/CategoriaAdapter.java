package cat.paucasesnoves.mtkgames.utilidades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Categoria;
import cat.paucasesnoves.mtkgames.ui.CategoriaActivity;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Categoria> categorias = new ArrayList<>();
    private Bundle idCategoria;

    public CategoriaAdapter(ArrayList<Categoria> mCategoria,  @NonNull Context context) {
        this.categorias = mCategoria;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre_categoria;

        public ViewHolder(View view) {
            super(view);

            nombre_categoria = view.findViewById(R.id.nombre_categoria);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CategoriaActivity.class);
                    idCategoria = new Bundle();
                    int position = getAdapterPosition();
                    idCategoria.putInt("key", categorias.get(position).getId());
                    idCategoria.putString("nombre", categorias.get(position).getNombre());
                    intent.putExtras(idCategoria);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public CategoriaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.categoria_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoriaAdapter.ViewHolder viewHolder, int position) {
        Categoria categoria = categorias.get(position);

        viewHolder.nombre_categoria.setText(categoria.getNombre());
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }
}