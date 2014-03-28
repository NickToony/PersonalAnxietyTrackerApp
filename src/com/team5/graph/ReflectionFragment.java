package com.team5.graph;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team5.pat.R;

import java.util.List;

/**
 * Popup window allowing user to reflect upon a record
 * @author David
 *
 */
public class ReflectionFragment extends Fragment implements OnClickListener {

    private View v;
    private ImageButton leftArrow;
    private ImageButton rightArrow;
    private int ser;
    private int anx;
    private String thought;
    private EditText serious;
    private EditText anxiety;
    private TextView reflection;
    private Button cancel;
    private int currentLoadedIndex;
    private List<Point> points;

    public void setSer(float ser) {
        this.ser = (int) ser;
    }

    public void setAnx(float anx) {
        this.anx = (int) anx;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }


    // public ReflectionFragment(float ser, float anx, String thought) {
    // this.ser = (int) ser;
    // this.anx = (int) anx;
    // this.thought = thought;
    // }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.reflection_fragment, container, false);
        serious = (EditText) v.findViewById(R.id.seriousnessEditText);
        anxiety = (EditText) v.findViewById(R.id.anxietyEditText);
        reflection = (TextView) v.findViewById(R.id.reflectionText);

        cancel = (Button) v.findViewById(R.id.ref_fragment_cancel_button);
        cancel.setOnClickListener(this);
        /*
        * set the onclick listener to view data from left to right
        * include the date of the point on the fragment
        * */


        //End of left click button implementation
        reflection.setText(thought);
        serious.setText("" + ser);
        anxiety.setText("" + anx);

        return v;

    }


    @Override
    public void onClick(View v) {
        getFragmentManager().beginTransaction().remove(this).commit();

    }
}
