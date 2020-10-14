package cat.paucasesnoves.mtkgames.ui.iniciar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import cat.paucasesnoves.mtkgames.R;

import cat.paucasesnoves.mtkgames.utilidades.GestorBBDD;

public class IniciarFragment extends Fragment {

    private GestorBBDD gestor;
    private CardView boton_login;
    public static EditText edit_nickname;
    public static EditText edit_password;
    private String nickname;
    private String password;
    private Drawable icono;

    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_iniciar, container, false);

        gestor = new GestorBBDD();
        boton_login = rootView.findViewById(R.id.boton_login);
        edit_nickname = rootView.findViewById(R.id.login_nickname);
        edit_password = rootView.findViewById(R.id.login_password);
        icono = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error, null);
        icono.setBounds(0, 0, icono.getIntrinsicWidth(), icono.getIntrinsicHeight());

        boton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = edit_nickname.getText().toString();
                password = edit_password.getText().toString();
                if (compruebaDatos(nickname, password)) {
                    Intent intent = new Intent(getContext(), LoginAutomatico.class);
                    Bundle b = new Bundle();
                    b.putString("nickname", nickname);
                    b.putString("password", password);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    /**
     * Comprueba los campos del login
     * @param nickEdit
     * @param passEdit
     * @return boolean true si no hay campos vacios
     */
    public boolean compruebaDatos(String nickEdit, String passEdit) {
        if (("").equals(nickEdit.trim()) || ("").equals(passEdit.trim())) {
            if (("").equals(nickEdit.trim())) {
                IniciarFragment.edit_nickname.setError("Campo obligatorio.", icono);
            }
            if (("").equals(passEdit.trim())) {
                IniciarFragment.edit_password.setError("Campo obligatorio.", icono);
            }
            return false;
        }
        return true;
    }
}
