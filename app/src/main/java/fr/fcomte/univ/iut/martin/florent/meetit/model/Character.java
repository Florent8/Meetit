package fr.fcomte.univ.iut.martin.florent.meetit.model;

import android.graphics.Bitmap;

import fr.fcomte.univ.iut.martin.florent.meetit.string.MyStringBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static android.graphics.Bitmap.createScaledBitmap;

@RequiredArgsConstructor
public final class Character {

    private final MyStringBuilder stringBuilder = new MyStringBuilder();
    @Getter private final int    id;
    private final         String firstname;
    private final         String lastname;
    @Getter private final String weburl;
    @Getter private final double latitude;
    @Getter private final double longitude;
    @Getter private final Bitmap image;

    @Override
    public String toString() {
        stringBuilder.append(firstname);
        if (lastname != null)
            stringBuilder.append("\n").append(lastname);
        return stringBuilder.toString();
    }

    public String toStringInline() {
        stringBuilder.append(firstname);
        if (lastname != null)
            stringBuilder.append(" ").append(lastname);
        return stringBuilder.toString();
    }
}
