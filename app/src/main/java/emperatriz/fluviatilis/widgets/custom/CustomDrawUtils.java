package emperatriz.fluviatilis.widgets.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CustomDrawUtils {
    public static int height,width, p20;
    public static Canvas canvas;
    public static boolean isInAmbientMode;
    private static Paint paint = new Paint();

    public static int offsetX=0;
    public static int offsetY=0;


    private static CustomDrawUtils instance=null;

    public  static CustomDrawUtils init() {

        if (instance==null) {

            instance=new CustomDrawUtils();
        }
        return instance;
    }

    private static float p20(float factor){
        return p20*factor;
    }

    public static void drawNoNotifications(int color, Bitmap by, Paint paint2){
        Paint paint = paint2;
        paint.setColorFilter(new LightingColorFilter(color, 1));
        canvas.drawBitmap(by,(width / 2 - by.getWidth()/2)+offsetX, size(290, width)+offsetY, paint);


    }

    public static float size(float size, int totalSize){
        return (size*totalSize)/454;
    }

    public static int size(int size, int totalSize){
        return Math.round((size*totalSize)/454);
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

    public static void drawDayTimes(int color, String rise, String noon, String set, String nadir, Calendar now, Paint paint2, boolean continuo){
        RectF r1 = new RectF();

        paint.setStrokeWidth(size(20, width));
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
        paint.setAlpha(80);

        if (continuo){
            canvas.drawArc(r1, 200, 140, false, paint);
        }
        else{
            for (int i=0;i<=72;i++){
                if (progDiscrete<i){
                    paint.setStrokeWidth(size(15, width));
                    canvas.drawArc(r1, 198+i*2, 1, false, paint);
                }
            }
        }
        paint.setAlpha(255);
        int sweepAngle = Math.round(prog*140);
        if (continuo){
            canvas.drawArc(r1, 200, sweepAngle, false, paint);
        }
        else{
            for (int i=0;i<=72;i++){
                if (progDiscrete>=i){
                    paint.setStrokeWidth(size(20, width));
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
        //canvas.drawTextOnPath(time1, circle, size(506f, width), size(6, width), paint2);
        canvas.drawTextOnPath(time1, circle, (float) (radius* Math.PI*1.12f), size(6, width), paint2);
        paint2.setTextAlign(Paint.Align.RIGHT);
        canvas.drawTextOnPath(time3, circle, (float) (radius* Math.PI*-0.12f), size(6, width), paint2);
        paint2.setTextAlign(Paint.Align.CENTER);
        canvas.drawTextOnPath(time2, circle, (float) (radius* Math.PI*0.5f), size(6, width), paint2);

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
            paint.setStrokeWidth(size(20, width));
        }
        else{
            paint.setStrokeCap(Paint.Cap.BUTT);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(size(20, width));
        }
        paint.setAntiAlias(true);

        paint.setFilterBitmap(true);


        float x = 2.8f;
        r1.set(p20(x)+offsetX,p20(x)+offsetY,width-p20(x)+offsetX,height-p20(x)+offsetY);
        float[] hsv = new float[3];

        int colorDark = Color.HSVToColor(hsv);

        paint.setAlpha(80);

        int levelDiscrete = 0;
        if (continuo){
            canvas.drawArc(r1, 95, 65, false, paint);
        }
        else{
            levelDiscrete = Math.round((level*36)/100);
            for (int i=0;i<=35;i++){
                if (levelDiscrete<i){
                    paint.setStrokeWidth(size(15, width));
                    canvas.drawArc(r1, 92+i*2, 1, false, paint);
                }
            }
        }

        paint.setAlpha(255);

        float prog = (level*1f)/100;
        int sweepAngle = Math.round(prog*65);
        if (continuo){
            canvas.drawArc(r1, 95, sweepAngle, false, paint);
        }
        else{
            //canvas.drawArc(r1, 95, sweepAngle, false, paint);
            for (int i=0;i<=35;i++){
                if (levelDiscrete>=i){
                    paint.setStrokeWidth(size(20, width));
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
        canvas.drawTextOnPath(value, circle2, (float) (radius* Math.PI*-0.52f), size(9, width), paint2);
        paint2.setTextAlign(Paint.Align.LEFT);
        canvas.drawTextOnPath(title, circle2, (float) (radius* Math.PI*1.12f), size(9, width), paint2);
    }

    public static void drawRightComplication(int color, int level, String title, String value, Paint paint2, boolean continuo){
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
            paint.setStrokeWidth(size(20, width));
        }
        else{
            paint.setStrokeCap(Paint.Cap.BUTT);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(size(20, width));
        }
        paint.setAntiAlias(true);

        paint.setFilterBitmap(true);


        float x = 2.8f;
        r1.set(p20(x)+offsetX,p20(x)+offsetY,width-p20(x)+offsetX,height-p20(x)+offsetY);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.60f; // value component
        paint.setAlpha(80);
        //canvas.drawArc(r1, 20, 65, false, paint);
        int levelDiscrete = 0;
        if (continuo){
            canvas.drawArc(r1, 20, 65, false, paint);
        }
        else{
            levelDiscrete = Math.round((level*36)/100);
            for (int i=0;i<=35;i++){
                if (levelDiscrete<i){
                    paint.setStrokeWidth(size(15, width));
                    canvas.drawArc(r1, 87-i*2, 1, false, paint);
                }
            }
        }
        paint.setAlpha(255);
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
                    paint.setStrokeWidth(size(20, width));
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
        canvas.drawTextOnPath(value, circle2, (float)(radius* Math.PI*1.52f), size(9, width), paint2);
        paint2.setTextAlign(Paint.Align.RIGHT);

        canvas.drawTextOnPath(title, circle2, (float)(radius* Math.PI*-0.12f), size(9, width), paint2);
    }

    public static void drawDate(int color, Calendar now, Typeface font2){

        SimpleDateFormat dia = new SimpleDateFormat("d");
        SimpleDateFormat diaNombre = new SimpleDateFormat("EE");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");

        String mes3 = mes.format(now.getTime()).replace(".","");
        mes3 = mes3.substring(0,3);

        float margin=p20(0.25f);

        Paint p = new Paint();
        p.setAntiAlias(false);
        p.setColor(color);
        p.setTextSize(size(41, width));
        p.setTypeface(font2);
        float textWidth = p.measureText(dia.format(now.getTime()));
        p.setStyle(Paint.Style.FILL);
        canvas.drawText(dia.format(now.getTime()),(width/2-textWidth/2)+offsetX, size(168, width)+offsetY,p);
//        p.setStyle(Paint.Style.STROKE);
//        p.setColor(0xff000000);
//        canvas.drawText(dia.format(now.getTime()),(width/2-textWidth/2)+offsetX,169+offsetY,p);
        p.setTextSize(size(24, width));
        p.setStyle(Paint.Style.FILL);
        //p.setColor(0xff999999);
        float textWidth2 = p.measureText(diaNombre.format(now.getTime()).replace(".",""));
        float textWidth3 = p.measureText(mes3);



        canvas.drawText(diaNombre.format(now.getTime()).replace(".",""),(width/2- Math.round(textWidth/2))-(textWidth2+1)-margin+offsetX+1, size(168, width)+offsetY,p);
        canvas.drawText(mes3,(width/2+ Math.round(textWidth/2))+margin+offsetX-1, size(168, width)+offsetY,p);


        float right = (width/2+ Math.round(textWidth/2))+margin+offsetX-1;
        float left = (width/2- Math.round(textWidth/2))-(1)-margin+offsetX+1;

        if (width==454){
            Paint p4 = new Paint();




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
//        else{
//            paint.setAlpha(120);
//            canvas.drawBitmap(shadowLeft, left-size(20f,width), size(145f,width)+offsetY, paint);
//            canvas.drawBitmap(shadowRight, right, size(145f,width)+offsetY, paint);
//            paint.setAlpha(255);
//        }


    }

}


