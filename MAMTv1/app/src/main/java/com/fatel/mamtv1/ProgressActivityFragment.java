package com.fatel.mamtv1;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.lylc.widget.circularprogressbar.CircularProgressBar;
import com.lylc.widget.circularprogressbar.CircularProgressBar.ProgressAnimationListener;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressActivityFragment extends Fragment {
    //add for progress bar
    //private static final int PROGRESS = 0x1;
    private ProgressBar timeProgress;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    TextView timeFrac,accept,cancel;
    //private int countAccept = 6;
    private int cancelPercent = 0;
    //private int countTotal = 10;
    private int cirProgressstatus = 0;
    private int timePerPic = 1;
    public static ProgressActivityFragment newInstance() {
        // Required empty public constructor
        ProgressActivityFragment fragment = new ProgressActivityFragment();
        return fragment;
    }
    public ProgressActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress_activity, container, false);
        //cal % of circular progress
        HistoryHelper mhistoryHelper = new HistoryHelper(getActivity());
        History history = mhistoryHelper.getHistoryUser(UserManage.getInstance(getActivity()).getCurrentIdUser());
        if(history.gettotal()==0){
            cirProgressstatus = 0;
            cancelPercent = 0;
            mProgressStatus = 0;
        }
        else{
            cirProgressstatus = (history.getNumberOfAccept()*100)/history.gettotal();
            cancelPercent = 100-cirProgressstatus;
            mProgressStatus = ((history.getNumberOfAccept() * timePerPic) * 100) / (history.gettotal() * timePerPic);
        }
        accept = (TextView) view.findViewById(R.id.acceptPercent);
        cancel = (TextView) view.findViewById(R.id.cancelPercent);
        accept.setText(cirProgressstatus + "%");
        cancel.setText(cancelPercent+"%");

        final CircularProgressBar c2 = (CircularProgressBar) view.findViewById(R.id.circularprogressbar2);
        c2.animateProgressTo(0, cirProgressstatus, new ProgressAnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationProgress(int progress) {
                c2.setTitle(progress + "%");
            }

            @Override
            public void onAnimationFinish() {
                c2.setSubTitle("accept of activity");
            }
        });


        //add for progressbar
        timeProgress = (ProgressBar) view.findViewById(R.id.barTime);
        timeFrac = (TextView) view.findViewById(R.id.timeFraction);


        //set progress bar in each days
        timeProgress.setProgress(mProgressStatus);
        // Show the progress on TextView
        timeFrac.setText((history.getNumberOfAccept()*timePerPic)+"/"+(history.gettotal()*timePerPic)+"min");
    return view;
    }


}
