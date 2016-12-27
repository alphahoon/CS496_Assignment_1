package kaist.cs496_assignment_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.net.URL;

public class TabFragment3 extends Fragment {

    public EditText imgURL;
    public Button imgBtn;
    public ImageView imgView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
        imgURL = (EditText) view.findViewById(R.id.imgURL);
        imgBtn = (Button) view.findViewById(R.id.getImageBtn);
        imgView = (ImageView) view.findViewById(R.id.imgView);
        imgBtn.setOnClickListener(imageBtnOnClicked);

        return view;
    }

    View.OnClickListener imageBtnOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = imgURL.getText().toString();
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://".concat(url);
            GetImage getImage = new GetImage(url);
            getImage.execute();
        }
    };

    public Bitmap getBitmapFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Invalid image URL");
            return null;
        }
    }

    private class GetImage extends AsyncTask<Void, Void, Void> {
        String url;
        Bitmap bmp;

        GetImage(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            bmp = getBitmapFromUrl(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (bmp != null) {
                imgView.setImageBitmap(bmp);
            } else {
                System.out.println("Null image");
            }
        }
    }
}

