package cat.paucasesnoves.mtkgames.ui.tienda;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Juego;
import cat.paucasesnoves.mtkgames.entidades.Usuario;
import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;

public class JuegoActivity extends AppCompatActivity {

    private int id;
    private GestorBBDD gestor = new GestorBBDD();
    private ExpandableTextView expandableTextView;
    private ImageView juego_imagen;
    private TextView juego_nombre;
    private TextView juego_desarrollador;
    private TextView juego_nota;
    private RatingBar estrella_nota;
    private TextView juego_comprar;
    private Juego juegoObtenido;
    private CardView boton_comprar;
    private boolean comprar = false;
    private Usuario user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_juego);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getInt("key");
        }

        new RequestAsync().execute();

        expandableTextView = findViewById(R.id.expand_text_view);
        juego_imagen = findViewById(R.id.juego_imagen);
        juego_nombre = findViewById(R.id.juego_nombre);
        juego_desarrollador = findViewById(R.id.juego_desarrollador);
        juego_nota = findViewById(R.id.juego_nota);
        estrella_nota = findViewById(R.id.juego_nota_rating);
        estrella_nota.setActivated(false);
        juego_comprar = findViewById(R.id.juego_comprar);
        boton_comprar = findViewById(R.id.card_view_comprar);

        obtenDatosLogin();
        boton_comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Snackbar.make(v,"Debes iniciar sesión para poder comprar un juego.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    comprar = true;
                    new RequestAsync().execute();
                }
            }
        });
    }

    public void obtenDatosLogin() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("datos_login", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuario", null);
        Type type = new TypeToken<Usuario>() {}.getType();
        user = gson.fromJson(json, type);
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
                if (comprar) {
                    String parametros = "{\"IdJuego\": \"" + id + "\", \"IdBiblioteca\": \"" + user.getId() + "\"}";
                    return gestor.enviarPost("http://192.168.0.167:45455/api/juego/comprarjuego", parametros);
                }
                return gestor.enviarGet("http://192.168.0.167:45455/api/juego/obtenerjuegoid/" + id);
            } catch (Exception e) {
                return "Excepción: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            // Obtenemos respuesta de la api
            if (s != null) {
                try {
                    if (!comprar) {
                        // Cogemos el JSONObject del JSON file
                        JSONObject juego = new JSONObject(s);

                        juegoObtenido = new Juego(juego.getInt("IdJuego"), juego.getString("Nombre"), juego.getString("Descripcion"),
                                juego.getString("ImagenJuego").getBytes(), juego.getString("IconoJuego").getBytes(),
                                juego.getString("EmpresaDesarrollo"), juego.getDouble("Nota"), juego.getInt("Precio"),
                                juego.getInt("NumeroVentas"), juego.getInt("IdCategoria"));

                        System.out.println(s);
                        expandableTextView.setText(juegoObtenido.getDescripcion());
                        juego_nombre.setText(juegoObtenido.getNombre());
                        juego_desarrollador.setText(juegoObtenido.getEmpresaDesarrollo());
                        juego_nota.setText(String.valueOf(juegoObtenido.getNota()));
                        byte[] imageAsBytes = Base64.decode(juegoObtenido.getIconoJuego(), Base64.DEFAULT);
                        juego_imagen.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                        estrella_nota.setRating(Float.parseFloat(String.valueOf(juegoObtenido.getNota())));
                        juego_comprar.setText(String.valueOf(juegoObtenido.getPrecio()));
                    } else {
                        switch (s) {
                            case "\"Error\"":
                                Toast.makeText(getApplicationContext(), "No tienes las suficientes gemas para comprar este juego", Toast.LENGTH_LONG).show();
                                break;
                            case "\"Repetido\"":
                                Toast.makeText(getApplicationContext(), "Ya has adquirido este juego previamente", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                comprar = false;
                                user.setSaldo(user.getSaldo() - juegoObtenido.getPrecio());
                                guardarDatosLogin(user);
                                Toast.makeText(getApplicationContext(), juegoObtenido.getNombre() + " ha sido añadido a su biblioteca", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Método para guardar todos los datos de un usuario en un xml
     * @param usuario
     */
    public void guardarDatosLogin(Usuario usuario) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("datos_login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(usuario);
        editor.putString("usuario", json);
        editor.apply();
    }
}