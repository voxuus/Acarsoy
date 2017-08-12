package ua.in.zeusapps.acarsoy.common;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class GenericAdapter<TItem, THolder extends GenericHolder>
        extends RecyclerView.Adapter<THolder> {

    private final List<TItem> _items;

    protected GenericAdapter(List<TItem> items) {
        _items = items;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(THolder holder, int position) {
        holder.update(_items.get(position));
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }
}
