package fr.fcomte.univ.iut.martin.florent.meetit.views.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import fr.fcomte.univ.iut.martin.florent.meetit.string.MyStringBuilder;

import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.api_key;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.key_neighbor_intent;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.key_neighbor_path;

public final class NeighborAsyncTask extends AsyncTask<Location, Void, Void> {

    public static final String LOCATION_LENGTH = "location_length";
    private final MyStringBuilder stringBuilder = new MyStringBuilder();
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final Location location;

    public NeighborAsyncTask(final Context context, final Location location) {
        super();
        this.context = context;
        this.location = location;
    }

    @Override
    protected Void doInBackground(final Location[] locations) {
        final Intent intent = new Intent(context.getResources().getString(key_neighbor_intent));
        intent.putExtra(LOCATION_LENGTH, locations.length);
        for (int i = 0; i < locations.length; i++)
            intent.putExtra(context.getResources().getString(key_neighbor_path) + i, getPathLength(location, locations[i]));
        context.sendBroadcast(intent);
        return null;
    }

    private String getPathLength(final Location lo, final Location ld) {
        try {
            stringBuilder.append("https://maps.googleapis.com/maps/api/directions/json?")
                    .append("&mode=walking")
                    .append("&origin=").append(Double.toString(lo.getLatitude())).append(",").append(Double.toString(lo.getLongitude()))
                    .append("&destination=").append(Double.toString(ld.getLatitude())).append(",").append(Double.toString(ld.getLongitude()))
                    .append("&key=").append(context.getResources().getString(api_key));
            final URLConnection conn = new URL(stringBuilder.toString()).openConnection();
            conn.setDoOutput(true);
            new OutputStreamWriter(conn.getOutputStream()).flush();
            return convertStreamToString(conn.getInputStream());
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertStreamToString(final InputStream in) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; )
                out.write(buffer, 0, count);
            return new String(out.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
