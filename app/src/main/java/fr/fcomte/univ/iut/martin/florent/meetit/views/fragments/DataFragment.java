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
import fr.fcomte.univ.iut.martin.florent.meetit.string.MyStringBuilder;
import fr.fcomte.univ.iut.martin.florent.meetit.views.recyclerview.MyCharacterRecyclerViewAdapter;

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

/**
 * A fragment representing a list of Items.
 */
public final class DataFragment extends Fragment {

    private final MyStringBuilder stringBuilder = new MyStringBuilder();
    private CharactersDatabaseHandler handler;
    private View root;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DataFragment() {
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new CharactersDatabaseHandler(getContext());
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        root = inflater.inflate(fragment_data, container, false);

        // Set the adapter
        final RecyclerView recyclerView = root.findViewById(characters_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyCharacterRecyclerViewAdapter(handler.getAllCharacters()));

        return root;
    }

    private void updateUI() {
        final SharedPreferences preferences = getDefaultSharedPreferences(getContext());
        ((TextView) root.findViewById(text_location_switch)).setText(stringBuilder.append(" ").append(Boolean.toString(
                preferences.getBoolean(getResources().getString(location_enabled_key),
                        Boolean.parseBoolean(getResources().getString((location_enabled_default_value)))))
        ).toString());
        ((TextView) root.findViewById(text_search_delay)).setText(stringBuilder.append(" ").append(
                preferences.getString(getResources().getString(search_delay_key),
                        getResources().getString(search_delay_default_value))
        ).toString());
        ((TextView) root.findViewById(text_search_radius)).setText(stringBuilder.append(" ").append(
                preferences.getString(getResources().getString(search_radius_key),
                        getResources().getString(search_radius_default_value))
        ).toString());
        ((TextView) root.findViewById(text_characters_number)).setText(stringBuilder.append(" ").append(
                Long.toString(handler.length())
        ).toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
