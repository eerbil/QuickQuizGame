package com.example.eliferbil.quickquiz.memogame;

import android.content.Context;
import android.support.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.List;

import static com.example.eliferbil.quickquiz.memogame.Flag.OpenState.CLOSED;
import static com.example.eliferbil.quickquiz.memogame.Flag.OpenState.ELIMINATED;
import static com.example.eliferbil.quickquiz.memogame.Flag.OpenState.OPEN;


public class FlagAdapter extends TaggedImageAdapter<FlagAdapter.IndexedFlagTag> {
    private final int cardResId;
    private List<Flag> flags;

    public FlagAdapter(Context c, @DrawableRes int cardResId, List<Flag> flags) {
        super(c, getMipmapIds(flags, cardResId), createTagList(flags));
        this.cardResId = cardResId;
        this.flags = flags;
    }

    public void openFlag(int index) {
        flagAction(index, OPEN);
    }

    public void closeFlag(int index) {
        flagAction(index, CLOSED);
    }

    public void eliminateFlag(int index) {
        flagAction(index, ELIMINATED);
    }

    private void flagAction(int index, Flag.OpenState nextState) {
        Flag current = flags.get(index);
        current.setState(nextState);
        mipmapIds[index] = getFlagMipmapId(current, cardResId);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position) {
        Flag flag = flags.get(position);
        return flag.getState() != ELIMINATED && super.isEnabled(position);
    }

    @DrawableRes
    private static int getFlagMipmapId(Flag current, @DrawableRes int cardResId) {
        Flag.OpenState state = current.getState();
        @DrawableRes int mipmapId = 0;
        switch (state) {
            case OPEN:
                mipmapId = current.getCountry().getMipmapId();
                break;
            case CLOSED:
                mipmapId = cardResId;
                break;
            case ELIMINATED:
                mipmapId = 0;
                break;
        }
        return mipmapId;
    }

    @DrawableRes
    private static int[] getMipmapIds(List<Flag> tags, @DrawableRes int cardResId) {
        int[] mipmapIds = new int[tags.size()];
        for (int i = 0; i < mipmapIds.length; i++) {
            mipmapIds[i] = getFlagMipmapId(tags.get(i), cardResId);
        }
        return mipmapIds;
    }

    private static List<IndexedFlagTag> createTagList(List<Flag> flags) {
        int size = flags.size();
        List<IndexedFlagTag> tags = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tags.add(new IndexedFlagTag(i, flags.get(i)));
        }
        return tags;
    }

    protected static class IndexedFlagTag {
        public final int index;
        public final Flag flag;

        public IndexedFlagTag(int index, Flag flag) {
            this.index = index;
            this.flag = flag;
        }
    }
}
