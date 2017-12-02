package fr.fcomte.univ.iut.martin.florent.meetit.views.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.fcomte.univ.iut.martin.florent.meetit.model.Character;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.api_key;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.key_neighbor_intent;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.key_neighbor_path;
import static java.util.Collections.sort;
import static lombok.AccessLevel.PRIVATE;

/**
 * Tâche asynchrone qui va calculer la distance entre la position de l'utilisateur et celle des
 * personnages à proximité <br/>
 * Hérite de {@link NeighborAsyncTask} <br/>
 * Implémente {@link AsyncTask}
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class NeighborAsyncTask extends AsyncTask<Map<Location, Character>, Void, Void> {

    StringBuilder stringBuilder = new StringBuilder();
    @SuppressLint("StaticFieldLeak")
    Context context;
    Location location;

    /**
     * Récupère et compare les distances, puis les affiches du personnages
     * le plus proche au plus lointain
     *
     * @param maps tableau de {@link Map} de {@link Location} et {@link Character}
     * @return {@link Void}
     */
    @Override
    protected Void doInBackground(final Map<Location, Character>[] maps) {
        final Map<Character, Integer> mapDistance = new HashMap<>();
        final List<Integer> distances = new ArrayList<>();
        for (final Map<Location, Character> map : maps)
            for (final Map.Entry<Location, Character> entry : map.entrySet()) {
                final int distance = getDistance(getPathLength(location, entry.getKey()));
                distances.add(distance);
                mapDistance.put(entry.getValue(), distance);
            }

        sort(distances);

        stringBuilder.setLength(0);
        stringBuilder.append("Personnes de la plus proche à la plus lointaine :");
        for (final int distance : distances)
            for (final Map.Entry<Character, Integer> entry : mapDistance.entrySet())
                if (distance == entry.getValue())
                    stringBuilder.append("\n").append(entry.getKey().toStringInline());

        context.sendBroadcast(new Intent(context.getResources().getString(key_neighbor_intent))
                                      .putExtra(context.getResources().getString(key_neighbor_path),
                                                stringBuilder.toString()
                                      ));
        return null;
    }

    /**
     * Retourne la réponse en JSON de l'URL de la position demandée
     *
     * @param lo {@link Location}
     * @param ld {@link Location}
     * @return {@link String}
     */
    @SneakyThrows(IOException.class)
    private String getPathLength(final Location lo, final Location ld) {
        stringBuilder.append("https://maps.googleapis.com/maps/api/directions/json?")
                     .append("&mode=walking")
                     .append("&origin=").append(Double.toString(lo.getLatitude())).append(",")
                     .append(Double.toString(lo.getLongitude()))
                     .append("&destination=").append(Double.toString(ld.getLatitude()))
                     .append(",").append(Double.toString(ld.getLongitude()))
                     .append("&key=").append(context.getResources().getString(api_key));
        final URLConnection conn = new URL(stringBuilder.toString()).openConnection();
        conn.setDoOutput(true);
        new OutputStreamWriter(conn.getOutputStream()).flush();
        return convertStreamToString(conn.getInputStream());
    }

    /**
     * @param in {@link InputStream}
     * @return {@link String}
     */
    @SneakyThrows(IOException.class)
    private String convertStreamToString(final InputStream in) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; )
            out.write(buffer, 0, count);
        return new String(out.toByteArray(), "UTF-8");
    }

    /**
     * Récupère la distance dans un tableau JSON
     *
     * @param jsontext {@link String}
     * @return int
     */
    @SneakyThrows(JSONException.class)
    private int getDistance(final String jsontext) {
        return new JSONObject(jsontext)
                .getJSONArray("routes")
                .getJSONObject(0)
                .getJSONArray("legs")
                .getJSONObject(0)
                .getJSONObject("distance")
                .getInt("value");
    }
}
