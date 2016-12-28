//To use "global variable" on TabFragment3

package kaist.cs496_assignment_1;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Display;

public class MyApplication extends Application {
    public static Bitmap stored_bmp;
    public static int save_number=0;
    @Override
    public void onCreate(){
        super.onCreate();
    }
}
