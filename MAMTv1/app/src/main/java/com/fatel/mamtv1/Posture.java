package com.fatel.mamtv1;
import android.content.Context;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 23/10/2558.
 */
public class Posture {

    private int id;

    //public static final String DATABASE_NAME = "fatel_alarm.db";
    private PostureHelper helper;

    public static final String TABLE = "postureTB";
    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String IMAGE= "image";
        public static final String DESCRIPTION = "description";
    }

    public Posture(int id, int image, String description, Context context){
        this.id=id;
        helper=new PostureHelper(context);//Activity.context );
        if (!(helper.hasImage(id))) helper.saveImage(id, image, description);
    }

    public int getImage(){
        int img = helper.loadImage(this.id);
        return img;

    }

    public String getDescription(){

        String des = helper.loadDescription(this.id);
        return des;

    }




}
