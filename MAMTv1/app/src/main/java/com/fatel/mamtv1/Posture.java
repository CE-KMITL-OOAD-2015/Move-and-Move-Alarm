package com.fatel.mamtv1;
import android.content.Context;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 23/10/2558.
 */
public class Posture {

    private int id;
    private int idPosture;
    private int image;
    private String description;

    private PostureHelper helper;

    public static final String TABLE = "postureTB";
    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String IDPOSTURE = "idPosture";
        public static final String IMAGE= "image";
        public static final String DESCRIPTION = "description";
    }

    public Posture(int id,int idPosture, int image, String description){
        this.id=id;
        this.idPosture = idPosture;
        this.image=image;
        this.description=description;
    }
    public Posture(int idPosture,int image,String description){
        this.id=-1;
        this.idPosture = idPosture;
        this.image=image;
        this.description=description;
    }
    public void save (Context context){
        PostureHelper postureHelper = new PostureHelper(context);
        if (this.id == -1){
            this.id = postureHelper.addPosture(this);
        }
        else {
            postureHelper.updatePosture(this);
        }
    }
    public static Posture find(int idPosture,Context context){
        PostureHelper postureHelper = new PostureHelper(context);
        if (postureHelper.getPosture(idPosture)==null){
            return null;
        }
        else
            return postureHelper.getPosture(idPosture);
    }
    public static int getPostureCount(Context context){
        PostureHelper postureHelper = new PostureHelper(context);
        return postureHelper.postureCount();
    }
    public int getId(){
        return this.id;
    }

    public int getIdPosture() {
        return this.idPosture;
    }

    public int getImage(){
        return this.image;

    }

    public String getDescription(){

      return this.description;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdPosture(int idPosture) {
        this.idPosture = idPosture;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
