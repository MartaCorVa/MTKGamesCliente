package cat.paucasesnoves.mtkgames.ui.iniciar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import cat.paucasesnoves.mtkgames.MainActivity;
import cat.paucasesnoves.mtkgames.MainLogged;
import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Usuario;
import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;

public class LoginAutomatico extends AppCompatActivity {

    private GestorBBDD gestor = new GestorBBDD();
    private String nickname;
    private String password;
    private Usuario user;
    private Bundle b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = getIntent().getExtras();
        obtenDatosLogin();
    }

    public void obtenDatosLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("datos_login", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuario", null);
        Type type = new TypeToken<Usuario>() {}.getType();
        user = gson.fromJson(json, type);

        if (user == null) {
            if (b != null) {
                nickname = b.getString("nickname");
                password = b.getString("password");
                new RequestAsync().execute();
            }
            startActivity(new Intent(this, MainActivity.class));
        } else {
            nickname = user.getNickName();
            password = user.getPassword();
            new RequestAsync().execute();
        }
    }

    /**
     * Creamos una clase Asincrona y la llamamos RequestAsync.
     * Las clases asincronas se ejecutan independientemente de los otros procesos.
     * Así que ejecutará el proceso en segundo plano.
     */
    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                String parametros = "{\"NickName\": \"" + nickname + "\", \"Password\": \"" + password + "\"}";

                return gestor.enviarPost("http://192.168.0.167:45455/api/usuario/iniciarsesion", parametros);
            }
            catch(Exception e){
                return "Excepción: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            // Obtenemos respuesta de la api
            if(s!=null){
                if (("\"Error\"").equals(s)) {
                    Toast.makeText(getApplicationContext(), "El nombre de usuario o la contraseña no son correctos", Toast.LENGTH_LONG).show();
                } else {
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

                        // Redirigimos a la activity de usuario registrado
                        startActivity(new Intent(getApplicationContext(), MainLogged.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    public void borrarShared() {
        getApplicationContext().getSharedPreferences("datos_login", 0).edit().clear().apply();
    }
}