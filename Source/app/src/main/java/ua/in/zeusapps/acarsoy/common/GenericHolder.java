package ua.in.zeusapps.acarsoy.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public abstract class GenericHolder<TItem> extends RecyclerView.ViewHolder {
    public GenericHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    protected Context getContext(){
        return itemView.getContext();
    }

    public abstract void update(TItem item);
}
