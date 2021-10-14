package notyet.newword.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStatus {
    public static final int TYPE_MOBILE = 1;
    public static final int TYPE_WIFI = 2;
    public static final int TYPE_NOT_CONNECTED = 3;

    public static int getConnectivityStatus(Context context){
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo info =manager.getActiveNetworkInfo();
        if(info !=null){
            int type=info.getType();
            switch (type){
                case ConnectivityManager.TYPE_MOBILE:
                    return TYPE_MOBILE;
                case ConnectivityManager.TYPE_WIFI:
                    return TYPE_WIFI;
            }
        }
        return TYPE_NOT_CONNECTED;
    }
}
