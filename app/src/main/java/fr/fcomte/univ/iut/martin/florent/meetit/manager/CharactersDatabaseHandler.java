package fr.fcomte.univ.iut.martin.florent.meetit.manager;

import android.annotation.SuppressLint;
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
import fr.fcomte.univ.iut.martin.florent.meetit.string.MyStringBuilder;

import static android.database.DatabaseUtils.queryNumEntries;
import static android.graphics.BitmapFactory.decodeByteArray;

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
    private final ContentValues values = new ContentValues();
    private final Context context;

    public CharactersDatabaseHandler(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(stringBuilder.append("CREATE TABLE ").append(TABLE_CHARACTERS).append("(")
                .append(KEY_ID).append(" INTEGER PRIMARY KEY,")
                .append(KEY_FIRSTNAME).append(" TEXT DEFAULT NULL,")
                .append(KEY_LASTNAME).append(" TEXT DEFAULT NULL,")
                .append(KEY_WEBURL).append(" TEXT DEFAULT NULL,")
                .append(KEY_LATITUDE).append(" REAL DEFAULT NULL,")
                .append(KEY_LONGITUDE).append(" REAL DEFAULT NULL,")
                .append(KEY_IMAGE).append(" BLOB DEFAULT NULL)")
                .toString());
        updateImage(db, "couchot.png", insert(db, "Jean-François", "Couchot", "http://members.femto-st.fr/jf-couchot/fr", 47.642900, 6.840027));
        updateImage(db, "couturier.png", insert(db, "Raphaël", "Couturier", "http://members.femto-st.fr/raphael-couturier/fr", 47.659518, 6.813337));
        updateImage(db, "domas.png", insert(db, "Stéphane", "Domas", "http://info.iut-bm.univ-fcomte.fr/staff/sdomas/", 47.6387143, 6.8370225));
        updateImage(db, "makhoul.png", insert(db, "Abdallah", "Makhoul", "http://members.femto-st.fr/abdallah-makhoul/fr", 47.638114, 6.862139));
        updateImage(db, "chocolat.png", insert(db, "Chocolat", null, null, 0, 0));
        updateImage(db, "mini_mew.png", insert(db, "Mini Mew", null, null, 0, 0));
        updateImage(db, "rin.png", insert(db, "Rin", "Tezuka", null, 0, 0));
        updateImage(db, "yami.png", insert(db, "Konjiki no Yami", null, null, 0, 0));
    }

    private long insert(final SQLiteDatabase db, final String firstname, final String lastname, final String weburl, final double latitude, final double longitude) {
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void updateImage(final SQLiteDatabase db, final String s, final long id) {
        try {
            final InputStream inputStream = context.getAssets().open(s);
            final byte[] bitmapdata = new byte[inputStream.available()];
            inputStream.read(bitmapdata);
            inputStream.close();

            values.clear();
            values.put(KEY_IMAGE, bitmapdata);
            db.update(TABLE_CHARACTERS, values, stringBuilder.append(KEY_ID).append(" = ?").toString(), new String[]{Long.toString(id)});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Character> getCharacters(final long n) {
        final List<Character> characters = new ArrayList<>();
        final SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") final Cursor cursor = db.rawQuery(stringBuilder.append("SELECT * FROM ").append(TABLE_CHARACTERS)
                .append(" LIMIT ").append(Long.toString(n)).toString(), null);
        if (cursor.moveToFirst())
            do {
                final byte[] image = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
                characters.add(new Character(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_WEBURL)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)),
                        decodeByteArray(image, 0, image.length)
                ));
            } while (cursor.moveToNext());
        cursor.close();
        db.close();
        return characters;
    }

    public long length() {
        return queryNumEntries(getReadableDatabase(), TABLE_CHARACTERS);
    }

    public List<Character> getAllCharacters() {
        return getCharacters(length());
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }
}
