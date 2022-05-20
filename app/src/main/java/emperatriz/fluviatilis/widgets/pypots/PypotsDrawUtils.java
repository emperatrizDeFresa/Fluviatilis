package emperatriz.fluviatilis.widgets.pypots;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PypotsDrawUtils {
    public static int height,width, p20;
    public static Canvas canvas;
    public static boolean isInAmbientMode;
    private static Paint paint = new Paint();

    public static int offsetX=0;
    public static int offsetY=0;


    private static PypotsDrawUtils instance=null;

    public  static PypotsDrawUtils init() {

        if (instance==null) {

            instance=new PypotsDrawUtils();
        }
        return instance;
    }

    private static float p20(float factor){
        return p20*factor;
    }



    public static void drawSeconds(int color, int second,long millisecond){
        RectF r1 = new RectF();

        paint.setStrokeWidth(SysPypots.size(44, width));
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFilterBitmap(true);
        r1.set(SysPypots.size(23, width)+offsetX, SysPypots.size(23, width)+offsetY,width- SysPypots.size(23, width)+offsetX,height- SysPypots.size(23, width)+offsetY);
        paint.setShadowLayer(0, 0, 0, 0x00000000);






        float[] hsv = new float[3];
        float[] hsv2 = new float[3];
        float[] hsv3 = new float[3];
        Color.colorToHSV(color, hsv);
        Color.colorToHSV(color, hsv2);
        Color.colorToHSV(color, hsv3);
        hsv[2] *= 0.45f;
        hsv2[2] *= 0.45f;
        hsv3[2] *= 0.45f;
        int colorDark = Color.HSVToColor(hsv);
        int colorDark2 = Color.HSVToColor(hsv2);
        int colorDark3 = Color.HSVToColor(hsv3);

        if (isInAmbientMode){

            paint.setColor(0xff262626);
            for (int i=0;i<=59;i++){
                canvas.drawArc(r1, i*6-90+1, 4, false, paint);
            }
        }
        else{
            for (int i=0;i<=59;i++){

                int add = i%3==0?0:0;

                float millis = millisecond % 2001;
                int md = (int)Math.floor((millis / 2000l)*(62));
                int md2 = md+90%60;

                if (second==i){
                    paint.setColor(color);
                }
                else if (second==(i+1)%60){
                    paint.setColor(color);
                }
                else if ((second+md)%60==i){
                    paint.setColor(colorDark);
                }
                else if ((second+md-1)%60==i){
                    paint.setColor(colorDark2);
                }
                else if ((second+md+1)%60==i){
                    paint.setColor(colorDark3);
                }
                else if ((second+md+30)%60==i){
                    paint.setColor(colorDark);
                }
                else if ((second+md+29)%60==i){
                    paint.setColor(colorDark2);
                }
                else if ((second+md+31)%60==i){
                    paint.setColor(colorDark3);
                }
                else {
                    paint.setColor(0xff262626);
                }
                canvas.drawArc(r1, i*6-90+1, 4, false, paint);
            }
        }

    }

    public static void drawSecondsMulti(int second, long millisecond, int divisions){
        RectF r1 = new RectF();

        paint.setStrokeWidth(SysPypots.size(44, width));
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFilterBitmap(true);
        r1.set(SysPypots.size(23, width)+offsetX, SysPypots.size(23, width)+offsetY,width- SysPypots.size(23, width)+offsetX,height- SysPypots.size(23, width)+offsetY);
        paint.setShadowLayer(0, 0, 0, 0x00000000);






        float[] hsv = new float[3];
        float[] hsv2 = new float[3];
        float[] hsv3 = new float[3];
        Color.colorToHSV(0xffffffff, hsv);
        Color.colorToHSV(0xffffffff, hsv2);
        Color.colorToHSV(0xffffffff, hsv3);
        //hsv[2] *= 0.45f;
        hsv2[2] *= 0.45f;
        hsv3[2] *= 0.45f;
        int colorDark = Color.HSVToColor(hsv);
        int colorDark2 = Color.HSVToColor(hsv2);
        int colorDark3 = Color.HSVToColor(hsv3);

        if (isInAmbientMode){

            paint.setColor(0xff262626);
            for (int i=0;i<=59;i++){
                canvas.drawArc(r1, i*6-90+1, 4, false, paint);
            }
        }
        else{
            if (divisions==0){
                for (int i=0;i<=59;i++){

                    if (second==i){
                        paint.setColor(0xffffffff);

                    }
                    else if (second==(i+1)%60){
                        paint.setColor(0xffffffff);

                    }
                    else {
                        paint.setColor(0xff262626);
                    }


                    canvas.drawArc(r1, i*6-90+1, 4, false, paint);
                }
            }
            else {
                for (int i=0;i<=59;i++){
                    float millis = millisecond % (divisions*1000+1);
                    int md = (int)Math.floor((millis / (divisions*1000l))*(60+divisions));


                    List<Integer> anim = new ArrayList<Integer>();
                    int ch = 60/divisions;
                    for (int d = 0 ;d < divisions; d++){
                        anim.add((second+md+(ch*d))%60);
                        anim.add((second+md+(ch*d)-1)%60);
                        anim.add((second+md+(ch*d)+1)%60);


                    }

                    if (second==i){
                        paint.setColor(0xffffffff);

                    }
                    else if (second==(i+1)%60){
                        paint.setColor(0xffffffff);

                    }
                    else if (anim.contains(i)){
                        colorDark = awayFromSec(second, i);
                        paint.setColor(colorDark);

                    }
                    else {
                        paint.setColor(0xff262626);

                    }


                    canvas.drawArc(r1, i*6-90+1, 4, false, paint);
                }
            }

        }
    }

    private static int awayFromSec(int sec, int pos){
        int diff1 = Math.abs((pos-sec-60)%(60));
        int diff2 = Math.abs((pos+60-sec)%(60));
        int diff = Math.min(diff1, diff2);
        float perc =  1-(diff/(30f));
        return Color.argb(255,Math.round(190*perc+65), Math.round(190*perc+65), Math.round(190*perc+65));
    }

    public static void drawSecondsThin(int second, long millisecond, int divisions){
        RectF r1 = new RectF();

        paint.setStrokeWidth(SysPypots.size(44, width));
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFilterBitmap(true);
        r1.set(SysPypots.size(23, width)+offsetX, SysPypots.size(23, width)+offsetY,width- SysPypots.size(23, width)+offsetX,height- SysPypots.size(23, width)+offsetY);
        paint.setShadowLayer(0, 0, 0, 0x00000000);






        float[] hsv = new float[3];
        float[] hsv2 = new float[3];
        float[] hsv3 = new float[3];
        Color.colorToHSV(0xffffffff, hsv);
        Color.colorToHSV(0xffffffff, hsv2);
        Color.colorToHSV(0xffffffff, hsv3);
        hsv[2] *= 0.45f;
        hsv2[2] *= 0.45f;
        hsv3[2] *= 0.45f;
        int colorDark = Color.HSVToColor(hsv);
        int colorDark2 = Color.HSVToColor(hsv2);
        int colorDark3 = Color.HSVToColor(hsv3);

        if (isInAmbientMode){

            paint.setColor(0xff262626);
            for (int i=0;i<=119;i++){
                canvas.drawArc(r1, i*3-90+1, 2, false, paint);
            }
        }
        else{
            if (divisions==0){
                for (int i=0;i<=119;i++){

                    if (second==i){
                        paint.setColor(0xffffffff);

                    }
                    else if (second==(i+1)%120){
                        paint.setColor(0xffffffff);

                    }
                    else {
                        paint.setColor(0xff262626);
                    }


                    canvas.drawArc(r1, i*3-90+1, 2, false, paint);
                }
            }
            else {
                for (int i=0;i<=119;i++){
                    float millis = millisecond % (divisions*1000+1);
                    int md = (int)Math.floor((millis / (divisions*1000l))*(120+divisions));

                    List<Integer> anim = new ArrayList<Integer>();
                    int ch = 120/divisions;
                    for (int d = 0 ;d < divisions; d++){
                        anim.add((second+md+(ch*d))%120);
                        anim.add((second+md+(ch*d)-1)%120);
                        anim.add((second+md+(ch*d)+1)%120);
                    }

                    if (second==i){
                        paint.setColor(0xffffffff);

                    }
                    else if (second==(i+1)%120){
                        paint.setColor(0xffffffff);

                    }
                    else if (anim.contains(i)){
                        paint.setColor(colorDark);
                    }
                    else {
                        paint.setColor(0xff262626);
                    }


                    canvas.drawArc(r1, i*3-90+1, 2, false, paint);
                }
            }

        }

    }

    public static void drawSecondsContinuous(int color, int second,long millisecond){
        RectF r1 = new RectF();

        paint.setStrokeWidth(SysPypots.size(44, width));
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFilterBitmap(true);
        r1.set(SysPypots.size(23, width)+offsetX, SysPypots.size(23, width)+offsetY,width- SysPypots.size(23, width)+offsetX,height- SysPypots.size(23, width)+offsetY);
        paint.setShadowLayer(0, 0, 0, 0x00000000);



        int animationMs = 100;


        float[] hsv = new float[3];
        float[] hsv2 = new float[3];
        float[] hsv3 = new float[3];

        Color.colorToHSV(color, hsv);
        Color.colorToHSV(color, hsv2);
        Color.colorToHSV(color, hsv3);


        long ms = millisecond%1000;

        hsv[2] *= 0.45f;


        if (ms<=animationMs){
            hsv2[2] *= (0.15+((ms*0.85f)/animationMs));
        }
        else{
            hsv2[2] *= 1f;
        }

        if (ms<=(1000-animationMs)){
            hsv3[2] *= 1f;
        }
        else{
            hsv3[2] *= (1-(((ms-(1000-animationMs))*0.85f)/animationMs));
        }




        int colorDark = Color.HSVToColor(hsv);
        int colorEnd = Color.HSVToColor(hsv2);
        int colorStart = Color.HSVToColor(hsv3);

        if (isInAmbientMode){

            paint.setColor(0xff262626);
            for (int i=0;i<=59;i++){
                canvas.drawArc(r1, i*6-90+1, 4, false, paint);
            }
        }
        else{
            for (int i=0;i<=59;i++){

                int add = i%3==0?0:0;

                float millis = millisecond % 2001;
                int md = (int)Math.floor((millis / 2000l)*(62));
                int md2 = md+90%60;

                if (second==i){
                    paint.setColor(colorEnd);
                }
                else if (second==(i+1)%60){
                    paint.setColor(color);
                }else if (second==(i+2)%60){
                        paint.setColor(colorStart);
                }
                else if ((second+md)%60==i){
                    paint.setColor(colorDark);
                }
                else if ((second+md-1)%60==i){
                    paint.setColor(colorDark);
                }
                else if ((second+md-2)%60==i){
                    paint.setColor(colorDark);
                }
                else if ((second+md+30)%60==i){
                    paint.setColor(colorDark);
                }
                else if ((second+md+29)%60==i){
                    paint.setColor(colorDark);
                }
                else if ((second+md+28)%60==i){
                    paint.setColor(colorDark);
                }
                else {
                    paint.setColor(0xff262626);
                }
                canvas.drawArc(r1, i*6-90+1, 4, false, paint);
            }
        }

    }

    public static void drawSeconds2(int color, int second,long millisecond){
        RectF r1 = new RectF();

        paint.setStrokeWidth(SysPypots.size(44, width));
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFilterBitmap(true);
        r1.set(SysPypots.size(23, width)+offsetX, SysPypots.size(23, width)+offsetY,width- SysPypots.size(23, width)+offsetX,height- SysPypots.size(23, width)+offsetY);
        paint.setShadowLayer(0, 0, 0, 0x00000000);






        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.45f;
        int colorDark = Color.HSVToColor(hsv);


        if (isInAmbientMode){

            paint.setColor(0xff262626);
            for (int i=0;i<=59;i++){
                canvas.drawArc(r1, i*6-90+1, 4, false, paint);
            }
        }
        else{
            for (int i=0;i<=59;i++){

                if (second==i){
                    paint.setColor(color);
                }
                else if (second==(i+1)%60){
                    paint.setColor(color);
                }
                else if (second>i){
                    paint.setColor(colorDark);
                }
                else{
                    paint.setColor(0xff262626);
                }
                canvas.drawArc(r1, i*6-90+1, 4, false, paint);
            }
        }


    }

    public static void drawSecondsSpin(int color, float chunk, long millis){

            RectF r1 = new RectF();

            paint.setStrokeWidth(SysPypots.size(46, width));
            paint.setAntiAlias(true);
            paint.setStrokeCap(Paint.Cap.BUTT);
            paint.setStyle(Paint.Style.STROKE);
            paint.setFilterBitmap(false);
            float margin = chunk*4;
        r1.set(SysPypots.size(23, width)+offsetX, SysPypots.size(23, width)+offsetY,width- SysPypots.size(23, width)+offsetX,height- SysPypots.size(23, width)+offsetY);

            millis = millis%60000;
            float startAngle = (360f*millis/60000f)-90;

            paint.setColor(Color.argb(255, Color.red(0), Color.green(0), Color.blue(0)));
            canvas.drawArc(r1, startAngle-4, 5, false, paint);
            paint.setColor(color);
            canvas.drawArc(r1, startAngle - 3, 3, false, paint);



    }

    public static void drawHalo(Bitmap halo){
        canvas.drawBitmap(halo, 0+offsetX, 0+offsetY, paint);
    }

    public static void drawSpin(int color, int speed, float widthStroke, float size, boolean clockwise, boolean alpha){


            if (!alpha){
                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);
                hsv[2] *= 0.274f; // value component
                color= Color.HSVToColor(hsv);
            }else{
                color = Color.argb(70, Color.red(color), Color.green(color), Color.blue(color));
            }

            RectF r1 = new RectF();

            paint.setStrokeWidth(SysPypots.size(15*widthStroke, width));
            paint.setAntiAlias(true);
            paint.setStrokeCap(Paint.Cap.BUTT);
            paint.setStyle(Paint.Style.STROKE);
            paint.setFilterBitmap(false);

        r1.set(SysPypots.size(7.5f*widthStroke, width)+offsetX, SysPypots.size(7.5f*widthStroke, width)+offsetY,width- SysPypots.size(7.5f*widthStroke, width)+offsetX,height- SysPypots.size(7.5f*widthStroke, width)+offsetY);

            paint.setShadowLayer(0, 0, 0, 0xff000000);

            paint.setColor(color);
            float millis = System.currentTimeMillis()%(speed*1000);
            if (!clockwise){
                millis=speed*1000-millis;
            }
            float startAngle = (360*millis/(speed*1000))-125;
            canvas.drawArc(r1, startAngle, 360*size, false, paint);


    }

    public static void drawNoNotifications(Bitmap by, Paint paint2){
        Paint paint = paint2;
//        paint.setAntiAlias(false);
//        paint.setTextSize(17);
//        paint.setTextAlign(Paint.Align.LEFT);
//
//
//        float textWidth1 = paint.measureText(text1);
//        paint.setTextSize(10.4f);
//        //float textWidth2 = paint.measureText(text2);
//
//        paint.setColor(0xff999999);
//
//        paint.setTextSize(17);
//        canvas.drawText(text1, width / 2 - textWidth1 / 2, 303, paint);
//        paint.setTextSize(10.4f);
        //canvas.drawText(text2, width / 2 - textWidth2 / 2, 317, paint);
        canvas.drawBitmap(by,(width / 2 - by.getWidth()/2)+offsetX, SysPypots.size(290, width)+offsetY, paint);

//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(0xff000000);
//        paint.setStrokeWidth(1);
//        paint.setTextSize(20);
//        canvas.drawText(text1, width / 2 - textWidth1 / 2, 300, paint);
//        paint.setTextSize(16);
//        canvas.drawText(text2, width / 2 - textWidth2 / 2, 317, paint);




    }

    public static float dayProgress(String time1, String time2, Calendar now){
        try{
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy ");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date t1 = sdf.parse(sdf1.format(now.getTime())+time1);
            Date t2 = sdf.parse(sdf1.format(now.getTime())+time2);
            long t2gt = t2.getTime();
            if (t2gt<t1.getTime()){
                t2gt += 1000*60*60*24;
            }
            long total = t2gt-t1.getTime();
            long ngt = now.getTime().getTime();
            if (ngt<t1.getTime()){
                ngt += 1000*60*60*24;
            }
            long progress =  ngt-t1.getTime();

            return progress/(total*1f);
        }
        catch (Exception ex){
            return 0f;
        }

    }

    private static int getQuarterOfDay(String time1, String time2, String time3, String time4, Calendar nowC){
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmm");
        int rise = Integer.parseInt(time1.replace(":",""));
        int noon = Integer.parseInt(time2.replace(":",""));
        int set = Integer.parseInt(time3.replace(":",""));
        int nadir = Integer.parseInt(time4.replace(":",""));
        int now = Integer.parseInt(sdf1.format(nowC.getTime()));

        if (nadir>rise){
            if (rise<=now && now<noon) return 1;
            if (noon<=now && now<set) return 2;
            if (set<=now && now<nadir) return 3;
            return 4;
        }
        else{
            if (rise<=now && now<noon) return 1;
            if (noon<=now && now<set) return 2;
            if (nadir<=now && now<rise) return 4;
            return 3;
        }
    }

    public static void drawDayTimes(int color, String rise, String noon, String set, String nadir, Calendar now, Paint paint2){
        drawDayTimes(color, rise, noon, set, nadir, now, paint2, true);
    }

    public static void drawDayTimes(int color, String rise, String noon, String set, String nadir, Calendar now, Paint paint2, boolean continuo){
        RectF r1 = new RectF();

        paint.setStrokeWidth(SysPypots.size(20, width));
        paint.setAntiAlias(true);
        if (isInAmbientMode){
            paint.setColor(0xff666666);
        }
        else{
            paint.setColor(color);
        }
        if (continuo){
            paint.setStrokeCap(Paint.Cap.ROUND);
        }
        else{
            paint.setStrokeCap(Paint.Cap.BUTT);
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setFilterBitmap(true);

        int quarter = getQuarterOfDay(rise, noon, set, nadir, now);

        String time1="", time2="", time3="";

        float prog =0;
        switch (quarter){
            case 1:{
                time1 = rise;
                time2 = noon;
                time3 = set;
                prog = dayProgress(rise, noon, now)/2;
                break;
            }
            case 2:{
                time1 = rise;
                time2 = noon;
                time3 = set;
                prog = 0.5f+dayProgress(noon, set, now)/2;
                break;
            }
            case 3:{
                time1 = set;
                time2 = nadir;
                time3 = rise;
                prog = dayProgress(set, nadir, now)/2;
                break;
            }
            case 4:{
                time1 = set;
                time2 = nadir;
                time3 = rise;
                prog = 0.5f+dayProgress(nadir, rise, now)/2;
                break;
            }
        }

        int progDiscrete = Math.round(prog*73);

        float x = 2.8f;
        r1.set(p20(x)+offsetX,p20(x)+offsetY,width-p20(x)+offsetX,height-p20(x)+offsetY);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.60f; // value component
        int colorDark = Color.HSVToColor(hsv);
        if (isInAmbientMode){
            paint.setColor(0xff333333);
        }
        else{
            paint.setColor(colorDark);
        }

        if (continuo){
            canvas.drawArc(r1, 200, 140, false, paint);
        }
        else{
            for (int i=0;i<=72;i++){
                if (progDiscrete<i){
                    paint.setStrokeWidth(SysPypots.size(15, width));
                    canvas.drawArc(r1, 198+i*2, 1, false, paint);
                }
            }
        }
        if (isInAmbientMode){
            paint.setColor(0xff666666);
        }
        else{
            paint.setColor(color);
        }
        int sweepAngle = Math.round(prog*140);
        if (continuo){
            canvas.drawArc(r1, 200, sweepAngle, false, paint);
        }
        else{
            for (int i=0;i<=72;i++){
                if (progDiscrete>=i){
                    paint.setStrokeWidth(SysPypots.size(20, width));
                    canvas.drawArc(r1, 198+i*2, 1, false, paint);
                }
            }
        }
        Path circle  = new Path();
        float radius = (width-2*p20(3.8f))/2;
        circle.addCircle(r1.centerX(), r1.centerY(), radius, Path.Direction.CW);
        if (isInAmbientMode){
            paint2.setColor(0xff666666);
        }
        else{
            paint2.setColor(color);
        }
        paint2.setTextAlign(Paint.Align.LEFT);
        paint2.setAntiAlias(true);
        paint2.setTextSize(p20(0.93f));
        //canvas.drawTextOnPath(time1, circle, SysPypots.size(506f, width), SysPypots.size(6, width), paint2);
        canvas.drawTextOnPath(time1, circle, (float) (radius*Math.PI*1.12f), SysPypots.size(6, width), paint2);
        paint2.setTextAlign(Paint.Align.RIGHT);
        canvas.drawTextOnPath(time3, circle, (float) (radius*Math.PI*-0.12f), SysPypots.size(6, width), paint2);
        paint2.setTextAlign(Paint.Align.CENTER);
        canvas.drawTextOnPath(time2, circle, (float) (radius*Math.PI*0.5f), SysPypots.size(6, width), paint2);

    }

    public static void drawLeftComplication(int color, int level, String title, String value, Paint paint2){
        drawLeftComplication(color, level, title, value, paint2, true);
    }

    public static void drawLeftComplication(int color, int level, String title, String value, Paint paint2, boolean continuo){
        RectF r1 = new RectF();

        if (isInAmbientMode){
            paint.setColor(0xff666666);
        }
        else{
            paint.setColor(color);
        }
        if (continuo){
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(SysPypots.size(20, width));
        }
        else{
            paint.setStrokeCap(Paint.Cap.BUTT);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(SysPypots.size(20, width));
        }
        paint.setAntiAlias(true);

        paint.setFilterBitmap(true);


        float x = 2.8f;
        r1.set(p20(x)+offsetX,p20(x)+offsetY,width-p20(x)+offsetX,height-p20(x)+offsetY);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.60f; // value component
        int colorDark = Color.HSVToColor(hsv);
        if (isInAmbientMode){
            paint.setColor(0xff333333);
        }
        else{
            paint.setColor(colorDark);
        }
        int levelDiscrete = 0;
        if (continuo){
            canvas.drawArc(r1, 95, 65, false, paint);
        }
        else{
            levelDiscrete = Math.round((level*36)/100);
            for (int i=0;i<=35;i++){
                if (levelDiscrete<i){
                    paint.setStrokeWidth(SysPypots.size(15, width));
                    canvas.drawArc(r1, 92+i*2, 1, false, paint);
                }
            }
        }

        if (isInAmbientMode){
            paint.setColor(0xff666666);
        }
        else{
            paint.setColor(color);
        }
        float prog = (level*1f)/100;
        int sweepAngle = Math.round(prog*65);
        if (continuo){
            canvas.drawArc(r1, 95, sweepAngle, false, paint);
        }
        else{
            //canvas.drawArc(r1, 95, sweepAngle, false, paint);
            for (int i=0;i<=35;i++){
                if (levelDiscrete>=i){
                    paint.setStrokeWidth(SysPypots.size(20, width));
                    canvas.drawArc(r1, 92+i*2, 1, false, paint);
                }
            }
        }


        Path circle2  = new Path();
        float radius = (width-2*p20(3.8f))/2;
        circle2.addCircle(r1.centerX(), r1.centerY(), radius, Path.Direction.CCW);
        if (isInAmbientMode){
            paint2.setColor(0xff666666);
        }
        else{
            paint2.setColor(color);
        }
        paint2.setAntiAlias(true);
        paint2.setTextSize(p20(0.93f));
        paint2.setTextAlign(Paint.Align.RIGHT);
        canvas.drawTextOnPath(value, circle2, (float) (radius*Math.PI*-0.52f), SysPypots.size(9, width), paint2);
        paint2.setTextAlign(Paint.Align.LEFT);
        canvas.drawTextOnPath(title, circle2, (float) (radius*Math.PI*1.12f), SysPypots.size(9, width), paint2);
    }

    public static void drawRightComplication(int color, int level, String title, String value,  Paint paint2){
        drawRightComplication(color, level, title, value,  paint2, true);
    }

    public static void drawRightComplication(int color, int level, String title, String value,  Paint paint2, boolean continuo){
        RectF r1 = new RectF();

        if (isInAmbientMode){
            paint.setColor(0xff666666);
        }
        else{
            paint.setColor(color);
        }
        if (continuo){
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(SysPypots.size(20, width));
        }
        else{
            paint.setStrokeCap(Paint.Cap.BUTT);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(SysPypots.size(20, width));
        }
        paint.setAntiAlias(true);

        paint.setFilterBitmap(true);


        float x = 2.8f;
        r1.set(p20(x)+offsetX,p20(x)+offsetY,width-p20(x)+offsetX,height-p20(x)+offsetY);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.60f; // value component
        int colorDark = Color.HSVToColor(hsv);
        if (isInAmbientMode){
            paint.setColor(0xff333333);
        }
        else{
            paint.setColor(colorDark);
        }
        //canvas.drawArc(r1, 20, 65, false, paint);
        int levelDiscrete = 0;
        if (continuo){
            canvas.drawArc(r1, 20, 65, false, paint);
        }
        else{
            levelDiscrete = Math.round((level*36)/100);
            for (int i=0;i<=35;i++){
                if (levelDiscrete<i){
                    paint.setStrokeWidth(SysPypots.size(15, width));
                    canvas.drawArc(r1, 87-i*2, 1, false, paint);
                }
            }
        }
        if (isInAmbientMode){
            paint.setColor(0xff666666);
        }
        else{
            paint.setColor(color);
        }
        float prog = (level*1f)/100;
        int sweepAngle = Math.round(prog*65);
        //canvas.drawArc(r1, 20+(65-sweepAngle), sweepAngle, false, paint);
        if (continuo){
            canvas.drawArc(r1, 20+(65-sweepAngle), sweepAngle, false, paint);
        }
        else{
            //canvas.drawArc(r1, 95, sweepAngle, false, paint);
            for (int i=0;i<=35;i++){
                if (levelDiscrete>=i){
                    paint.setStrokeWidth(SysPypots.size(20, width));
                    canvas.drawArc(r1, 87-i*2, 1, false, paint);
                }
            }
        }
        Path circle2  = new Path();
        float radius = (width-2*p20(3.8f))/2;
        circle2.addCircle(r1.centerX(), r1.centerY(), radius, Path.Direction.CCW);
        if (isInAmbientMode){
            paint2.setColor(0xff666666);
        }
        else{
            paint2.setColor(color);
        }
        paint2.setAntiAlias(true);
        paint2.setTextSize(p20(0.93f));
        paint2.setTextAlign(Paint.Align.LEFT);
        canvas.drawTextOnPath(value, circle2, (float)(radius*Math.PI*1.52f), SysPypots.size(9, width), paint2);
        paint2.setTextAlign(Paint.Align.RIGHT);

        canvas.drawTextOnPath(title, circle2, (float)(radius*Math.PI*-0.12f), SysPypots.size(9, width), paint2);
    }

    public static void drawDate(Calendar now, Typeface font2){

        SimpleDateFormat dia = new SimpleDateFormat("d");
        SimpleDateFormat diaNombre = new SimpleDateFormat("EE");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");

        String mes3 = mes.format(now.getTime()).replace(".","");
        mes3 = mes3.substring(0,3);

        float margin=p20(0.25f);

        Paint p = new Paint();
        p.setAntiAlias(false);
        p.setColor(0xff999999);
        p.setTextSize(SysPypots.size(41, width));
        p.setTypeface(font2);
        float textWidth = p.measureText(dia.format(now.getTime()));
        p.setStyle(Paint.Style.FILL);
        canvas.drawText(dia.format(now.getTime()),(width/2-textWidth/2)+offsetX, SysPypots.size(168, width)+offsetY,p);
//        p.setStyle(Paint.Style.STROKE);
//        p.setColor(0xff000000);
//        canvas.drawText(dia.format(now.getTime()),(width/2-textWidth/2)+offsetX,169+offsetY,p);
        p.setTextSize(SysPypots.size(24, width));
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xff999999);
        float textWidth2 = p.measureText(diaNombre.format(now.getTime()).replace(".",""));
        float textWidth3 = p.measureText(mes3);



        canvas.drawText(diaNombre.format(now.getTime()).replace(".",""),(width/2-Math.round(textWidth/2))-(textWidth2+1)-margin+offsetX+1, SysPypots.size(168, width)+offsetY,p);
        canvas.drawText(mes3,(width/2+Math.round(textWidth/2))+margin+offsetX-1, SysPypots.size(168, width)+offsetY,p);

        if (width==454){
            Paint p4 = new Paint();

            float right = (width/2+Math.round(textWidth/2))+margin+offsetX-1;
            float left = (width/2-Math.round(textWidth/2))-(1)-margin+offsetX+1;


            p4.setColor(0x3f000000);
            canvas.drawRect(right,145+offsetY, right+3, 170+offsetY, p4);
            canvas.drawRect(left,145+offsetY, left-3, 170+offsetY, p4);
            p4.setColor(0x2f000000);
            canvas.drawRect(right+3,145+offsetY, right+5, 170+offsetY, p4);
            canvas.drawRect(left-3,145+offsetY, left-5, 170+offsetY, p4);
            p4.setColor(0x1f000000);
            canvas.drawRect(right+5,145+offsetY, right+7, 170+offsetY, p4);
            canvas.drawRect(left-5,145+offsetY, left-7, 170+offsetY, p4);

        }

    }

    public static boolean isTopSticky(String st){
        if (st.equals("52")) return true;
        if (st.equals("53")) return true;
        if (st.equals("57")) return true;
        return false;
    }

    public static boolean isBottomSticky(String st){
        if (st.equals("11")) return true;
        if (st.equals("13")) return true;
        if (st.equals("15")) return true;
        if (st.equals("19")) return true;
        if (st.equals("21")) return true;
        if (st.equals("23")) return true;
        if (st.equals("25")) return true;
        if (st.equals("29")) return true;

        return false;
    }

    public static Bitmap get(String s, Bitmap n0, Bitmap n1, Bitmap n2, Bitmap n3, Bitmap n4, Bitmap n5, Bitmap n6, Bitmap n7, Bitmap n8, Bitmap n9){
        if (s.equals("0")) return n0;
        if (s.equals("1")) return n1;
        if (s.equals("2")) return n2;
        if (s.equals("3")) return n3;
        if (s.equals("4")) return n4;
        if (s.equals("5")) return n5;
        if (s.equals("6")) return n6;
        if (s.equals("7")) return n7;
        if (s.equals("8")) return n8;
        if (s.equals("9")) return n9;
         return null;
    }

    public static void drawTime(String now, Bitmap n0, Bitmap n1, Bitmap n2, Bitmap n3, Bitmap n4, Bitmap n5, Bitmap n6, Bitmap n7, Bitmap n8, Bitmap n9,
                                Bitmap n11,Bitmap n13,Bitmap n15,Bitmap n19, Bitmap n21,Bitmap n23,Bitmap n25,Bitmap n29,Bitmap n53,Bitmap n57, Paint p2){

        String sticky = "11,13,15,19,21,23,25,29,53,57";

        String hh = now.split(":")[0];
        String mm = now.split(":")[1];

        p2.setColor(0xffffffff);
        canvas.drawRect(SysPypots.size(223,width)+offsetX, SysPypots.size(223,width)+offsetY, SysPypots.size(231,width)+offsetX, SysPypots.size(231,width)+offsetY, p2);
        canvas.drawRect(SysPypots.size(223,width)+offsetX, SysPypots.size(250,width)+offsetY, SysPypots.size(231,width)+offsetX, SysPypots.size(258,width)+offsetY, p2);


        if (sticky.contains(hh)){
            if (hh.equals("11")){
                canvas.drawBitmap(n11, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("11")){
                canvas.drawBitmap(n11, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("13")){
                canvas.drawBitmap(n13, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("15")){
                canvas.drawBitmap(n15, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("19")){
                canvas.drawBitmap(n19, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("21")){
                canvas.drawBitmap(n21, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("23")){
                canvas.drawBitmap(n23, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("25")){
                canvas.drawBitmap(n25, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("29")){
                canvas.drawBitmap(n29, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("53")){
                canvas.drawBitmap(n53, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (hh.equals("57")){
                canvas.drawBitmap(n57, SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            }
        }
        else{
            canvas.drawBitmap(get(hh.substring(0,1),n0, n1, n2, n3, n4, n5, n6, n7, n8, n9), SysPypots.size(75f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            canvas.drawBitmap(get(hh.substring(1),n0, n1, n2, n3, n4, n5, n6, n7, n8, n9), SysPypots.size(149f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
        }
        if (sticky.contains(mm)){
            if (mm.equals("11")){
                canvas.drawBitmap(n11, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("11")){
                canvas.drawBitmap(n11, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("13")){
                canvas.drawBitmap(n13, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("15")){
                canvas.drawBitmap(n15, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("19")){
                canvas.drawBitmap(n19, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("21")){
                canvas.drawBitmap(n21, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("23")){
                canvas.drawBitmap(n23, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("25")){
                canvas.drawBitmap(n25, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("29")){
                canvas.drawBitmap(n29, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("53")){
                canvas.drawBitmap(n53, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            } else if (mm.equals("57")){
                canvas.drawBitmap(n57, SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            }
        }
        else{
            canvas.drawBitmap(get(mm.substring(0,1),n0, n1, n2, n3, n4, n5, n6, n7, n8, n9), SysPypots.size(243f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
            canvas.drawBitmap(get(mm.substring(1),n0, n1, n2, n3, n4, n5, n6, n7, n8, n9), SysPypots.size(317f,width)+offsetX, SysPypots.size(196f,width)+offsetY, paint);
        }


    }

    public static void drawNotifications(int notifications,  boolean showNumber, Typeface font2, long millisecond){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff999999);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(SysPypots.size(209, width)+offsetX, SysPypots.size(291, width)+offsetY, SysPypots.size(245, width)+offsetX, SysPypots.size(327, width)+offsetY,paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(SysPypots.size(5, width));
        RectF r = new RectF(SysPypots.size(200, width)+offsetX, SysPypots.size(282, width)+offsetY, SysPypots.size(254, width)+offsetX, SysPypots.size(336, width)+offsetY);
        canvas.drawOval(SysPypots.size(200, width)+offsetX, SysPypots.size(282, width)+offsetY, SysPypots.size(254, width)+offsetX, SysPypots.size(336, width)+offsetY,paint);
        paint.setColor(0xff000000);
        paint.setTextSize(SysPypots.size(34, width));
        paint.setTypeface(font2);
        paint.setStyle(Paint.Style.FILL);
        float textWidth = paint.measureText(notifications+"");
        paint.setAntiAlias(false);
        if (showNumber && notifications<10){
            canvas.drawText(notifications+"",(width/2-textWidth/2)+offsetX,321+offsetY,paint);
        }
        paint.setAntiAlias(true);
        paint.setColor(0xff999999);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(SysPypots.size(2, width));
        canvas.drawOval(SysPypots.size(210, width)+offsetX, SysPypots.size(292, width)+offsetY, SysPypots.size(243, width)+offsetX, SysPypots.size(325, width)+offsetY,paint);
        paint.setColor(0xff000000);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(SysPypots.size(7, width));
        paint.setFilterBitmap(true);
        long speed = notifications*2000;
        float millis = millisecond % (speed+1);
        int md = (int)Math.floor((millis / speed)*360);
        for (int i=0;i<notifications; i++){
            canvas.drawArc(r,((360/notifications)*i)-md,15,false, paint);
        }

    }

}


