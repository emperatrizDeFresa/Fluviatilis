package emperatriz.fluviatilis.widgets.spin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Calendar;

import emperatriz.fluviatilis.widgets.WidgetDrawer;

public class SpinDrawer implements WidgetDrawer {

    float chunk = SpinDrawUtils.p20(0.014f);

    static int NORMAL=0, NORMAL_OVERLAP=1, CIRCLE=2, CIRCLE_OVERLAP=3, EXCENTRIC=4, EXCENTRIC_CIRCLE=5,EXCENTRIC_OPEN=6, EXCENTRIC_OPEN_CIRCLE=7;
    static int WEEKDAY=0, STEPS=1;
    int outmode = NORMAL;
    int color = 0xff00ffdd;
    SharedPreferences preferences;

    float speed1 = 1f;
    float speed2 = 2f;
    float speed3 = 4f;
    float speed4 = 7f;

    float size1 = SpinDrawUtils.random(0.55f, 0.80f);
    float size2 = SpinDrawUtils.random(0.55f, 0.80f);
    float size3 = SpinDrawUtils.random(0.35f, 0.70f);
    float size4 = SpinDrawUtils.random(0.35f, 0.70f);


    boolean clockwise1 = SpinDrawUtils.random(0,1)>0.50;
    boolean clockwise2 = !clockwise1;
    boolean clockwise3 = SpinDrawUtils.random(0,1)>0.50;
    boolean clockwise4 = SpinDrawUtils.random(0,1)>0.50;

    Paint paintText,paintTime;

    @Override
    public void init(@NotNull Context context, int x, int y, int width, int height) {
        SpinDrawUtils.now = System.currentTimeMillis();
        SpinDrawUtils.height = height;
        SpinDrawUtils.width = width;

        SpinDrawUtils.isInAmbientMode = false;
        SpinDrawUtils.ctx = context;
        SpinDrawUtils.p20 = width/20;

        SpinDrawUtils.offsetX=100;
        SpinDrawUtils.offsetY=520;


        preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE);

        outmode = preferences.getInt("outmode", 0);
        chunk = SpinDrawUtils.p20(0.014f);

        paintText = new Paint();
        paintTime = new Paint();

        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "fonts/SF Movie Poster Bold.ttf");
        Typeface font2 = Typeface.createFromAsset(context.getAssets(), "fonts/SF Movie Poster Condensed.ttf");
        Typeface font3 = Typeface.createFromAsset(context.getAssets(), "fonts/SF Movie Poster.ttf");
        paintText.setTypeface(font2);
        paintTime.setTypeface(font3);
        paintText.setAntiAlias(true);
        paintTime.setAntiAlias(true);
