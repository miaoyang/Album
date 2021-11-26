package com.ym.album.action;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

/**
 * Activity相关意图
 */
public interface ActivityAction {
    Context getContext();

    default Activity getActivity(){
        Context context = getContext();
        do {
            if (context instanceof Activity){
                return (Activity) context;
            }else if (context instanceof ContextWrapper){
                return (Activity) ((ContextWrapper) context).getBaseContext();
            }else {
                return null;
            }
        }while (context !=null);
    }

    default void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(getContext(), clazz));
    }

    default void startActivity(Intent intent){
        if (!(getContext() instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        getContext().startActivity(intent);
    }

}
