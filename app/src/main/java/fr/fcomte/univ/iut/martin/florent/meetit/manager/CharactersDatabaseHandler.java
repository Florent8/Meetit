package fr.fcomte.univ.iut.martin.florent.meetit.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.fcomte.univ.iut.martin.florent.meetit.string.MyStringBuilder;

public final class CharactersDatabaseHandler extends SQLiteOpenHelper {

    // Database
    private static final String DATABASE_NAME = "characters_db";
    private static final int DATABASE_VERSION = 1;

    // Table Characters
    private static final String TABLE_CHARACTERS = "characters";

    // Table Characters content
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_WEBURL = "weburl";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_IMAGE = "image";

    // Attributs
    private final MyStringBuilder stringBuilder = new MyStringBuilder();

    public CharactersDatabaseHandler(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(stringBuilder.append("CREATE TABLE ").append(TABLE_CHARACTERS).append("(")
                .append(KEY_ID).append(" INTEGER PRIMARY KEY,")
                .append(KEY_FIRSTNAME).append(" TEXT,")
                .append(KEY_LASTNAME).append(" TEXT,")
                .append(KEY_WEBURL).append(" TEXT,")
                .append(KEY_LATITUDE).append(" REAL,")
                .append(KEY_LONGITUDE).append(" REAL,")
                .append(KEY_IMAGE).append("") //TODO Blob
                .toString());
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }
}
