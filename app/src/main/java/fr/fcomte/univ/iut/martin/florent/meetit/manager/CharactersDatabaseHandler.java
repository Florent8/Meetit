package fr.fcomte.univ.iut.martin.florent.meetit.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * Gestion des personnages en base de données
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
public final class CharactersDatabaseHandler extends SQLiteOpenHelper {

    // Database
    static String DATABASE_NAME        = "characters_db";
    static byte   DATABASE_VERSION     = 1;
    static String CHARACTERS_JSON_FILE = "characters.json";

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
     * @param context activité où est instanciée la classe
     */
    public CharactersDatabaseHandler(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Création et initialisation de la table {@value TABLE_CHARACTERS} en base de données
     *
     * @param db database
     * @see CharactersDatabaseHandler#insert(SQLiteDatabase, Character)
     * @see CharactersDatabaseHandler#updateImage(SQLiteDatabase, String, long)
     */
    @SneakyThrows(IOException.class)
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

        final Character[] characters = new Gson().fromJson(
                new InputStreamReader(context.getAssets().open(CHARACTERS_JSON_FILE)),
                Character[].class
        );

        for (final Character character : characters)
            updateImage(db, character.imageName(),
                        insert(db, character)
            );
    }

    /**
     * Insertion d'un personnage en base de données
     *
     * @param db        database
     * @param character personnage à insérer
     * @return id du personnage inséré
     */
    private long insert(final SQLiteDatabase db, final Character character) {
        values.clear();
        values.put(KEY_FIRSTNAME, character.firstname());
        values.put(KEY_LASTNAME, character.lastname());
        values.put(KEY_WEBURL, character.weburl());
        if (character.latitude() != 0)
            values.put(KEY_LATITUDE, character.latitude());
        if (character.longitude() != 0)
            values.put(KEY_LONGITUDE, character.longitude());
        return db.insert(TABLE_CHARACTERS, null, values);
    }

    /**
     * Insertion d'une image en base de données
     *
     * @param db database
     * @param s  nom de l'image
     * @param id id du personnage
     */
    @SneakyThrows(IOException.class)
    private void updateImage(final SQLiteDatabase db, final String s, final long id) {
        @Cleanup final InputStream inputStream = context.getAssets().open(s);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final byte[] bitmapdata = stream.toByteArray();
        values.clear();
        values.put(KEY_IMAGE, bitmapdata);
        db.update(TABLE_CHARACTERS, values, KEY_ID + " = ?",
                  new String[]{Long.toString(id)}
        );
    }

    /**
     * @param n nombre de personnages
     * @return liste des n premiers personnages en base de données
     */
    public List<Character> getCharacters(final long n) {
        final List<Character> characters = new ArrayList<>();
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db
                .query(TABLE_CHARACTERS,
                       new String[]{KEY_ID, KEY_FIRSTNAME, KEY_LASTNAME, KEY_WEBURL, KEY_LATITUDE,
                                    KEY_LONGITUDE, KEY_IMAGE},
                       null, null, null, null, null, Long.toString(n)
                );
        if (cursor.moveToFirst())
            do {
                final byte[] image = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
                characters.add(new Character(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                                             cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)),
                                             cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)),
                                             cursor.getString(cursor.getColumnIndex(KEY_WEBURL)),
                                             cursor.getFloat(cursor.getColumnIndex(KEY_LATITUDE)),
                                             cursor.getFloat(cursor.getColumnIndex(KEY_LONGITUDE)),
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
     * @return tous les personnages en base de données
     * @see CharactersDatabaseHandler#getCharacters(long)
     */
    public List<Character> getAllCharacters() {
        return getCharacters(length());
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {}
}
