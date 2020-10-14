package cat.paucasesnoves.mtkgames.utilidades;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Juego;

public class BibliotecaAdapter extends RecyclerView.Adapter<BibliotecaAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Juego> juegos = new ArrayList<>();

    public BibliotecaAdapter(ArrayList<Juego> mJuegos, @NonNull Context context) {
        this.juegos = mJuegos;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagen_juego;
        public TextView nombre_juego;

        public ViewHolder(View view) {
            super(view);

            imagen_juego = view.findViewById(R.id.imagen_juego);
            nombre_juego = view.findViewById(R.id.nombre_juego);
        }
    }

    @Override
    public BibliotecaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.juego_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BibliotecaAdapter.ViewHolder viewHolder, int position) {
        Juego juego = juegos.get(position);

        viewHolder.nombre_juego.setText(juego.getNombre());
        byte[] imageAsBytes = Base64.decode(juego.getImagenJuego(), Base64.DEFAULT);
        viewHolder.imagen_juego.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        viewHolder.imagen_juego.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return juegos.size();
    }
}
