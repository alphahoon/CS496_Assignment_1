package kaist.cs496_assignment_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class TabFragment2 extends Fragment {
    private int[] imageIDs = new int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);

        GridView grid = (GridView)view.findViewById(R.id.grid);
        MainActivity activity = (MainActivity) getActivity();
        ImageAdapter Adapter = new ImageAdapter(activity,imageIDs);
        grid.setAdapter(Adapter);

        return view;
    }
}
