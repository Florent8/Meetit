package fr.fcomte.univ.iut.martin.florent.meetit.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.NoArgsConstructor;

import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.fragment_home;

/**
 * A simple {@link Fragment} subclass.
 */
@NoArgsConstructor
public final class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(fragment_home, container, false);
    }
}
