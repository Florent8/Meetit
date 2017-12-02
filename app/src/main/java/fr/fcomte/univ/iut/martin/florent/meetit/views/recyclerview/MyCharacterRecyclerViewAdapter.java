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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import static android.view.animation.AnimationUtils.loadAnimation;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.anim.slide_in_right;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.image_view_character;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.id.text_view_character;
import static fr.fcomte.univ.iut.martin.florent.meetit.R.layout.fragment_character;
import static lombok.AccessLevel.PRIVATE;

/**
 * {@link RecyclerView.Adapter} pour l'affichage de {@link fr.fcomte.univ.iut.martin.florent.meetit.model.Character}
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class MyCharacterRecyclerViewAdapter
        extends RecyclerView.Adapter<MyCharacterRecyclerViewAdapter.CharacterViewHolder> {

    List<Character> characters;
    @NonFinal int firstAnimations = 5;

    /**
     * @param parent   {@link ViewGroup}
     * @param viewType int
     * @return {@link CharacterViewHolder}
     * @see RecyclerView.Adapter#createViewHolder(ViewGroup, int)
     */
    @Override
    public CharacterViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new CharacterViewHolder(LayoutInflater.from(parent.getContext())
                                                     .inflate(fragment_character, parent, false));
    }

    /**
     * Affichage du personnage et de sa photo
     *
     * @param holder   {@link CharacterViewHolder}
     * @param position int
     */
    @Override
    public void onBindViewHolder(final CharacterViewHolder holder, int position) {
        final Character character = characters.get(position);
        holder.setText(character.toString());
        holder.setImageBitmap(character.image());
        int startOffset = 0;
        if (firstAnimations > 0) {
            firstAnimations--;
            startOffset = (position % 5) * 200;
        }
        holder.anim(startOffset);
    }

    /**
     * @return nombre de personnages
     */
    @Override
    public int getItemCount() {
        return characters.size();
    }

    /**
     * Vue d'un personnage <br/>
     * Hérite de {@link RecyclerView.ViewHolder}
     *
     * @see Character
     */
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    final class CharacterViewHolder extends RecyclerView.ViewHolder {

        View      view;
        ImageView imageView;
        TextView  textView;

        /**
         * Constructeur
         *
         * @param view {@link View}
         */
        CharacterViewHolder(final View view) {
            super(view);
            this.view = view;
            imageView = view.findViewById(image_view_character);
            textView = view.findViewById(text_view_character);
        }

        /**
         * Affichage du nom et prénom du personnage
         *
         * @param text {@link String}
         */
        void setText(final String text) {
            textView.setText(text);
        }

        /**
         * Affichage de la photo du personnage
         *
         * @param imageBitmap {@link Bitmap}
         */
        void setImageBitmap(final Bitmap imageBitmap) {
            imageView.setImageBitmap(imageBitmap);
        }

        /**
         * Animation à l'apparition de la vue
         *
         * @param startOffset long
         */
        void anim(final long startOffset) {
            final Animation animation = loadAnimation(view.getContext(), slide_in_right);
            animation.setStartOffset(startOffset);
            view.startAnimation(animation);
        }
    }
}
