package com.fatel.mamtv1;
import android.content.Context;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Administrator on 23/10/2558.
 */
public class Image {

    private int id;

    //public static final String DATABASE_NAME = "fatel_alarm.db";
    private ImageHelper helper;

    public static final String TABLE = "imageTB";
    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String IMAGE= "image";
        public static final String DESCRIPTION = "description";
    }

    public Image (int id,int image,String description,Context context){
        this.id=id;
        helper=new ImageHelper(context);//Activity.context );
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
