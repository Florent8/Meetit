package fr.fcomte.univ.iut.martin.florent.meetit.model;

import android.graphics.Bitmap;

import fr.fcomte.univ.iut.martin.florent.meetit.string.MyStringBuilder;

public final class Character {
    private final MyStringBuilder stringBuilder = new MyStringBuilder();
    private final int id;
    private final String firstname;
    private final String lastname;
    private final String weburl;
    private final double latitude;
    private final double longitude;
    private final Bitmap image;

    public Character(final int id, final String firstname, final String lastname, final String weburl, final double latitude, final double longitude, final Bitmap image) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.weburl = weburl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getWeburl() {
        return weburl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public String toString() {
        return stringBuilder.append(firstname).append(" ").append(lastname).toString();
    }
}
