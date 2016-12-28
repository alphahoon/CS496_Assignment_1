//To use "global variable" on TabFragment3

package kaist.cs496_assignment_1;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by q on 2016-12-28.
 */

public class MyApplication extends Application {

    public static Bitmap stored_bmp;
    public static int save_number=0;
    @Override
    public void onCreate(){
        super.onCreate();
    }
}
