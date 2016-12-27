package kaist.cs496_assignment_1;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import kaist.cs496_assignment_1.ImageClickListener;
import kaist.cs496_assignment_1.R;

public class ImageAdapter extends BaseAdapter {
    private Context context;

    int[] picture = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j};
    int[] imageIDs = null;

    @Override
    public int getCount() {
        return (null!= imageIDs) ? imageIDs.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return(null!= imageIDs) ? picture[position%imageIDs.length] : 0;
    }

    public ImageAdapter(Context c, int[] imageIDs)
    {
        context=c;
        this.imageIDs=imageIDs;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if (convertView != null) {
            imageView = (ImageView) convertView;
        }
        else {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setImageResource(picture[position % imageIDs.length]);
            //OnClickListener
            ImageClickListener imageViewClickListener = new ImageClickListener(context, imageIDs[position]);
            imageView.setOnClickListener(imageViewClickListener);
        }
        return imageView;
    }
}
