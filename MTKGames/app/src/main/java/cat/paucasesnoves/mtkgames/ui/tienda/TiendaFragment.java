package cat.paucasesnoves.mtkgames.ui.tienda;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Categoria;
import cat.paucasesnoves.mtkgames.entidades.Juego;
import cat.paucasesnoves.mtkgames.utilidades.BestSellerAdapter;
import cat.paucasesnoves.mtkgames.utilidades.CategoriaAdapter;
import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;

public class TiendaFragment extends Fragment {

    private GestorBBDD gestor = new GestorBBDD();
    private static String opcion = "";
    private RecyclerView recycler_bestsellers;
    private RecyclerView recycler_categorias;
    private ImageView primer_juego;
    private ImageView segundo_juego;
    private ImageView tercer_juego;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tienda, container, false);

        primer_juego = rootView.findViewById(R.id.primer_juego);
        segundo_juego = rootView.findViewById(R.id.segundo_juego);
        tercer_juego = rootView.findViewById(R.id.tercer_juego);
        new RequestAsync().execute("obtener_mejoresJuegos");

        recycler_bestsellers = rootView.findViewById(R.id.bestsellers_recycler);
        recycler_bestsellers.setHasFixedSize(true);
        recycler_bestsellers.setItemAnimator(new DefaultItemAnimator());
        recycler_bestsellers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        new RequestAsync().execute("obtener_bestsellers");

        recycler_categorias = rootView.findViewById(R.id.categorias_recycler);
        recycler_categorias.setHasFixedSize(true);
        recycler_categorias.setItemAnimator(new DefaultItemAnimator());
        recycler_categorias.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        new RequestAsync().execute("obtener_categorias");

        primer_juego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), JuegoActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", Integer.parseInt(String.valueOf(primer_juego.getContentDescription())));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        segundo_juego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), JuegoActivity.class);
                Bundle b = new Bundle();
                String id = (String) segundo_juego.getContentDescription();
                b.putInt("key", Integer.parseInt(String.valueOf(segundo_juego.getContentDescription())));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        tercer_juego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), JuegoActivity.class);
                Bundle b = new Bundle();
                String id = (String) tercer_juego.getContentDescription();
                b.putInt("key", Integer.parseInt(String.valueOf(tercer_juego.getContentDescription())));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        return rootView;
    }

    /**
     * Creamos una clase Asincrona y la llamamos RequestAsync.
     * Las clases asincronas se ejecutan independientemente de los otros procesos.
     * Así que ejecutará el proceso en segundo plano.
     */
    public class RequestAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                for (String s : strings) {
                    opcion = s;
                }
                switch (opcion) {
                    case "obtener_bestsellers":
                        return gestor.enviarGet("http://192.168.0.167:45455/api/juego/obtenerbestsellers");
                    case "obtener_categorias":
                        return gestor.enviarGet("http://192.168.0.167:45455/api/categoria/obtenercategorias");
                    case "obtener_mejoresJuegos":
                        return gestor.enviarGet("http://192.168.0.167:45455/api/juego/obtenermejorvalorados");
                }
            } catch (Exception e) {
                return "Excepción: " + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // Obtenemos respuesta de la api
            if (s != null) {
                switch (opcion) {
                    case "obtener_bestsellers":
                        recibirJuegos(s);
                        break;
                    case "obtener_categorias":
                        recibirCategorias(s);
                        break;
                    case "obtener_mejoresJuegos":
                        recibirJuegosValorados(s);
                        break;
                }
            }
        }
    }

    private void recibirJuegosValorados(String juegosRecibidos) {
        ArrayList<Juego> juegos = new ArrayList<>();
        try {
            // Cogemos el JSONObject del JSON file
            JSONArray obj = new JSONArray(juegosRecibidos);

            for (int i = 0; i < obj.length(); i++) {
                JSONObject juego = obj.getJSONObject(i);

                juegos.add(new Juego(juego.getInt("IdJuego"), juego.getString("Nombre"), juego.getString("Descripcion"),
                        juego.getString("ImagenJuego").getBytes(), juego.getString("IconoJuego").getBytes(),
                        juego.getString("EmpresaDesarrollo"), juego.getDouble("Nota"), juego.getInt("Precio"),
                        juego.getInt("NumeroVentas"), juego.getInt("IdCategoria")));
            }
            byte[] imageAsBytes = Base64.decode(juegos.get(0).getImagenJuego(), Base64.DEFAULT);
            primer_juego.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            primer_juego.setContentDescription(String.valueOf(juegos.get(0).getId()));

            byte[] imageAsBytes2 = Base64.decode(juegos.get(1).getImagenJuego(), Base64.DEFAULT);
            segundo_juego.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes2, 0, imageAsBytes2.length));
            segundo_juego.setContentDescription(String.valueOf(juegos.get(1).getId()));

            byte[] imageAsBytes3 = Base64.decode(juegos.get(2).getImagenJuego(), Base64.DEFAULT);
            tercer_juego.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes3, 0, imageAsBytes3.length));
            tercer_juego.setContentDescription(String.valueOf(juegos.get(2).getId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void recibirCategorias(String categoriasRecibidas) {
        ArrayList<Categoria> categorias = new ArrayList<>();
        try {
            // Cogemos el JSONObject del JSON file
            JSONArray obj = new JSONArray(categoriasRecibidas);

            for (int i = 0; i < obj.length(); i++) {
                JSONObject categoria = obj.getJSONObject(i);

                categorias.add(new Categoria(categoria.getInt("IdCategoria"), categoria.getString("Nombre")));
            }

            CategoriaAdapter categoriaAdapter = new CategoriaAdapter(categorias, getContext());
            recycler_categorias.setAdapter(categoriaAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void recibirJuegos(String juegosRecibidos) {
        ArrayList<Juego> juegos = new ArrayList<>();
        try {
            // Cogemos el JSONObject del JSON file
            JSONArray obj = new JSONArray(juegosRecibidos);

            for (int i = 0; i < obj.length(); i++) {
                JSONObject juego = obj.getJSONObject(i);

                juegos.add(new Juego(juego.getInt("IdJuego"), juego.getString("Nombre"), juego.getString("Descripcion"),
                        juego.getString("ImagenJuego").getBytes(), juego.getString("IconoJuego").getBytes(),
                        juego.getString("EmpresaDesarrollo"), juego.getDouble("Nota"), juego.getInt("Precio"),
                        juego.getInt("NumeroVentas"), juego.getInt("IdCategoria")));
            }

            BestSellerAdapter bestSellerAdapter = new BestSellerAdapter(juegos, getContext());
            recycler_bestsellers.setAdapter(bestSellerAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}