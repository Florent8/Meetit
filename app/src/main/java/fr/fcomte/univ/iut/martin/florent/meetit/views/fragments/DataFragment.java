package fr.fcomte.univ.iut.martin.florent.meetit.views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.fcomte.univ.iut.martin.florent.meetit.manager.CharactersDatabaseHandler;
import fr.fcomte.univ.iut.martin.florent.meetit.views.recyclerview.MyCharacterRecyclerViewAdapter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.characters_recycler_view;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.text_characters_number;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.text_location_switch;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.text_search_delay;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.text_search_radius;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.fragment_data;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.location_enabled_default_value;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.location_enabled_key;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.search_delay_default_value;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.search_delay_key;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.search_radius_default_value;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.string.search_radius_key;
import static lombok.AccessLevel.PRIVATE;

/**
 * Fragment qui affiche la liste des personnages <br/>
 * Hérite de {@link Fragment}
 *
 * @see MyCharacterRecyclerViewAdapter
 */
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public final class DataFragment extends Fragment {

    final StringBuilder stringBuilder = new StringBuilder();
    CharactersDatabaseHandler handler;
    View                      root;

    /**
     * Initialisation du fragment
     *
     * @param savedInstanceState {@link Bundle}
     */
    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new CharactersDatabaseHandler(getContext());
    }

    /**
     * Initialisation de la vue
     *
     * @param inflater           {@link LayoutInflater}
     * @param container          {@link ViewGroup}
     * @param savedInstanceState {@link Bundle}
     * @return {@link View}
     * @see MyCharacterRecyclerViewAdapter
     */
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        root = inflater.inflate(fragment_data, container, false);

        // Set the adapter
        final RecyclerView recyclerView = root.findViewById(characters_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyCharacterRecyclerViewAdapter(handler.getAllCharacters()));

        return root;
    }

    /**
     * Affichage des préférences de l'utilisateur
     * et du nombre de personnages en base de données
     */
    private void updateUI() {
        final SharedPreferences preferences = getDefaultSharedPreferences(getContext());
        stringBuilder.setLength(0);
        ((TextView) root.findViewById(text_location_switch))
                .setText(stringBuilder.append(" ").append(Boolean.toString(
                        preferences.getBoolean(getResources().getString(location_enabled_key),
                                               Boolean.parseBoolean(getResources().getString(
                                                       (location_enabled_default_value)))
                        ))
                ).toString());
        stringBuilder.setLength(0);
        ((TextView) root.findViewById(text_search_delay)).setText(stringBuilder.append(" ").append(
                preferences.getString(getResources().getString(search_delay_key),
                                      getResources().getString(search_delay_default_value)
                )
        ).toString());
        stringBuilder.setLength(0);
        ((TextView) root.findViewById(text_search_radius)).setText(stringBuilder.append(" ").append(
                preferences.getString(getResources().getString(search_radius_key),
                                      getResources().getString(search_radius_default_value)
                )
        ).toString());
        stringBuilder.setLength(0);
        ((TextView) root.findViewById(text_characters_number))
                .setText(stringBuilder.append(" ").append(
                        Long.toString(handler.length())
                ).toString());
    }

    /**
     * @see Fragment#onResume()
     * @see DataFragment#updateUI()
     */
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
