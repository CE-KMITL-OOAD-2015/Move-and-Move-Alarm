package com.fatel.mamtv1;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Administrator on 31/10/2558.
 */
public class ActivityHandle {

    int[] imageId = new int[] {-1,-1,-1,-1};

    public ActivityHandle(){

        random();

    }
    public int[] getImageId(){
        return imageId;

    }
    public int getImageIdByIndex(int index){
        return imageId[index];

    }

    public void random(){
        for(int i=0;i<4;i++){
            boolean same=true;
            int x=0;
            while(same){
                same=false;
                x=(int)(Math.random() * 13);
                for(int j=0;j<i;j++) {
                    if (x == imageId[j]){
                        same=true;
                        break;
                    }
                }
            }
            imageId[i]=x;
        }

    }
    public ArrayList<Posture> getRandomPosture(Context context){
        PostureCollection postureCollection= PostureCollection.getInstance(context);
        ArrayList<Posture> randomPosture = postureCollection.getPosture(getImageId());
        return  randomPosture;
    }

}
