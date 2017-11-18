package fr.fcomte.univ.iut.martin.florent.meetit.views.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.fcomte.univ.iut.martin.florent.meetit.R;
import fr.fcomte.univ.iut.martin.florent.meetit.model.Character;

import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.fragment_character;

/**
 * {@link RecyclerView.Adapter} that can display a {@link fr.fcomte.univ.iut.martin.florent.meetit.model.Character}
 */
public final class MyCharacterRecyclerViewAdapter extends RecyclerView.Adapter<MyCharacterRecyclerViewAdapter.CharacterViewHolder> {

    private final List<Character> characters;

    public MyCharacterRecyclerViewAdapter(List<Character> items) {
        characters = items;
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(fragment_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CharacterViewHolder holder, int position) {
        holder.mItem = characters.get(position);
        //holder.mIdView.setText(characters.get(position).id);
        //holder.mContentView.setText(characters.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public class CharacterViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Character mItem;

        public CharacterViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
