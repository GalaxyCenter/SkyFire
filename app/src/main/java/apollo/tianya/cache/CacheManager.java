package apollo.tianya.cache;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import apollo.tianya.util.CompatibleUtil;

/**
 * Created by Texel on 2016/7/15.
 */
public class CacheManager {

    // wifi缓存时间为5分钟
    private static long wifi_cache_time = 5 * 60 * 1000;
    // 其他网络环境为1小时
    private static long other_cache_time = 60 * 60 * 1000;

    /**
     * 判断缓存是否存在
     * @param context
     * @param cachefile
     * @return
     */
    public static boolean isExistDataCache(Context context, String cachefile) {
        if (context == null)
            return false;
        boolean exist = false;
        File data = context.getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 判断缓存是否已经失效
     * @param context
     * @param cachefile
     * @return
     */
    public static boolean isCacheDataFailure(Context context, String cachefile) {
        File data = context.getFileStreamPath(cachefile);
        if (!data.exists()) {
            return false;
        }

        long existTime = System.currentTimeMillis() - data.lastModified();
        boolean failure = false;
        if (CompatibleUtil.getNetworkType() == CompatibleUtil.NETTYPE_WIFI) {
            failure = existTime > wifi_cache_time ? true : false;
        } else {
            failure = existTime > other_cache_time ? true : false;
        }
        return failure;
    }

    /**
     * 从磁盘中读取缓存对象
     * @param context
     * @param cachefile
     * @return
     */
    public static Serializable readObject(Context context, String cachefile) {
        if (!isExistDataCache(context, cachefile))
            return null;

        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = context.openFileInput(cachefile);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = context.getFileStreamPath(cachefile);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 保存对象到磁盘中
     * @param context
     * @param seri
     * @param cachefile
     * @return
     */
    public static boolean saveObject(Context context, Serializable seri, String cachefile) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = context.openFileOutput(cachefile, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(seri);
            oos.flush();

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try { oos.close(); } catch(Exception e){}
            try { fos.close(); } catch(Exception e){}
        }
    }
}
