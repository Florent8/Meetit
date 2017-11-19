package fr.fcomte.univ.iut.martin.florent.meetit.views.recyclerview;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.fcomte.univ.iut.martin.florent.meetit.model.Character;

import static android.view.animation.AnimationUtils.loadAnimation;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.anim.slide_in_right;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.image_view_character;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.text_view_character;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.fragment_character;

/**
 * {@link RecyclerView.Adapter} that can display a {@link fr.fcomte.univ.iut.martin.florent.meetit.model.Character}
 */
public final class MyCharacterRecyclerViewAdapter extends RecyclerView.Adapter<MyCharacterRecyclerViewAdapter.CharacterViewHolder> {

    private final List<Character> characters;
    private int firstAnimations = 5;

    public MyCharacterRecyclerViewAdapter(final List<Character> characters) {
        this.characters = characters;
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new CharacterViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(fragment_character, parent, false));
    }

    @Override
    public void onBindViewHolder(final CharacterViewHolder holder, int position) {
        final Character character = characters.get(position);
        holder.setText(character.toString());
        holder.setImageBitmap(character.getImage());
        int startOffset = 0;
        if (firstAnimations > 0) {
            firstAnimations--;
            startOffset = (position % 5) * 200;
        }
        holder.anim(startOffset);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    final class CharacterViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final ImageView imageView;
        private final TextView textView;

        CharacterViewHolder(final View view) {
            super(view);
            this.view = view;
            imageView = view.findViewById(image_view_character);
            textView = view.findViewById(text_view_character);
        }

        void setText(final String text) {
            textView.setText(text);
        }

        void setImageBitmap(final Bitmap imageBitmap) {
            imageView.setImageBitmap(imageBitmap);
        }

        void anim(final long startOffset) {
            final Animation animation = loadAnimation(view.getContext(), slide_in_right);
            animation.setStartOffset(startOffset);
            view.startAnimation(animation);
        }
    }
}
