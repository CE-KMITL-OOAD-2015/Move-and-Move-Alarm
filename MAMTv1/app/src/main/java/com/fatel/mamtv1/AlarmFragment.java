package com.fatel.mamtv1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends android.support.v4.app.Fragment {


    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        createSpinner(12, R.id.start_hr,true,view);
        createSpinner(60, R.id.start_min,false,view);
        createSpinner(12, R.id.fin_hr,true,view);
        createSpinner(60, R.id.fin_min,false,view);
        createSpinnerAmPm(R.id.start_AP, view);
        createSpinnerAmPm(R.id.fin_AP,view);
        createSpinnerFrq(R.id.frq_min, view);

        Button bt = (Button) view.findViewById(R.id.buttonSet);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tx = getFragmentManager().beginTransaction();
                tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                tx.replace(R.id.container, new MainFragment());
                tx.addToBackStack(null);
                tx.commit();
            }
        });
        return view;
    }

    public void createSpinner(int num,int id,boolean isHr,View view)
    {
        Spinner spinner = (Spinner)view.findViewById(id);
        String[] numm = new String[num];
        ArrayAdapter<String> adapter;
        if(isHr) {
            for (int i = 1; i <= num; i++) {
                if(i<10)
                    numm[i - 1] = "0" + i;
                else
                    numm[i - 1] = "" + i;
            }
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(11);
        }
        else {
            for (int i = 0; i < num; i++) {
                if(i<10)
                    numm[i] = "0" + i;
                else
                    numm[i] = "" + i;
            }
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

    }

    public void createSpinnerAmPm(int id,View view)
    {
        Spinner spinner = (Spinner)view.findViewById(id);
        String[] numm = new String[]{"AM","PM"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void createSpinnerFrq(int id,View view)
    {
        Spinner spinner = (Spinner)view.findViewById(id);
        String[] numm = new String[]{"15","30","45","60","75","90"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }




}
