package com.fatel.mamtv1;

import java.util.ArrayList;

/**
 * Created by Administrator on 24/10/2558.
 */
public class ImageCollection {

    private ArrayList<Image> imageCollection=new ArrayList<Image>();;
    private int count=0;

    public ImageCollection(){


    }

    public void addImage(int image,String description){

        Image img = new Image(this.count,image,description);
        imageCollection.add(img);
        this.count++;

    }
    


}
