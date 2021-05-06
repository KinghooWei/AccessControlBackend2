package com.example.cms;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

public class jnaTest {
    public interface Clibrary extends Library {

        Clibrary INSTANCE = (Clibrary) Native.loadLibrary("D:/faceRecognition-jna/build/Face", Clibrary.class);

        boolean FaceModelInit(String weightPath);
        Pointer FaceDetect(byte[] imageDate_, int imageWidth, int imageHeight, int imageChannel);
        String FaceFeature(byte[] imageDate_, int w, int h);
        void Test();
    }

    public static void main(String[] args) {
        Clibrary.INSTANCE.Test();
        try{
            File file=new File("D:/faceRecognition-jna/build/Winnie.jpg");
            BufferedImage bufIma=ImageIO.read(file);//
            int width=bufIma.getWidth();
            int height=bufIma.getHeight();
            byte[] imgRGBA = getPixels(bufIma, 0, 0, width, height);
            boolean inital_ = Clibrary.INSTANCE.FaceModelInit("D:/faceRecognition-jna/weights");
            if(inital_){        //权重文件正常
                Pointer pInt;
                pInt = Clibrary.INSTANCE.FaceDetect(imgRGBA, width, height, 4);
                int faceNum = pInt.getInt(0);       //人脸数量
                System.out.println("face num:"+faceNum);
                if(faceNum==1){     //有一张人脸
                    int[] arr = pInt.getIntArray(0, 1+faceNum*14);
                    int left = arr[1], top = arr[2], w = arr[3]-arr[1], h = arr[4]-arr[2];
                    byte[] faceRGBA = getPixels(bufIma, left, top, w, h);
                    String feastr = Clibrary.INSTANCE.FaceFeature(faceRGBA, w, h);      //人脸特征描述
                    System.out.println(feastr);
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        String base64 = changeImagetoBase64String("D:/faceRecognition-jna/build/Winnie.jpg");
        System.out.println(base64);
    }

    private static byte[] getPixels(BufferedImage image, int left, int top, int width, int height) {
        byte[] result = new byte[height*width*4];
        for (int y = top, j=0; y < top+height; y++,j++) {
            for (int x = left, i=0; x < left+width; x++,i++) {
                Color c = new Color(image.getRGB(x, y), true);
                result[j*width*4 + i*4] = (byte)c.getRed();
                result[j*width*4 + i*4 + 1] = (byte)c.getGreen();
                result[j*width*4 + i*4 + 2] = (byte)c.getBlue();
                result[j*width*4 + i*4 + 3] = (byte)c.getAlpha();
            }
        }
        return result;
    }

    /**
     *
     *@param filePath 照片绝对路径
     *@return String base64码字符串
     *
     **/
    public static String changeImagetoBase64String(String filePath){
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = "";
        File file = new File(filePath);
        BufferedImage buffer;
        try {
            buffer = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(buffer, "jpg", baos);  //第二个参数“jpg”不需要修改
            byte[] bytes = baos.toByteArray();
            base64String = encoder.encodeBuffer(bytes).trim();
            base64String = base64String.replaceAll("\n","").replaceAll("\r","");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64String;
    }

}
