package cat.paucasesnoves.mtkgames.ui;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Juego;
import cat.paucasesnoves.mtkgames.entidades.Usuario;
import cat.paucasesnoves.mtkgames.utilidades.BibliotecaAdapter;
import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;

import static android.content.Context.MODE_PRIVATE;

public class BibliotecaFragment extends Fragment {

    private TextView numJuegos;
    private RecyclerView biblioteca;
    private String opcion;
    private GestorBBDD gestor = new GestorBBDD();
    private Usuario user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.biblioteca_layout, container, false);

        numJuegos = rootView.findViewById(R.id.biblioteca);
        biblioteca = rootView.findViewById(R.id.biblioteca_recycler);
        biblioteca.setHasFixedSize(true);
        biblioteca.setItemAnimator(new DefaultItemAnimator());
        biblioteca.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        obtenDatosLogin();
        new RequestAsync().execute("numero");

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
                if (("numero").equals(opcion)) {
                    return gestor.enviarGet("http://192.168.0.167:45455/api/biblioteca/obtenernumjuegos/" + user.getIdBiblioteca());
                }
                return gestor.enviarGet("http://192.168.0.167:45455/api/biblioteca/obtenerjuegos/" + user.getIdBiblioteca());
            } catch (Exception e) {
                return "Excepción: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            // Obtenemos respuesta de la api
            if (s != null) {
                if (("numero").equals(opcion)) {
                    if (Integer.parseInt(s) == 1) {
                        numJuegos.setText("Actualmente tienes " + s + " juego.");
                    } else if (Integer.parseInt(s) == 0) {
                        numJuegos.setText("Actualmente no tienes ningún juego.");
                    } else {
                        numJuegos.setText("Actualmente tienes " + s + " juegos.");
                    }
                    opcion = "";
                    new RequestAsync().execute();
                } else {
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

                        BibliotecaAdapter adapter = new BibliotecaAdapter(juegos, getContext());
                        biblioteca.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void obtenDatosLogin() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datos_login", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuario", null);
        Type type = new TypeToken<Usuario>() {}.getType();
        user = gson.fromJson(json, type);
    }

}
