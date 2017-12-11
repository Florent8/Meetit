package fr.fcomte.univ.iut.martin.florent.meetit.model;

import android.graphics.Bitmap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

/**
 * Représentation d'un personnage
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Accessors(fluent = true)
public final class Character {

    StringBuilder stringBuilder = new StringBuilder();
    @Getter int    id;
    @Getter String firstname;
    @Getter String lastname;
    @Getter String weburl;
    @Getter float  latitude;
    @Getter float  longitude;
    @Getter String imageName;
    @Getter Bitmap image;

    public Character(final int id, final String firstname, final String lastname,
                     final String weburl, final float latitude, final float longitude,
                     final Bitmap image
    ) {
        this(id, firstname, lastname, weburl, latitude, longitude, null, image);
    }

    /**
     * @return {@link Character#firstname} et {@link Character#lastname} sur deux lignes
     */
    @Override
    public String toString() {
        stringBuilder.setLength(0);
        stringBuilder.append(firstname);
        if (lastname != null)
            stringBuilder.append("\n").append(lastname);
        return stringBuilder.toString();
    }

    /**
     * @return {@link Character#firstname} et {@link Character#lastname} sur une ligne
     */
    public String toStringInline() {
        stringBuilder.setLength(0);
        stringBuilder.append(firstname);
        if (lastname != null)
            stringBuilder.append(" ").append(lastname);
        return stringBuilder.toString();
    }
}
