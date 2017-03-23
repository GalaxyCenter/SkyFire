package apollo.tianya.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import apollo.tianya.AppContext;
import apollo.tianya.bean.Thread;

/**
 * Created by Texel on 2016/10/26.
 */

public class PublishTaskServiceUtils {

    public static void pubThread(Context context, Thread post) {
        Intent intent = new Intent(PublishTaskService.ACTION_PUB_POST);
        Bundle bundle = new Bundle();

        bundle.putSerializable(PublishTaskService.BUNDLE_PUB_POST, post);
        intent.putExtras(bundle);
        intent.setPackage(AppContext.getInstance().getPackageName());
        context.startService(intent);
    }
}