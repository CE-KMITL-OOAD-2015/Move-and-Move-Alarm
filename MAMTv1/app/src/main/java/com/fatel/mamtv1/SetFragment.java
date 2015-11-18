package com.fatel.mamtv1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends android.support.v4.app.Fragment  implements View.OnClickListener {

    private Switch mySwitch;
    private boolean checkswitch = false;
    public SetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        Button reset= (Button)view.findViewById(R.id.reset);
        mySwitch = (Switch) view.findViewById(R.id.mySwitch);

        //set the switch to ON
        if(UserManage.getInstance(getActivity()).getCurrentStateSw()==1){
            mySwitch.setChecked(true);
        }
        else{
            mySwitch.setChecked(false);
        }
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    //switchStatus.setText("Switch is currently ON");
                    checkswitch = true;
                    mySwitch.setChecked(true);
                    UserManage.getInstance(getActivity()).setStateSw(1,getActivity());
                }else{
                    //switchStatus.setText("Switch is currently OFF");
                    checkswitch =false;
                    mySwitch.setChecked(false);
                    UserManage.getInstance(getActivity()).setStateSw(0, getActivity());
                }
                Cache.getInstance().putData("switch",checkswitch);
            }
        });

        //check the current state before we display the screen
        if(mySwitch.isChecked()){
            //switchStatus.setText("Switch is currently ON");
        }
        else {
           // switchStatus.setText("Switch is currently OFF");
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

}
