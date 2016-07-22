package com.ambient.testmenu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MapView mapView;
    private MapboxMap map;
    private SlidingUpPanelLayout mLayout;
    LocationServices locationServices;
    FloatingActionButton floatingActionButton;
    TextView textNameSlide;
    ListView lv;

    private static final int PERMISSIONS_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        inicializar(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //Metodo para controlar los eventos del menu lateral....
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.reportes_usuario) {
            // Handle the camera action
            Toast.makeText(context, "Presiono Boton Cosa 1...", duration).show();
            startActivity(new Intent(MainActivity.this, ActivityPnlLateral1.class));
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(context, "Presiono Boton Cosa 2...", duration).show();
            startActivity(new Intent(MainActivity.this, ActivityPnlLateral2.class));
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(context, "Presiono Boton Cosa 3...", duration).show();
            startActivity(new Intent(MainActivity.this, ActivityPnlLateral3.class));
        } else if (id == R.id.nav_manage) {
            Toast.makeText(context, "Presiono Boton Cosa 4...", duration).show();
            startActivity(new Intent(MainActivity.this, ActivityPnlLateral4.class));
        } else if (id == R.id.configuracion) {
            Toast.makeText(context, "Presiono Boton Cosa 1 Configuracion...", duration).show();
            startActivity(new Intent(MainActivity.this, ActivityPnlLateral5.class));
        } else if (id == R.id.ayuda) {
            Toast.makeText(context, "Presiono Boton Cosa 2 Configuracion...", duration).show();
            startActivity(new Intent(MainActivity.this, ActivityPnlLateral6.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Metodos para el ciclo de vida del activity..
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    ////

    //Metodo para conseguir la ubicacion del usuario...
    @UiThread
    public void toggleGps(boolean enableGps) {
        if (enableGps) {
            // Check if user has granted location permission
            if (!locationServices.areLocationPermissionsGranted()) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
            } else {
                enableLocation(true);
            }
        } else {
            enableLocation(false);
        }
    }

    //Metodo encargado de setear la ubicacion del usuario...
    private void enableLocation(boolean enabled) {
        if (enabled) {
            locationServices.addLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        // Move the map camera to where the user location is
                        map.setCameraPosition(new CameraPosition.Builder()
                                .target(new LatLng(location))
                                .zoom(16)
                                .build());
                    }
                }
            });
            //Setear iconos dependiendo del clic en el boton de ubicacion...
            floatingActionButton.setImageResource(R.drawable.ic_localizacion_usuario_desac_svg);
        } else {
            floatingActionButton.setImageResource(R.drawable.ic_localizacion_usuario_svg);
        }
        // Enable or disable the location layer on the map
        map.setMyLocationEnabled(enabled);
    }

    //Metodo encargado de revisar el permiso de ubicacion del dispositivo...
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_LOCATION: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation(true);
                }
            }
        }
    }

    public void inicializar(Bundle savedInstanceState){
        inicializarComponentesGraficos();
        inicializarMapa(savedInstanceState);
        inicializarBarraInferior(savedInstanceState);
        inicializarBotonUbicacionUsuario();
    }

    public void inicializarComponentesGraficos(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void inicializarMapa(Bundle savedInstanceState){
        //Visualizacion del mapa....
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create a mapView
        mapView = (MapView) findViewById(R.id.mapa);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // Customize map with markers, polylines, etc.
                map = mapboxMap;
            }
        });
    }

    public void inicializarLista(int itemId){
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, ActivityForm.class));
                //Toast.makeText(MainActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> listaOpciones = new ArrayList<>();

        switch (itemId) {
            case R.id.recent_item:
                listaOpciones = Arrays.asList("Opcion 1", "Opcion 2", "Opcion 3");
                break;
            case R.id.favorite_item:
                listaOpciones = Arrays.asList("Opcion 4", "Opcion 5", "Opcion 6");
                break;
            case R.id.location_item:
                listaOpciones = Arrays.asList("Opcion 7", "Opcion 8", "Opcion 9", "Opcion 10");
                break;
        }
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listaOpciones);

        lv.setAdapter(arrayAdapter);
    }

    public void inicializarBarraInferior(Bundle savedInstanceState){

        textNameSlide = (TextView) findViewById(R.id.name);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        //Barra inferior si se quiere poner con bottom!! Falta mirar como se hace :/ ....
        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.menu_bottom, new OnMenuTabSelectedListener() {
            public void onMenuItemSelected(int itemId) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                switch (itemId) {
                    case R.id.recent_item:
                        //Snackbar.make(coordinatorLayout, "Recent Item Selected", Snackbar.LENGTH_LONG).show();
                        inicializarLista(R.id.recent_item);
                        textNameSlide.setText("Items Recientes");
                        if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.HIDDEN)) {
                            mLayout.setAnchorPoint(0.5f);
                            //Toast.makeText(context, "State:..." + mLayout.getPanelState().name(), duration).show();
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        }
                        break;
                    case R.id.favorite_item:
                        //Snackbar.make(coordinatorLayout, "Favorite Item Selected", Snackbar.LENGTH_LONG).show();
                        inicializarLista(R.id.favorite_item);
                        textNameSlide.setText("Items Favoritos");
                        if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.HIDDEN)) {
                            mLayout.setAnchorPoint(0.5f);
                            //Toast.makeText(context, "State:..." + mLayout.getPanelState().name(), duration).show();
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        }
                        break;
                    case R.id.location_item:
                        //Snackbar.make(coordinatorLayout, "Location Item Selected", Snackbar.LENGTH_LONG).show();
                        inicializarLista(R.id.location_item);
                        textNameSlide.setText("Items Localizacion");
                        if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.HIDDEN)) {
                            mLayout.setAnchorPoint(0.5f);
                            //Toast.makeText(context, "State:..." + mLayout.getPanelState().name(), duration).show();
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        }
                        break;
                }
            }
        });
    }

    public void inicializarBotonUbicacionUsuario(){
        //Ubicacion del usuario por medio del boton flotante...
        locationServices = LocationServices.getLocationServices(MainActivity.this);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    toggleGps(!map.isMyLocationEnabled());
                }
            }
        });
    }
}