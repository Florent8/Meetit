package fr.fcomte.univ.iut.martin.florent.meetit.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.fcomte.univ.iut.martin.florent.meetit.model.Character;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import static android.database.DatabaseUtils.queryNumEntries;
import static android.graphics.Bitmap.createScaledBitmap;
import static android.graphics.BitmapFactory.decodeByteArray;
import static lombok.AccessLevel.PRIVATE;

/**
 * Gestion des personnages en base de données <br/>
 * Hérite de {@link SQLiteOpenHelper}
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
public final class CharactersDatabaseHandler extends SQLiteOpenHelper {

    // Database
    static String DATABASE_NAME    = "characters_db";
    static byte   DATABASE_VERSION = 1;

    // Table Characters
    static String TABLE_CHARACTERS = "characters";

    // Table Characters content
    static String KEY_ID        = "id";
    static String KEY_FIRSTNAME = "firstname";
    static String KEY_LASTNAME  = "lastname";
    static String KEY_WEBURL    = "weburl";
    static String KEY_LATITUDE  = "latitude";
    static String KEY_LONGITUDE = "longitude";
    static String KEY_IMAGE     = "image";

    // Attributs
    ContentValues values = new ContentValues();
    Context context;

    /**
     * Constructeur
     *
     * @param context {@link Context}
     */
    public CharactersDatabaseHandler(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Création et initialisation de la table {@value TABLE_CHARACTERS} en base de données
     *
     * @param db {@link SQLiteDatabase}
     * @see CharactersDatabaseHandler#insert(SQLiteDatabase, String, String, String, float, float)
     * @see CharactersDatabaseHandler#updateImage(SQLiteDatabase, String, long)
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CHARACTERS + "("
                   + KEY_ID + " INTEGER PRIMARY KEY,"
                   + KEY_FIRSTNAME + " TEXT DEFAULT NULL,"
                   + KEY_LASTNAME + " TEXT DEFAULT NULL,"
                   + KEY_WEBURL + " TEXT DEFAULT NULL,"
                   + KEY_LATITUDE + " REAL DEFAULT NULL,"
                   + KEY_LONGITUDE + " REAL DEFAULT NULL,"
                   + KEY_IMAGE + " BLOB DEFAULT NULL)");
        updateImage(db, "couchot.png", insert(db, "Jean-François", "Couchot",
                                              "http://members.femto-st.fr/jf-couchot/fr",
                                              47.642900f,
                                              6.840027f
        ));
        updateImage(db, "couturier.png", insert(db, "Raphaël", "Couturier",
                                                "http://members.femto-st.fr/raphael-couturier/fr",
                                                47.659518f, 6.813337f
        ));
        updateImage(db, "domas.png", insert(db, "Stéphane", "Domas",
                                            "http://info.iut-bm.univ-fcomte.fr/staff/sdomas/",
                                            47.6387143f, 6.8370225f
        ));
        updateImage(db, "makhoul.png", insert(db, "Abdallah", "Makhoul",
                                              "http://members.femto-st.fr/abdallah-makhoul/fr",
                                              47.638114f, 6.862139f
        ));
        updateImage(db, "chocolat.png", insert(db, "Chocolat", null, null, 0, 0));
        updateImage(db, "mini_mew.png", insert(db, "Mini Mew", null, null, 0, 0));
        updateImage(db, "rin.png", insert(db, "Rin", "Tezuka", null, 0, 0));
        updateImage(db, "yami.png", insert(db, "Konjiki no Yami", null, null, 0, 0));
    }

    /**
     * Insertion d'un personnage en base de données
     *
     * @param db        {@link SQLiteDatabase}
     * @param firstname {@link String}
     * @param lastname  {@link String}
     * @param weburl    {@link String}
     * @param latitude  float
     * @param longitude float
     * @return id du personnage inséré
     */
    private long insert(final SQLiteDatabase db, final String firstname, final String lastname,
                        final String weburl, final float latitude, final float longitude
    ) {
        values.clear();
        values.put(KEY_FIRSTNAME, firstname);
        values.put(KEY_LASTNAME, lastname);
        values.put(KEY_WEBURL, weburl);
        if (latitude != 0)
            values.put(KEY_LATITUDE, latitude);
        if (longitude != 0)
            values.put(KEY_LONGITUDE, longitude);
        return db.insert(TABLE_CHARACTERS, null, values);
    }

    /**
     * Insertion d'une image en base de données
     *
     * @param db {@link SQLiteDatabase}
     * @param s  {@link String} nom de l'image dans le dossier « assets »
     * @param id long
     */
    @SneakyThrows(IOException.class)
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void updateImage(final SQLiteDatabase db, final String s, final long id) {
        @Cleanup final InputStream inputStream = context.getAssets().open(s);
        final byte[] bitmapdata = new byte[inputStream.available()];
        inputStream.read(bitmapdata);

        values.clear();
        values.put(KEY_IMAGE, bitmapdata);
        db.update(TABLE_CHARACTERS, values, KEY_ID + " = ?",
                  new String[]{Long.toString(id)}
        );
    }

    /**
     * Récupération de la liste des n premiers personnages en base de données
     *
     * @param n long
     * @return {@link List} de {@link Character}
     */
    public List<Character> getCharacters(final long n) {
        final List<Character> characters = new ArrayList<>();
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db
                .query(TABLE_CHARACTERS,
                       new String[]{KEY_ID, KEY_FIRSTNAME, KEY_LASTNAME, KEY_WEBURL, KEY_LATITUDE,
                                    KEY_LONGITUDE},
                       null, null, null, null, null, Long.toString(n)
                );
        if (cursor.moveToFirst())
            do {
                final byte[] image = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
                characters.add(new Character(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                                             cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)),
                                             cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)),
                                             cursor.getString(cursor.getColumnIndex(KEY_WEBURL)),
                                             cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                                             cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)),
                                             createScaledBitmap(
                                                     decodeByteArray(image, 0, image.length), 150,
                                                     200, false
                                             )
                ));
            } while (cursor.moveToNext());
        cursor.close();
        db.close();
        return characters;
    }

    /**
     * @return nombre de personnages en base de données
     */
    public long length() {
        return queryNumEntries(getReadableDatabase(), TABLE_CHARACTERS);
    }

    /**
     * Récupère tous les personnages en base de données
     *
     * @return {@link List} de {@link Character}
     * @see CharactersDatabaseHandler#getCharacters(long)
     */
    public List<Character> getAllCharacters() {
        return getCharacters(length());
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {}
}
