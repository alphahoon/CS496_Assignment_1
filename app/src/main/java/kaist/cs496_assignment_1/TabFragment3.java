package kaist.cs496_assignment_1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import static android.R.attr.bitmap;
import static kaist.cs496_assignment_1.MyApplication.save_number;
import static kaist.cs496_assignment_1.MyApplication.stored_bmp;

public class TabFragment3 extends Fragment {

    public EditText imgURL;
    public Button imgBtn;
    public ImageView imgView;
    public Button button01;
    public Button button02;
    public Button button03;
    public Button button04;
    public Button button05;
    //02 03 04 05;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
        imgURL = (EditText) view.findViewById(R.id.imgURL);
        imgBtn = (Button) view.findViewById(R.id.getImageBtn);
        imgView = (ImageView) view.findViewById(R.id.imgView);
        button01 = (Button) view.findViewById(R.id.Btn01);
        button02 = (Button) view.findViewById(R.id.Btn02);
        button03 = (Button) view.findViewById(R.id.Btn03);
        button05 = (Button) view.findViewById(R.id.Btn05);
        imgBtn.setOnClickListener(imageBtnOnClicked);
        button01.setOnClickListener(button01OnClicked);
        button02.setOnClickListener(button02OnClicked);
        //button03.setOnClickListener(button03OnClicked);
        button05.setOnClickListener(button05OnClicked);

        return view;
    }

    View.OnClickListener imageBtnOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = imgURL.getText().toString();
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "https://technofriends.files.wordpress.com/2008/07/google-test-framework.png";
                //url = "http://".concat(url);    //Doesn't work on https
            GetImage getImage = new GetImage(url);
            getImage.execute();
        }
    };
    View.OnClickListener button01OnClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Bitmap bmp;
            if(stored_bmp==null) {
                MainActivity activity = (MainActivity) getActivity();
                Toast.makeText(activity,"You should get the image first", Toast.LENGTH_SHORT).show();
            }
            else
            {
                bmp=doGreyscale(stored_bmp);
                imgView.setImageBitmap(bmp);
            }
        }
    };
    View.OnClickListener button02OnClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Bitmap bmp;
            if(stored_bmp==null) {
                MainActivity activity = (MainActivity) getActivity();
                Toast.makeText(activity,"You should get the image first", Toast.LENGTH_SHORT).show();
            }
            else
            {
                bmp=doInvert(stored_bmp);
                imgView.setImageBitmap(bmp);
            }
        }
    };
    /*
    View.OnClickListener button03OnClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Bitmap bmp;
            float degree;
            if(stored_bmp==null) {
                MainActivity activity = (MainActivity) getActivity();
                Toast.makeText(activity,"You should get the image first", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Input : degree 'Message'
                bmp=rotate(stored_bmp,degree);
                imgView.setImageBitmap(bmp);
            }
        }
    };
    */
    View.OnClickListener button05OnClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Bitmap bmp;
            if(stored_bmp==null) {
                MainActivity activity = (MainActivity) getActivity();
                Toast.makeText(activity,"You should get the image first", Toast.LENGTH_SHORT).show();
            }
            else
            {
                save(stored_bmp);
            }
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
    /*
    private class GetProcessedImage extends GetImage{
        public GetProcessedImage(String URL){
            super(URL);
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            bmp = getBitmapFromUrl(url);
            bmp = doGreyscale(bmp);
            return null;
        }
    }
    */
    public static Bitmap doGreyscale(Bitmap src){
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = src.getWidth();
        int height = src.getHeight();

        // scan through every single pixel
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        // return final image
        return bmOut;
    }
    public static Bitmap doInvert(Bitmap src) {
        // create new bitmap with the same settings as source bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        // return final bitmap
        return bmOut;
    }
    public static Bitmap rotate(Bitmap src, float degree) {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);

        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }
    public void save(Bitmap src){
        MainActivity activity = (MainActivity) getActivity();
        try {
            //File file = new File(getContext().getFilesDir(), )
            FileOutputStream fos;
            fos = getActivity().openFileOutput("save" + "" + save_number + "" + ".png", Context.MODE_PRIVATE);

            src.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show();
        }catch(Exception e) { Toast.makeText(activity, "file error", Toast.LENGTH_SHORT).show();}
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
            stored_bmp = bmp;
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

