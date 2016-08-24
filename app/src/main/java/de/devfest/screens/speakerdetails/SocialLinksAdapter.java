package de.devfest.screens.speakerdetails;

import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.devfest.R;
import de.devfest.databinding.ItemSocialButtonBinding;
import de.devfest.model.SocialLink;

public class SocialLinksAdapter extends RecyclerView.Adapter<SocialLinksAdapter.SocialButtonViewHolder> {

    private final View.OnClickListener itemClickerListener;
    private final List<SocialLink> socialLinks;

    public SocialLinksAdapter(View.OnClickListener itemClickListener) {
        this.itemClickerListener = itemClickListener;
        socialLinks = new ArrayList<>();
    }

    @Override
    public SocialButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_social_button, parent, false);
        view.setOnClickListener(itemClickerListener);
        return new SocialButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SocialButtonViewHolder holder, int position) {
        holder.bind(socialLinks.get(position));
    }

    public void setSocialLinks(List<SocialLink> socialLinks) {
        this.socialLinks.clear();
        this.socialLinks.addAll(socialLinks);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return socialLinks.size();
    }

    public SocialLink getItem(int index) {
        return socialLinks.get(index);
    }

    static class SocialButtonViewHolder extends RecyclerView.ViewHolder {
        private final ItemSocialButtonBinding binding;

        public SocialButtonViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        void bind(SocialLink socialLink) {
            int size = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_large);
            binding.btnSocial.setText(socialLink.name);
            Drawable icon = ContextCompat.getDrawable(itemView.getContext(), socialLink.icon);
            icon.setBounds(0, 0, size, size);
            binding.btnSocial.setCompoundDrawables(icon, null, null, null);
            if (socialLink.color != 0) {
                int color = ContextCompat.getColor(itemView.getContext(), socialLink.color);
                binding.btnSocial.setTextColor(color);
                icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        }
    }

    static class SocialButtonData {
        final String name;
        final String link;
        final int icon;
        final int color;

        public SocialButtonData(String name, String link, @DrawableRes int icon, @ColorRes int color) {
            this.name = name;
            this.link = link;
            this.icon = icon;
            this.color = color;
        }
    }
}
