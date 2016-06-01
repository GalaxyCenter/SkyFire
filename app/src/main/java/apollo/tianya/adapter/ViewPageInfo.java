package apollo.tianya.adapter;

import android.os.Bundle;

/**
 * Created by Texel on 2016/6/1.
 */
public class ViewPageInfo {

    public final String tag;
    public final Class<?> refer;
    public final Bundle args;
    public final String title;

    public ViewPageInfo(String _title, String _tag, Class<?> _refer, Bundle _args) {
        title = _title;
        tag = _tag;
        refer = _refer;
        args = _args;
    }

}
