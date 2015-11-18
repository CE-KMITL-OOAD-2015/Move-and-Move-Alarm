package com.fatel.mamtv1;

import android.content.Context;
import android.media.Image;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 24/10/2558.
 */
public class PostureCollection {

    private static PostureCollection instance = null;
    private ArrayList<Posture> collection = new ArrayList<>();
    private static int count=0;
    private PostureCollection(){

    }
    public static PostureCollection getInstance(Context context){
        if (instance == null) {
            instance = new PostureCollection();
            instance.initial(context);
        }
        return instance;
    }

    public void addImage(int image,String description,Context context){
        Posture posture = new Posture(count,image,description);
        posture.save(context);
        collection.add(posture);
        count++;
    }

    public void initial (Context context){
        Log.i("Posture", "getPostureCount :" + Posture.getPostureCount(context));
        if(Posture.getPostureCount(context)<13) {
            addImage(R.drawable.pos1, "ยืดกล้ามเนื้อแขน", context);
            addImage(R.drawable.pos2, "ยืดกล้ามเนื้อขา", context);
            addImage(R.drawable.pos3, "ยืดต้นขา ยืดเข่า", context);
            addImage(R.drawable.pos4, "บริหารข้อเท้า", context);
            addImage(R.drawable.pos5, "ยืดกล้ามเนื้อคอ", context);
            addImage(R.drawable.pos6, "ยืดกล้ามเนื้อคอ", context);
            addImage(R.drawable.pos7, "ยืดกล้ามเนื้อคอ", context);
            addImage(R.drawable.pos8, "บริหารกล้ามเนื้อคอ", context);
            addImage(R.drawable.pos9, "ยืดกล้ามเนื้อขา", context);
            addImage(R.drawable.pos10, "ยืดกล้ามเนื้อขา", context);
            addImage(R.drawable.pos11, "ยืดกล้ามเนื้อส่วนแขน", context);
            addImage(R.drawable.pos12, "ยืดกล้ามเนื้อส่วนแขน", context);
            addImage(R.drawable.pos13, "ยืดกล้ามเนื้อส่วนแขน", context);
        }
        else{
            for(int i=0;i<Posture.getPostureCount(context);i++){
                Posture posture=Posture.find(i,context);
                collection.add(posture);
            }

        }

    }
    public int size(){
        return collection.size();
    }
    public Posture getPosture(int id){
        return collection.get(id);
    }
    public ArrayList<Posture> getPosture(int[] id){
        ArrayList<Posture> imgcoll = new ArrayList<>();
        for (int i=0;i<id.length;i++){
            imgcoll.add(collection.get(id[i]));
        }
        return imgcoll;
    }




}
