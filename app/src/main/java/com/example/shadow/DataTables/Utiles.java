package com.example.shadow.DataTables;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Utiles {
    public static Bitmap getImage(byte[] image){
        return BitmapFactory.decodeByteArray(image,0, image.length);
    }
    public static byte[] getBytes(InputStream inputStream)throws Exception{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int buffersize = 1024;
        byte[] buffer = new byte[buffersize];
        int len=0;
        while((len = inputStream.read(buffer))!=-1)
            byteBuffer.write(buffer,0,len);
        return byteBuffer.toByteArray();
    }
}


