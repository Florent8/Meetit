package fr.fcomte.univ.iut.martin.florent.meetit.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.fragment_data;

/**
 * A fragment representing a list of Items.
 */
public final class DataFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DataFragment() {
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(fragment_data, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new MyCharacterRecyclerViewAdapter(new ArrayList<>()));
        }
        return view;
    }
}
