package cat.paucasesnoves.mtkgames.ui.iniciar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import cat.paucasesnoves.mtkgames.MainActivity;
import cat.paucasesnoves.mtkgames.R;
import cat.paucasesnoves.mtkgames.entidades.Usuario;
import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;

import static android.content.Context.MODE_PRIVATE;

public class CerrarSesionFragment extends Fragment {

    private GestorBBDD gestor = new GestorBBDD();
    private Usuario user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_iniciar, container, false);

        obtenDatosLogin();
        new RequestAsync().execute();

        getActivity().getSharedPreferences("datos_login", 0).edit().clear().apply();
        startActivity(new Intent(getContext(), MainActivity.class));

        return rootView;
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
                String parametros = "{\"IdUsuario\": \"" + user.getId() + "\"}";
                return gestor.enviarPost("http://192.168.0.167:45455/api/usuario/cerrarsesion", parametros);
            }
            catch(Exception e){
                return "Excepción: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            // Obtenemos respuesta de la api
            if(s!=null){

            }
        }
    }

    public void obtenDatosLogin() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("datos_login", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuario", null);
        Type type = new TypeToken<Usuario>() {}.getType();
        user = gson.fromJson(json, type);
    }
}
