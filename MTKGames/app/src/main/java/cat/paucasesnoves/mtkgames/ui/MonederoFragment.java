package cat.paucasesnoves.mtkgames.ui;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Usuario;
import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;

import static android.content.Context.MODE_PRIVATE;

public class MonederoFragment extends Fragment {

    private GestorBBDD gestor;
    private Usuario user;
    private int saldo;

    private TextView num_saldo;

    private ImageView btn_comprar_cincuenta;
    private ImageView btn_comprar_cien;
    private ImageView btn_comprar_dos;
    private ImageView btn_comprar_cuatro;
    private ImageView btn_comprar_seis;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_monedero, container, false);

        gestor = new GestorBBDD();
        obtenDatosLogin();
        num_saldo = rootView.findViewById(R.id.monedero_num_saldo);
        num_saldo.setText(String.valueOf(user.getSaldo()));

        btn_comprar_cincuenta = rootView.findViewById(R.id.monedero_comprar_cincuenta);
        btn_comprar_cien = rootView.findViewById(R.id.monedero_comprar_cien);
        btn_comprar_dos = rootView.findViewById(R.id.monedero_comprar_dos);
        btn_comprar_cuatro = rootView.findViewById(R.id.monedero_comprar_cuatro);
        btn_comprar_seis = rootView.findViewById(R.id.monedero_comprar_seis);

        btn_comprar_cincuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saldo = user.getSaldo() + 50;
                user.setSaldo(saldo);
                new RequestAsync().execute();
            }
        });

        btn_comprar_cien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saldo = user.getSaldo() + 110;
                user.setSaldo(saldo);
                new RequestAsync().execute();
            }
        });

        btn_comprar_dos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saldo = user.getSaldo() + 240;
                user.setSaldo(saldo);
                new RequestAsync().execute();
            }
        });

        btn_comprar_cuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saldo = user.getSaldo() + 450;
                user.setSaldo(saldo);
                new RequestAsync().execute();
            }
        });

        btn_comprar_seis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saldo = user.getSaldo() + 630;
                user.setSaldo(saldo);
                new RequestAsync().execute();
            }
        });

        return rootView;
    }

    public void obtenDatosLogin() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("datos_login", MODE_PRIVATE);
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
                String parametros = "{\"IdUsuario\": \"" + user.getId() + "\", \"NickName\": \"" + user.getNickName() + "\", \"Saldo\": \"" +
                        saldo + "\"}";

                return gestor.enviarPost("http://192.168.0.167:45455/api/usuario/cargarmonedero", parametros);
            } catch (Exception e) {
                return "Excepción: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            // Obtenemos respuesta de la api
            if (s != null) {
                try {
                    // Cogemos el JSONObject del JSON file
                    JSONObject usuarioObj = new JSONObject(s);

                    Usuario usuario = new Usuario(usuarioObj.getInt("IdUsuario"), usuarioObj.getString("Nombre"),
                            usuarioObj.getString("NickName"), usuarioObj.getString("Email"), usuarioObj.getString("Password"),
                            usuarioObj.getString("FechaNacimiento"), usuarioObj.getInt("FotoPerfil"),
                            usuarioObj.getString("UltimaConexion"), usuarioObj.getInt("Saldo"), usuarioObj.getInt("Login"),
                            usuarioObj.getInt("IdBiblioteca"));

                    // Borramos el Shared antiguo
                    borrarShared();

                    // Guardar usuario en el Shared
                    guardarDatosLogin(usuario);

                    num_saldo.setText(String.valueOf(usuario.getSaldo()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Método para guardar todos los datos de un usuario en un xml
     *
     * @param usuario
     */
    public void guardarDatosLogin(Usuario usuario) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("datos_login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(usuario);
        editor.putString("usuario", json);
        editor.apply();
    }

    public void borrarShared() {
        getContext().getSharedPreferences("datos_login", 0).edit().clear().apply();
    }
}