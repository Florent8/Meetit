package fr.fcomte.univ.iut.martin.florent.meetit.views.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.fcomte.univ.iut.martin.florent.meetit.manager.CharactersDatabaseHandler;
import fr.fcomte.univ.iut.martin.florent.meetit.model.Character;
import fr.fcomte.univ.iut.martin.florent.meetit.views.asynctask.NeighborAsyncTask;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Intent.ACTION_VIEW;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.map;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.fragment_map;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.key_neighbor_intent;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.key_neighbor_path;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.location_enabled_default_value;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.location_enabled_key;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.search_delay_default_value;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.search_delay_key;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.search_radius_default_value;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.search_radius_key;
import static lombok.AccessLevel.PRIVATE;

/**
 * Fragment d'affichage de la carte <br/>
 * Hérite de {@link Fragment} <br/>
 * Implémente {@link OnMapReadyCallback}, {@link GoogleApiClient.ConnectionCallbacks} et {@link GoogleMap.OnMarkerClickListener}
 */
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public final class MapFragment extends Fragment implements OnMapReadyCallback,
                                                           GoogleApiClient.ConnectionCallbacks,
                                                           GoogleMap.OnMarkerClickListener {

    static final int           REQUEST_LOCATION = 1;
    final        StringBuilder stringBuilder    = new StringBuilder();
    Context                   context;
    CharactersDatabaseHandler handler;
    GoogleMap                 googleMap;
    GoogleApiClient           googleApiClient;
    LocationCallback          locationCallback;
    LocationRequest           locationRequest;
    boolean                   requestionLocationUpdates;
    SharedPreferences         preferences;
    BroadcastReceiver         neighborBR;

    /**
     * Initialisation du fragment
     *
     * @param savedInstanceState {@link Bundle}
     */
    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        handler = new CharactersDatabaseHandler(context);
        preferences = getDefaultSharedPreferences(context);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        neighborBR = new BroadcastReceiver() {

            @Override
            public void onReceive(final Context context, final Intent intent) {
                Toast.makeText(context,
                               intent.getStringExtra(getResources().getString(key_neighbor_path)),
                               Toast.LENGTH_LONG
                ).show();
            }
        };
    }

    /**
     * Création de la vue
     *
     * @param inflater           {@link LayoutInflater}
     * @param container          {@link ViewGroup}
     * @param savedInstanceState {@link Bundle}
     * @return {@link View}
     */
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(fragment_map, container, false);
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(map)).getMapAsync(this);
        return view;
    }

    /**
     * Récupération de la carte
     *
     * @param googleMap {@link GoogleMap}
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);
    }

    /**
     * Mise à jour de l'affichage des personnages sur la carte
     *
     * @param location {@link Location}
     */
    private void updateUI(final Location location) {
        googleMap.clear();

        stringBuilder.setLength(0);
        if (location != null)
            stringBuilder.append(Double.toString(location.getLatitude())).append(" ")
                         .append(Double.toString(location.getLongitude()));

        final List<Character> characters = handler.getCharacters(4);
        final LatLngBounds.Builder builder = LatLngBounds.builder();
        final Map<Location, Character>[] charactersLocations = new Map[characters.size()];

        for (Character character : characters) {
            final LatLng latLng = new LatLng(character.latitude(), character.longitude());
            final MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                                                   .title(character.toString())
                                                                   .alpha(0.5f);
            if (location != null) {
                final Location characterLocation = new Location("");
                characterLocation.setLatitude(character.latitude());
                characterLocation.setLongitude(character.longitude());
                if (location.distanceTo(characterLocation) <= Float.parseFloat(
                        preferences.getString(getResources().getString(search_radius_key),
                                              getResources().getString(search_radius_default_value)
                        ))) {
                    markerOptions.alpha(1f);
                    stringBuilder.append("\n").append(character.toStringInline());
                }
                Map<Location, Character> map = new HashMap<>();
                map.put(characterLocation, character);
                charactersLocations[characters.indexOf(character)] = map;
            }
            googleMap.addMarker(markerOptions).setTag(character.weburl());
            builder.include(latLng);
        }

        if (location != null) {
            Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_LONG).show();
            new NeighborAsyncTask(context.getApplicationContext(), location)
                    .execute(charactersLocations);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 40));
    }

    /**
     * @see Fragment#onStart()
     * @see GoogleApiClient#connect()
     */
    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    /**
     * @see Fragment#onStop()
     * @see GoogleApiClient#disconnect()
     */
    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    /**
     * Affichage des personnages de la carte à la connexion
     *
     * @param bundle {@link Bundle}
     */
    @Override
    public void onConnected(@Nullable final Bundle bundle) {
        startOrStopLocationUpdates();
        updateUI(null);
    }

    /**
     * Récupération de la permission d'accéder à la géolocalisation
     */
    private void requestLocationPermission() {
        final Activity activity = getActivity();
        assert activity != null;
        ActivityCompat.requestPermissions(
                activity,
                new String[]{ACCESS_FINE_LOCATION},
                REQUEST_LOCATION
        );
    }

    /**
     * Paramètrage de la localisation <br/>
     * Demande de récupération de la localisation selon une durée stockée dans les préférences
     * et choisie par l'utilisateur
     */
    private void setLocationParmeters() {
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(final LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateUI(locationResult.getLastLocation());
            }
        };

        requestionLocationUpdates = preferences
                .getBoolean(getResources().getString(location_enabled_key),
                            Boolean.parseBoolean(
                                    getResources().getString(location_enabled_default_value))
                );

        if (requestionLocationUpdates) {
            locationRequest = new LocationRequest();
            locationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
            final int interval = Integer
                    .parseInt(preferences.getString(getResources().getString(search_delay_key),
                                                    getResources()
                                                            .getString(search_delay_default_value)
                    ));
            locationRequest.setInterval(1000 * interval);
            locationRequest.setFastestInterval(1000 * interval / 2);
        }
    }

    /**
     * Début des requêtes de localisation
     */
    private void startLocationUpdates() {
        if (checkSelfPermission(context, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED)
            requestLocationPermission();
        else
            getFusedLocationProviderClient(context)
                    .requestLocationUpdates(locationRequest, locationCallback,
                                            googleApiClient.getLooper()
                    );
    }

    /**
     * Fin des requêtes de localisation
     */
    private void stopLocationUpdates() {
        getFusedLocationProviderClient(context).removeLocationUpdates(locationCallback);
    }

    /**
     * @see MapFragment#startLocationUpdates()
     * @see MapFragment#stopLocationUpdates()
     */
    private void startOrStopLocationUpdates() {
        if (requestionLocationUpdates)
            startLocationUpdates();
        else
            stopLocationUpdates();
    }

    /**
     * @see Fragment#onResume()
     * @see MapFragment#setLocationParmeters()
     * @see MapFragment#startOrStopLocationUpdates()
     * @see Context#registerReceiver(BroadcastReceiver, IntentFilter)
     */
    @Override
    public void onResume() {
        super.onResume();
        setLocationParmeters();
        if (googleApiClient.isConnected())
            startOrStopLocationUpdates();
        context.registerReceiver(neighborBR,
                                 new IntentFilter(getResources().getString(key_neighbor_intent))
        );
    }

    /**
     * @see Fragment#onPause()
     * @see MapFragment#stopLocationUpdates()
     * @see Context#unregisterReceiver(BroadcastReceiver)
     */
    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
        context.unregisterReceiver(neighborBR);
    }

    @Override
    public void onConnectionSuspended(final int i) {
    }

    /**
     * Affichage du site du personnage selon le {@link Marker} cliqué
     *
     * @param marker {@link Marker}
     * @return boolean
     * @see GoogleMap.OnMarkerClickListener#onMarkerClick(Marker)
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        startActivity(new Intent(ACTION_VIEW, Uri.parse((String) marker.getTag())));
        return false;
    }
}
