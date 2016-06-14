package apollo.tianya.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by kuibo on 2016/6/14.
 */
public class PhotoFragment extends Fragment {

    private String mUrl;

    public PhotoFragment(String url) {
        mUrl = url;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ImageView image = new ImageView(getActivity());
        //image.setImageResource(imageResourceId);

        LinearLayout layout = new LinearLayout(getActivity());
        //layout.setLayoutParams(new ViewGroup.LayoutParams());

        layout.setGravity(Gravity.CENTER);
        layout.addView(image);
        return layout;
    }

}
