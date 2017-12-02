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
    @Getter int id;
    String firstname;
    String lastname;
    @Getter String weburl;
    @Getter double latitude;
    @Getter double longitude;
    @Getter Bitmap image;

    /**
     * Affiche le nom et le prénom sur deux lignes
     *
     * @return {@link String}
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
     * Affiche le nom et le prénom sur une seule ligne
     *
     * @return {@link String}
     */
    public String toStringInline() {
        stringBuilder.setLength(0);
        stringBuilder.append(firstname);
        if (lastname != null)
            stringBuilder.append(" ").append(lastname);
        return stringBuilder.toString();
    }
}
