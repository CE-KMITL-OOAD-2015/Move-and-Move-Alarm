package com.fatel.mamtv1;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lylc.widget.circularprogressbar.CircularProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressEventFragment extends Fragment {
    //add for progress bar
    // private static final int PROGRESS = 0x1;
    private ProgressBar timeProgressE;
    private int mProgressStatusE = 0;
    private Handler mHandler = new Handler();

    TextView timeFracE,acceptE,cancelE;
    private int countAcceptE = 0;
    private int cancelPercentE = 0;
    private int countTotalE = 0;
    private int cirProgressstatusE = 0;
    private int timePerPicE = 1;
    public static ProgressEventFragment newInstance() {
        // Required empty public constructor
        ProgressEventFragment fragment = new  ProgressEventFragment();
        return fragment;
    }
    public ProgressEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress_event, container, false);

        //cal % of circular progress
        HistorygroupHelper mhistorygroupHelper = new HistorygroupHelper(getActivity());
        Historygroup historygroup = mhistorygroupHelper.getHistoryGroup(UserManage.getInstance(getActivity()).getCurrentIdGroup());
        if(historygroup==null){
            cirProgressstatusE = 0;
            cancelPercentE = 0;
            mProgressStatusE = 0;
        }
        else if(historygroup.gettotal()==0){
            cirProgressstatusE = 0;
            cancelPercentE = 0;
            mProgressStatusE = 0;
        }
        else{
            cirProgressstatusE = (historygroup.getNumberOfAccept()*100)/historygroup.gettotal();
            cancelPercentE = 100-cirProgressstatusE;
            mProgressStatusE = ((countAcceptE * timePerPicE) * 100) / (countTotalE * timePerPicE);
        }
        acceptE = (TextView) view.findViewById(R.id.acceptPercent);
        cancelE = (TextView) view.findViewById(R.id.cancelPercent);
        acceptE.setText(cirProgressstatusE + "%");
        cancelE.setText(cancelPercentE + "%");

        final CircularProgressBar cE = (CircularProgressBar) view.findViewById(R.id.circularprogressbarE);
        cE.animateProgressTo(0, cirProgressstatusE, new CircularProgressBar.ProgressAnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationProgress(int progress) {cE.setTitle(progress + "%");
            }

            @Override
            public void onAnimationFinish() {
                cE.setSubTitle("accept of event");
            }
        });


        //add for progressbar
        timeProgressE = (ProgressBar) view.findViewById(R.id.barTimeE);
        timeFracE = (TextView) view.findViewById(R.id.timeFractionE);

            //set progress bar in each days
            timeProgressE.setProgress(mProgressStatusE);
            // Show the progress on TextView
            timeFracE.setText((countAcceptE*timePerPicE)+"/"+(countTotalE*timePerPicE)+"min");


        return view;
    }


}
