package cat.paucasesnoves.mtkgames.ui;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Usuario;
import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;

import static android.content.Context.MODE_PRIVATE;

public class ModificarFragment extends Fragment {

    private GestorBBDD gestor = new GestorBBDD();
    private Usuario user;

    private EditText nombreEdit;
    private EditText passEdit;
    private EditText passConfirmEdit;
    private EditText emailEdit;

    private String email;
    private String nombre;
    private String pass;
    private String passConfirm;

    private CardView boton_modificar;
    private Drawable icono;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.modificar_perfil_layout, container, false);

        nombreEdit = rootView.findViewById(R.id.modificar_nombre);
        emailEdit = rootView.findViewById(R.id.modificar_email);
        passEdit = rootView.findViewById(R.id.modificar_password);
        passConfirmEdit = rootView.findViewById(R.id.modificar_password_confirma);
        boton_modificar = rootView.findViewById(R.id.boton_modificar);
        icono = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error, null);
        icono.setBounds(0, 0, icono.getIntrinsicWidth(), icono.getIntrinsicHeight());

        obtenDatosLogin();
        nombreEdit.setText(user.getNombre());
        emailEdit.setText(user.getEmail());

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEdit.getText().toString();
                nombre = nombreEdit.getText().toString();
                pass = passEdit.getText().toString();
                passConfirm = passConfirmEdit.getText().toString();
                if (compruebaDatos(nombre, email, pass, passConfirm)) {
                    new RequestAsync().execute();
                }
            }
        });

        return rootView;
    }

    public void obtenDatosLogin() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datos_login", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuario", null);
        Type type = new TypeToken<Usuario>() {
        }.getType();
        user = gson.fromJson(json, type);
    }

    /**
     * Comprueba que los campos no están vacios
     *
     * @param nombre      nombre del usuario
     * @param email       email del usuario
     * @param pass        contraseña del usuario
     * @param passConfirm confirmación de la contraseña del usuario
     * @return true si todo es correcto
     */
    public boolean compruebaDatos(String nombre, String email, String pass, String passConfirm) {
        if (("").equals(nombre.trim()) || ("").equals(email.trim()) || ("").equals(pass.trim()) || ("").equals(passConfirm.trim())) {
            if (("").equals(nombre.trim())) {
                nombreEdit.setError("Campo obligatorio.", icono);
            }
            if (("").equals(email.trim())) {
                emailEdit.setError("Campo obligatorio.", icono);
            }
            if (("").equals(pass.trim())) {
                passEdit.setError("Campo obligatorio.", icono);
            }
            if (("").equals(passConfirm.trim())) {
                passConfirmEdit.setError("Campo obligatorio.", icono);
            }
            return false;
        } else {
            boolean bolPass = false;
            boolean bolEmail = false;

            if (pass.trim().length() <= 6) {
                passEdit.setError("La contraseña debe contener al menos 6 carácteres", icono);
                passConfirmEdit.setError("La contraseña debe contener al menos 6 carácteres", icono);
                return false;
            }

            if ((pass).equals(passConfirm)) {
                bolPass = true;
            } else {
                passEdit.setError("Las contraseñas no coinciden", icono);
                passConfirmEdit.setError("Las contraseñas no coinciden", icono);
            }


            // Comprobar Email
            // Patrón para validar el email
            Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");

            Matcher mather = pattern.matcher(email);

            if (mather.find()) {
                bolEmail = true;
            } else {
                emailEdit.setError("Email incorrecto, prueba con uno diferente", icono);
            }

            return bolEmail && bolPass;
        }
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
                String parametros = "{\"Nombre\": \"" + nombre + "\", \"Email\": \"" + email + "\", \"Password\": \"" + pass + "\", " +
                        "\"IdUsuario\": " + user.getId() + ", \"NickName\": \"" + user.getNickName() + "\"}";

                return gestor.enviarPost("http://192.168.0.167:45455/api/usuario/modificarperfil", parametros);
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

                    Snackbar.make(getView(), "El usuario ha sido modificado correctamente", 5000).show();
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
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("datos_login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(usuario);
        editor.putString("usuario", json);
        editor.apply();
    }

    public void borrarShared() {
        getActivity().getApplicationContext().getSharedPreferences("datos_login", 0).edit().clear().apply();
    }

}