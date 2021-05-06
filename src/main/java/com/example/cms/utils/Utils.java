package com.example.cms.utils;


import com.example.cms.bean.PersonBean;
import com.example.cms.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

@Component
public class Utils {
    @Autowired
    PersonService personService;

    public static Utils utils;

    @PostConstruct
    public void init() {
        utils = this;
        utils.personService = this.personService;
    }

    public static String getSecondTimestamp() {
        //获取当前时间戳
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(timeStamp);
        return simpleDateFormat.format(date);
    }

    /*
    base64转字节数组
     */
    public static byte[] base64ToBytes(String src) {
        return Base64.getDecoder().decode(src);
    }

    static class DestroyTempPsdTask extends TimerTask {

        private String phoneNum;

        DestroyTempPsdTask(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        @Override
        public void run() {
            System.out.println("销毁临时密码！用户为:" + phoneNum);
            List<PersonBean> persons = utils.personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                utils.personService.updateTempPassword(phoneNum, "");
            }
        }
    }

    public static class TimerManager {
        private static String phoneNum;

        /**
         * 单例模式
         */
        private static TimerManager timerManager = null;

        public TimerManager(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public static TimerManager getInstance(String phoneNum) {
            if (timerManager == null) {
                timerManager = new TimerManager(phoneNum);
            }
            return timerManager;
        }

        /**
         * 定时器
         */
        private Timer timer = new Timer("destroyTempPwd");

        /**
         * 定时任务
         */
        private DestroyTempPsdTask timerTask = null;

        /**
         * 启动定时任务
         */
        public void startTimerTask() {
            timer.purge();
            if (timerTask == null) {
                timerTask = new DestroyTempPsdTask(phoneNum);
            }
            timer.schedule(timerTask, 60 * 1000);
        }

        /**
         * 定时任务取消
         */
        public void stopTimerTask() {
            timerTask.cancel();
            timerTask = null;//如果不重新new，会报异常
        }
    }

    /**
     * base64 编码转换为 BufferedImage
     */
    public static BufferedImage base64ToBufferedImage(String base64) {
        try {
            byte[] bytes1 = Base64.getDecoder().decode(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int[][] getPixels(BufferedImage bimg) {
        int[][] pixels = new int[bimg.getWidth()][bimg.getHeight()];
        //方式一：通过getRGB()方式获得像素矩阵
        //此方式为沿Height方向扫描
        for (int i = 0; i < bimg.getWidth(); i++) {
            for (int j = 0; j < bimg.getHeight(); j++) {
                pixels[i][j] = bimg.getRGB(i, j);
            }
        }
        return pixels;
    }

    /**
     * 转换BufferedImage 数据为byte数组
     */
    public static byte[] buffered2bytes(BufferedImage bufferedImage){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        try {
            ImageIO.write(bufferedImage, "png", baos);//写入流中
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();//转换成字节
    }

    // 产生logistic混沌序列
    private static int[] sequenceGenerator(double x0, int timeStep) {
        final double u = 3.9;                        // 控制参数u
        double[] x = new double[timeStep + 1000];

        x[0] = x0;
        // 迭代产生混沌序列，长度为 “timeStep+1000”
        for (int i = 0; i < timeStep + 999; i++) {
            x[i + 1] = u * x[i] * (1 - x[i]);       // 一维Logistic混沌系统
        }

        double[] new_x = Arrays.copyOfRange(x, 1000, timeStep + 1000);    // 去除前1000个混沌值，去除暂态效应
        int[] seq = new int[timeStep];
        // 处理混沌序列值
        for (int i = 0; i < timeStep; i++) {
            new_x[i] = new_x[i] * Math.pow(10, 4) - Math.floor(new_x[i] * Math.pow(10, 4));
            seq[i] = (int) Math.floor(Math.pow(10, 9) * new_x[i]);
        }
        return seq;
    }

    // 加解密程序
    public static BufferedImage processBitmap(int[][] pixels, double key) {
        int w = pixels.length;                            // 位图高度
        int h = pixels[0].length;                            // 位图宽度
        int mArrayColorLength = h * w;
        BufferedImage bimg = new BufferedImage(w,h,TYPE_INT_ARGB);
        int[] s = sequenceGenerator(key, mArrayColorLength);    // 设置Logistic混沌系统初始值和迭代次数
        int[][] mArrayColor = new int[w][h];
        // 遍历位图
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pixel = pixels[j][i];
                mArrayColor[j][i] = pixel ^ s[i * w + j];            // 位图像素值与混沌序列值作异或

                bimg.setRGB(j,i,mArrayColor[j][i]);
            }
        }
        return bimg;
    }
}

