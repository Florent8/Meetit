package fr.fcomte.univ.iut.martin.florent.meetit.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.fragment_camera;

/**
 * A simple {@link Fragment} subclass.
 */
public final class CameraFragment extends Fragment {

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(fragment_camera, container, false);
    }
}
