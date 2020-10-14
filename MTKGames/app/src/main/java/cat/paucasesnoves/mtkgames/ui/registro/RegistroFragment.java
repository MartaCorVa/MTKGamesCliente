package cat.paucasesnoves.mtkgames.ui.registro;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.ui.iniciar.LoginAutomatico;
import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;


public class RegistroFragment extends Fragment {

    private GestorBBDD gestor = new GestorBBDD();
    private EditText registro_email;
    private EditText registro_password;
    private EditText registro_confirma;
    private EditText registro_nickname;
    private CardView boton_registro_borrar;
    private CardView boton_registro;
    private String email;
    private String password;
    private String confirma_password;
    private String nickname;
    private String opcion;

    private Boolean nickname_bool = false;
    private Boolean email_bool = false;
    private Boolean pass_bool = false;

    private Drawable icono;

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registro, container, false);
        registro_email = rootView.findViewById(R.id.registro_email);
        registro_password = rootView.findViewById(R.id.registro_password);
        registro_confirma = rootView.findViewById(R.id.registro_password_confirma);
        registro_nickname = rootView.findViewById(R.id.registro_nickname);
        boton_registro_borrar = rootView.findViewById(R.id.boton_registro_borrar);
        boton_registro = rootView.findViewById(R.id.boton_registro);

        icono = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error, null);
        icono.setBounds(0, 0, icono.getIntrinsicWidth(), icono.getIntrinsicHeight());

        boton_registro_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarCampos();
            }
        });

        boton_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = registro_email.getText().toString();
                password = registro_password.getText().toString();
                nickname = registro_nickname.getText().toString();
                confirma_password = registro_confirma.getText().toString();
                try {
                    comprobarDatos();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    private void borrarCampos() {
        registro_email.setText("");
        registro_password.setText("");
        registro_confirma.setText("");
        registro_nickname.setText("");
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
                for (String o : strings) {
                    opcion = o;
                }
                if (("registra").equals(opcion)) {
                    String parametros = "{\"NickName\": \"" + nickname + "\", \"Email\": \"" + email + "\", \"Password\": \"" + password + "\"}";
                    return gestor.enviarPost("http://192.168.0.167:45455/api/usuario/registrarusuario", parametros);
                } else {
                    String parametros = "{\"NickName\": \"" + nickname + "\"}";
                    return gestor.enviarPost("http://192.168.0.167:45455/api/usuario/compruebanickname/", parametros);
                }
            } catch (Exception e) {
                return "Excepción: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if ("registra".equals(opcion)) {
                if (("\"Registro\"").equals(s)) {
                    Intent intent = new Intent(getContext(), LoginAutomatico.class);
                    Bundle b = new Bundle();
                    b.putString("nickname", nickname);
                    b.putString("password", password);
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Se ha producido un error, intentelo de nuevo más tarde.", Toast.LENGTH_LONG).show();
                }
            } else {
                if (("\"Correcto\"").equals(s)) {
                    nickname_bool = true;
                } else {
                    registro_nickname.setError("Este nickname ya está en uso, prueba con otro", icono);
                }
            }
        }
    }

    private void comprobarDatos() throws JSONException {
        // Comprobar NickName
        if (("").equals(nickname.trim())) {
            registro_nickname.setError("Campo obligatorio.", icono);
        } else {
            new RequestAsync().execute();
        }

        // Comprobar Email
        // Patrón para validar el email
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");

        Matcher mather = pattern.matcher(email);

        if (mather.find()) {
            email_bool = true;
        } else {
            registro_email.setError("Email incorrecto, prueba con uno diferente", icono);
        }

        // Comprobar contraseña
        if (("").equals(password.trim())) {
            registro_password.setError("Campo obligatorio.", icono);
        }
        if (("").equals(confirma_password.trim())) {
            registro_confirma.setError("Campo obligatorio.", icono);
        }

        if (!("").equals(password.trim()) && !("").equals(confirma_password.trim())) {
            if (password.length() >= 6) {
                if (password.trim().equals(confirma_password.trim())) {
                    pass_bool = true;
                } else {
                    registro_password.setError("Las contraseñas no coinciden", icono);
                    registro_confirma.setError("Las contraseñas no coinciden", icono);
                }
            } else {
                registro_password.setError("La contraseña debe tener por lo menos 6 carácteres", icono);
            }
        }

        if (nickname_bool && email_bool && pass_bool) {
            new RequestAsync().execute("registra");
        }
    }
}
