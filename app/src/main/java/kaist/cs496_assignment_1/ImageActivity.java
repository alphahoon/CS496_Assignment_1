package kaist.cs496_assignment_1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.content.Intent;
/**
 * Created by q on 2016-12-27.
 */

public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_image);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        setImage(imageView);
    }

    private void setImage(ImageView imageView){
        Intent receivedIntent = getIntent();

        int imageID = (Integer)receivedIntent.getExtras().get("image ID");
        imageView.setImageResource(imageID);
    }
}