//            paintRegular.setFakeBoldText(true);
        paintText.setLetterSpacing(0.06f);
    }

    @Override
    public void resize(int width, int height) {
        SpinDrawUtils.height = height;
        SpinDrawUtils.width = width;
        SpinDrawUtils.p20 = width/20;

    }

    @Override
    public void refresh(int width, int height) {
        SpinDrawUtils.height = height;
        SpinDrawUtils.width = width;
        SpinDrawUtils.p20 = width/20;

    }

    float angle1=0f,angle2=0f,angle3=0f,angle4=0f;

    @Override
    public void draw(@NotNull Canvas canvas, boolean isWallpaper, int x, int y) {
        SpinDrawUtils.mTime = Calendar.getInstance();
        SpinDrawUtils.canvas = canvas;

        color = preferences.getInt(SpinDrawUtils.ACCENT_COLOR, 0xff00ffdd);
        outmode = preferences.getInt("outmode", 0);

        SpinDrawUtils.p20 = SpinDrawUtils.width/20;
        chunk = SpinDrawUtils.p20(0.014f)*570/SpinDrawUtils.width;
        SpinDrawUtils.offsetX=x;
        SpinDrawUtils.offsetY=y;
        //chunk = SpinDrawUtils.p20(0.014f);
        SpinDrawUtils.drawBackground(0xff000000, new Paint());


        if (!isWallpaper){
            speed1 = 0.5f;
            speed2 = 1f;
            speed3 = 2f;
            speed4 = 3.5f;
        }

        angle1 = (angle1 + speed1)%360;
        angle2 = (angle2 + speed2)%360;
        angle3 = (angle3 + speed3)%360;
        angle4 = (angle4 + speed4)%360;

        if (outmode==NORMAL){
            SpinDrawUtils.drawSpin(color, angle1, chunk*4, size1, clockwise1,false);
            SpinDrawUtils.drawSpin(color, angle2, chunk*3, size2, clockwise2,true);
            SpinDrawUtils.drawSpin(color, angle3, chunk*2, size3, clockwise3,true);
            SpinDrawUtils.drawSpin(color, angle4, chunk, 1, clockwise4,true);
            SpinDrawUtils.drawSeconds(color, chunk);
        }
        else if (outmode==NORMAL_OVERLAP){
                SpinDrawUtils.drawSpin(color, angle1, chunk*4, size1, clockwise1,false);
                SpinDrawUtils.drawSpin(color, angle2, chunk*4, size2, clockwise2,true);
                SpinDrawUtils.drawSpin(color, angle3, chunk*4, size3, clockwise3,true);
                SpinDrawUtils.drawSpin(color, angle4, chunk * 4, size4, clockwise4, true);
                SpinDrawUtils.drawSpin(color, speed4, chunk*4, 1, clockwise4,true);
                SpinDrawUtils.drawSeconds(color, chunk);
        }
        else if (outmode==CIRCLE){
                SpinDrawUtils.drawSpin(color, angle4, chunk*4, 1, clockwise1,false);
                SpinDrawUtils.drawSpin(color, angle1, chunk*3, size2, clockwise2,true);
                SpinDrawUtils.drawSpin(color, angle2, chunk*2, size3, clockwise3,true);
                SpinDrawUtils.drawSpin(color, angle3, chunk, size1, clockwise1,true);
                SpinDrawUtils.drawCircle(color, chunk, paintText,true);

        }
        else if (outmode==CIRCLE_OVERLAP){
                SpinDrawUtils.drawSpin(color, angle1, chunk*4, size1, clockwise1,false);
                SpinDrawUtils.drawSpin(color, angle2, chunk*4, size2, clockwise2,true);
                SpinDrawUtils.drawSpin(color, angle3, chunk*4, size3, clockwise3,true);
                SpinDrawUtils.drawSpin(color, angle4, chunk * 4, size4, clockwise4, true);
                SpinDrawUtils.drawSpin(color, angle4, chunk*4, 1, clockwise4,true);
                SpinDrawUtils.drawNoCircle(color, chunk, paintText);
        }
        else if (outmode==EXCENTRIC){
                SpinDrawUtils.initMask();

                SpinDrawUtils.drawExcentric(color, 2.3f, chunk * 4, clockwise1);
                SpinDrawUtils.drawExcentric(color, 4.3f, chunk * 4, clockwise2);
                SpinDrawUtils.drawExcentric(color, 3.1f, chunk * 4, clockwise3);
//            SpinDrawUtils.drawBackground(0xffffffff, p);
//            SpinDrawUtils.endMask(p);
                SpinDrawUtils.drawSpin(color, angle4, chunk * 4, 1, clockwise4, true);
                SpinDrawUtils.drawSeconds(color, chunk);
        }
        else if (outmode==EXCENTRIC_CIRCLE){
            SpinDrawUtils.initMask();
                SpinDrawUtils.drawExcentric(color, 2.3f, chunk * 4, clockwise1);
                SpinDrawUtils.drawExcentric(color, 4.3f, chunk * 4, clockwise2);
                SpinDrawUtils.drawExcentric(color, 3.1f, chunk * 4, clockwise3);
                SpinDrawUtils.drawSpin(color, angle4, chunk * 4, 1, clockwise4, true);
                SpinDrawUtils.drawCircle(color, chunk, paintText, true);
        }
        else if (outmode==EXCENTRIC_OPEN){
            SpinDrawUtils.initMask();
                SpinDrawUtils.drawExcentricOpen(color, 2.3f, chunk * 4, clockwise1);
                SpinDrawUtils.drawExcentricOpen(color, 1.7f, chunk * 4, clockwise2);
                SpinDrawUtils.drawExcentricOpen(color, 2.9f, chunk * 4, clockwise3);
                SpinDrawUtils.drawExcentricOpen(color, 1.9f, chunk * 4, clockwise4);

                SpinDrawUtils.drawSpin(color, angle4, chunk * 4, 1, clockwise4, true);
                SpinDrawUtils.drawSeconds(color, chunk);
        }
        else if (outmode==EXCENTRIC_OPEN_CIRCLE){
            SpinDrawUtils.initMask();
                SpinDrawUtils.drawExcentricOpen(color, 2.3f, chunk * 4, clockwise1);
                SpinDrawUtils.drawExcentricOpen(color, 1.7f, chunk * 4, clockwise2);
                SpinDrawUtils.drawExcentricOpen(color, 2.9f, chunk * 4, clockwise3);
                SpinDrawUtils.drawExcentricOpen(color, 1.9f, chunk * 4, clockwise4);
                SpinDrawUtils.drawSpin(color, speed4, chunk * 4, 1, clockwise4, true);
                SpinDrawUtils.drawCircle(color, chunk, paintText, true);
        }




        DecimalFormat df = new DecimalFormat("00");
        float dateHeight = SpinDrawUtils.drawCenteredText(df.format(SpinDrawUtils.mTime.get(Calendar.HOUR_OF_DAY))+":"+ df.format(SpinDrawUtils.mTime.get(Calendar.MINUTE)), paintTime);


        SpinDrawUtils.drawDate(0xffbbbbbb, paintText, dateHeight);

    }

    @Override
    public boolean isInitialized() {
        return SpinDrawUtils.ctx!=null;
    }
}
