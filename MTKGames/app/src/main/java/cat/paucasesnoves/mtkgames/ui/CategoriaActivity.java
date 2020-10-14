package cat.paucasesnoves.mtkgames.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Juego;
import cat.paucasesnoves.mtkgames.utilidades.CategoriaJuegoAdapter;
import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;

public class CategoriaActivity extends AppCompatActivity {

    private int id;
    private String nombre;
    private GestorBBDD gestor = new GestorBBDD();
    private TextView categoria_titulo;
    private RecyclerView recycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getInt("key");
            nombre = b.getString("nombre");
        }

        categoria_titulo = findViewById(R.id.categorias_titulo);
        categoria_titulo.setText(nombre);

        recycler = findViewById(R.id.categoria_activity_recycler);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        new RequestAsync().execute();

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
                return gestor.enviarGet("http://192.168.0.167:45455/api/juego/obtenerjuegocategoria/" + id);
            } catch (Exception e) {
                return "Excepción: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            // Obtenemos respuesta de la api
            if (s != null) {
                ArrayList<Juego> juegos = new ArrayList<>();
                try {
                    // Cogemos el JSONObject del JSON file
                    JSONArray obj = new JSONArray(s);

                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject juego = obj.getJSONObject(i);

                        juegos.add(new Juego(juego.getInt("IdJuego"), juego.getString("Nombre"), juego.getString("Descripcion"),
                                juego.getString("ImagenJuego").getBytes(), juego.getString("IconoJuego").getBytes(),
                                juego.getString("EmpresaDesarrollo"), juego.getDouble("Nota"), juego.getInt("Precio"),
                                juego.getInt("NumeroVentas"), juego.getInt("IdCategoria")));
                    }

                    CategoriaJuegoAdapter adapter = new CategoriaJuegoAdapter(getApplicationContext(), juegos);
                    recycler.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
