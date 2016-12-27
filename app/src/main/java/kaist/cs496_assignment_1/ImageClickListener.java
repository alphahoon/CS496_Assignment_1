package kaist.cs496_assignment_1;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import kaist.cs496_assignment_1.R;

/**
 * Created by q on 2016-12-27.
 */

public class ImageClickListener implements View.OnClickListener {
    Context context;

    int imageID;

    public ImageClickListener(Context context, int imageID) {
        this.context = context;
        this.imageID = imageID;
    }

    public void onClick(View v) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("image ID", imageID);
        context.startActivity(intent);
    }
}