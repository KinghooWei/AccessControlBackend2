package com.example.cms.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FaceReg {
    private static boolean flagInitial = false;

    public interface Clibrary extends Library {

        Clibrary INSTANCE = (Clibrary) Native.loadLibrary("D:/faceRecognition-jna/build/Face", Clibrary.class);

        boolean FaceModelInit(String weightPath);

        Pointer FaceDetect(byte[] imageDate_, int imageWidth, int imageHeight, int imageChannel);

        String FaceFeature(byte[] imageDate_, int w, int h);

//        void Test();
    }

    public static String getFaceFeature(BufferedImage bufIma) {
        if (flagInitial) {      //权重文件正常
            int width = bufIma.getWidth();
            int height = bufIma.getHeight();
            byte[] imgRGBA = getPixels(bufIma, 0, 0, width, height);
            Pointer pInt;
            pInt = Clibrary.INSTANCE.FaceDetect(imgRGBA, width, height, 4); //人脸检测，门禁机也有
            int faceNum = pInt.getInt(0);
            System.out.println("face num:" + faceNum);
            if (faceNum == 1) {     //有一张人脸
                int[] arr = pInt.getIntArray(0, 1 + faceNum * 14);
                int left = arr[1], top = arr[2], w = arr[3] - arr[1], h = arr[4] - arr[2];
                byte[] faceRGBA = getPixels(bufIma, left, top, w, h);   //裁剪
                //
                //服务器提取特征
                return Clibrary.INSTANCE.FaceFeature(faceRGBA, w, h);
            } else {
                System.out.println("detect no face from picture, please check");
                return "";
            }
        } else {
            System.out.println("the model has not initialed!");
            return "";
        }
    }

    public void initialModel(String weightsPath) {
        boolean isInitial = Clibrary.INSTANCE.FaceModelInit(weightsPath);
        if (!isInitial) {
            System.out.println("初始化失败，请检查权重路径（注意不要以/结尾）");
        }
        flagInitial = isInitial;
    }

//    public static void main(String[] args) {
////        Clibrary.INSTANTCE.Test();
//        if (!flagInitial) {
//            initialModel("E:/jna/build/weights");
//        }
//        try {
//            File file = new File("E:/jna/build/wegen.jpg");
//            BufferedImage bufIma = ImageIO.read(file);//
//            String feastr = getFaceFeature(bufIma);
//            System.out.println(feastr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    private static byte[] getPixels(BufferedImage image, int left, int top, int width, int height) {
        byte[] result = new byte[height * width * 4];
        for (int y = top, j = 0; y < top + height; y++, j++) {
            for (int x = left, i = 0; x < left + width; x++, i++) {
                Color c = new Color(image.getRGB(x, y), true);
                result[j * width * 4 + i * 4] = (byte) c.getRed();
                result[j * width * 4 + i * 4 + 1] = (byte) c.getGreen();
                result[j * width * 4 + i * 4 + 2] = (byte) c.getBlue();
                result[j * width * 4 + i * 4 + 3] = (byte) c.getAlpha();
            }
        }
        return result;
    }

}

