package com.fatel.mamtv1;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Administrator on 24/10/2558.
 */
public class PostureCollection {

    private static ArrayList<Posture> imageCollection=new ArrayList<>();
    private static int count=1;
    //private PostureHelper helper = new PostureHelper();

    public static void addImage(int image,String description,Context context){
        Posture img = new Posture(count,image,description,context);
        imageCollection.add(img);
        count++;

    }

    public static void initial (Context context){
        addImage(R.drawable.pos1, "ยืดกล้ามเนื้อแขน",context);
        addImage(R.drawable.pos2,"ยืดกล้ามเนื้อขา",context);
        addImage(R.drawable.pos3,"ยืดต้นขา ยืดเข่า",context);
        addImage(R.drawable.pos4,"บริหารข้อเท้า",context);
        addImage(R.drawable.pos5,"ยืดกล้ามเนื้อคอ",context);
        addImage(R.drawable.pos6,"ยืดกล้ามเนื้อคอ",context);
        addImage(R.drawable.pos7,"ยืดกล้ามเนื้อคอ",context);
        addImage(R.drawable.pos8, "บริหารกล้ามเนื้อคอ",context);
        addImage(R.drawable.pos9,"ยืดกล้ามเนื้อขา",context);
        addImage(R.drawable.pos10,"ยืดกล้ามเนื้อขา",context);
        addImage(R.drawable.pos11,"ยืดกล้ามเนื้อส่วนแขน",context);
        addImage(R.drawable.pos12,"ยืดกล้ามเนื้อส่วนแขน",context);
        addImage(R.drawable.pos13,"ยืดกล้ามเนื้อส่วนแขน",context);

    }
    public static int size(){
        return imageCollection.size();
    }
    public static Posture getImageById(int id){
        return imageCollection.get(id);
    }
    public static ArrayList<Posture> getImageById(int[] id){
        ArrayList<Posture> imgcoll = new ArrayList<>();
        for (int i=0;i<id.length;i++){
            imgcoll.add(imageCollection.get(id[i]));
        }
        return imgcoll;
    }




}
