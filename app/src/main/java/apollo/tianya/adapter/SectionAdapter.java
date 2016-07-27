package apollo.tianya.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import apollo.tianya.bean.Section;
import butterknife.ButterKnife;

/**
 * Created by kuibo on 2016/7/27.
 */
public class SectionAdapter extends RecyclerBaseAdapter<Section, SectionAdapter.ViewHolder> {

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public ViewHolder getFootViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }
}
