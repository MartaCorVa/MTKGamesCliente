package cat.paucasesnoves.mtkgames;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Type;

import cat.paucasesnoves.mtkgames.entidades.Usuario;

public class MainLogged extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged);

        Toolbar toolbar = findViewById(R.id.toolbar_logged);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_logged);
        NavigationView navigationView = findViewById(R.id.nav_view_logged);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tienda_logged, R.id.nav_biblioteca_logged, R.id.nav_modificar_logged, R.id.nav_monedero_logged, R.id.nav_cerrar_logged)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_logged);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        obtenDatosLogin();
        View headView = navigationView.getHeaderView(0);
        TextView nickname = headView.findViewById(R.id.nav_nickname);
        nickname.setText(user.getNickName());
    }

    public void obtenDatosLogin() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("datos_login", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuario", null);
        Type type = new TypeToken<Usuario>() {}.getType();
        user = gson.fromJson(json, type);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_logged);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
