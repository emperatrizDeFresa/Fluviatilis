package emperatriz.fluviatilis.widgets.pypots;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import org.jetbrains.annotations.NotNull;
import org.shredzone.commons.suncalc.SunTimes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import emperatriz.fluviatilis.liveWallpaper.R;
import emperatriz.fluviatilis.widgets.WidgetDrawer;

public class PypotsDrawer implements WidgetDrawer {

    public Bitmap n0_;
    public Bitmap n1_;
    public Bitmap n2_;
    public Bitmap n3_;
    public Bitmap n4_;
    public Bitmap n5_;
    public Bitmap n6_;
    public Bitmap n7_;
    public Bitmap n8_;
    public Bitmap n9_;
    public Bitmap n11_;
    public Bitmap n13_;
    public Bitmap n15_;
    public Bitmap n19_;
    public Bitmap n21_;
    public Bitmap n23_;
    public Bitmap n25_;
    public Bitmap n29_;
    public Bitmap n53_;
    public Bitmap n57_;
    public Bitmap by;
    public Bitmap halo;
    Paint p2;
    Typeface font2;
    public Context context;

    private SharedPreferences preferences;

    private Paint blackPaint;

    @Override
    public boolean isInitialized(){
        return context!=null;
    }

    @Override
    public void init(@NotNull Context context, int x, int y, int width, int height) {
        this.context = context;

        blackPaint = new Paint();
        blackPaint.setColor(0xff000000);
        blackPaint.setAntiAlias(true);

        font2 = Typeface.createFromAsset(context.getAssets(), "fonts/Square.ttf");

        p2 = new Paint();
        p2.setTypeface(font2);


        n0_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n0);
        n1_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n1);
        n2_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n2);
        n3_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n3);
        n4_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n4);
        n5_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n5);
        n6_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n6);
        n7_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n7);
        n8_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n8);
        n9_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n9);

        n11_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n11);
        n13_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n13);
        n15_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n15);
        n19_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n19);
        n21_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n21);
        n23_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n23);
        n25_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n25);
        n29_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n29);
        n53_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n53);
        n57_ = BitmapFactory.decodeResource(context.getResources(), R.drawable.n57);

        by = BitmapFactory.decodeResource(context.getResources(), R.drawable.nonot);

        halo = BitmapFactory.decodeResource(context.getResources(), R.drawable.halo);



        PypotsDrawUtils.offsetX=x;
        PypotsDrawUtils.offsetY=y;
        PypotsDrawUtils.height=height;
        PypotsDrawUtils.width=width;
        PypotsDrawUtils.p20=Math.round(width/20);

        n0_ = Bitmap.createScaledBitmap(n0_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n1_ = Bitmap.createScaledBitmap(n1_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n2_ = Bitmap.createScaledBitmap(n2_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n3_ = Bitmap.createScaledBitmap(n3_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n4_ = Bitmap.createScaledBitmap(n4_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n5_ = Bitmap.createScaledBitmap(n5_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n6_ = Bitmap.createScaledBitmap(n6_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n7_ = Bitmap.createScaledBitmap(n7_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n8_ = Bitmap.createScaledBitmap(n8_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n9_ = Bitmap.createScaledBitmap(n9_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);

        n11_ = Bitmap.createScaledBitmap(n11_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n13_ = Bitmap.createScaledBitmap(n13_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n15_ = Bitmap.createScaledBitmap(n15_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n19_ = Bitmap.createScaledBitmap(n19_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n21_ = Bitmap.createScaledBitmap(n21_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n23_ = Bitmap.createScaledBitmap(n23_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n25_ = Bitmap.createScaledBitmap(n25_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n29_ = Bitmap.createScaledBitmap(n29_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n53_ = Bitmap.createScaledBitmap(n53_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n57_ = Bitmap.createScaledBitmap(n57_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);

        by = Bitmap.createScaledBitmap(by,SysPypots.size(114, PypotsDrawUtils.width),SysPypots.size(27, PypotsDrawUtils.width), true);
        halo = Bitmap.createScaledBitmap(halo, PypotsDrawUtils.width, PypotsDrawUtils.width, true);

    }


    @Override
    public void resize(int width, int height){
        PypotsDrawUtils.height=height;
        PypotsDrawUtils.width=width;
        PypotsDrawUtils.p20=Math.round(width/20);

        n0_ = Bitmap.createScaledBitmap(n0_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n1_ = Bitmap.createScaledBitmap(n1_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n2_ = Bitmap.createScaledBitmap(n2_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n3_ = Bitmap.createScaledBitmap(n3_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n4_ = Bitmap.createScaledBitmap(n4_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n5_ = Bitmap.createScaledBitmap(n5_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n6_ = Bitmap.createScaledBitmap(n6_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n7_ = Bitmap.createScaledBitmap(n7_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n8_ = Bitmap.createScaledBitmap(n8_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n9_ = Bitmap.createScaledBitmap(n9_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);

        n11_ = Bitmap.createScaledBitmap(n11_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n13_ = Bitmap.createScaledBitmap(n13_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n15_ = Bitmap.createScaledBitmap(n15_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n19_ = Bitmap.createScaledBitmap(n19_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n21_ = Bitmap.createScaledBitmap(n21_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n23_ = Bitmap.createScaledBitmap(n23_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n25_ = Bitmap.createScaledBitmap(n25_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n29_ = Bitmap.createScaledBitmap(n29_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n53_ = Bitmap.createScaledBitmap(n53_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n57_ = Bitmap.createScaledBitmap(n57_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);

        by = Bitmap.createScaledBitmap(by,SysPypots.size(114, PypotsDrawUtils.width),SysPypots.size(27, PypotsDrawUtils.width), true);
        halo = Bitmap.createScaledBitmap(halo, PypotsDrawUtils.width, PypotsDrawUtils.width, true);
    }

    @Override
    public void refresh(int width, int height){
        PypotsDrawUtils.height=height;
        PypotsDrawUtils.width=width;
        PypotsDrawUtils.p20=Math.round(width/20);

        n0_ = Bitmap.createScaledBitmap(n0_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n1_ = Bitmap.createScaledBitmap(n1_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n2_ = Bitmap.createScaledBitmap(n2_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n3_ = Bitmap.createScaledBitmap(n3_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n4_ = Bitmap.createScaledBitmap(n4_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n5_ = Bitmap.createScaledBitmap(n5_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n6_ = Bitmap.createScaledBitmap(n6_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n7_ = Bitmap.createScaledBitmap(n7_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n8_ = Bitmap.createScaledBitmap(n8_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n9_ = Bitmap.createScaledBitmap(n9_,SysPypots.size(62, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);

        n11_ = Bitmap.createScaledBitmap(n11_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n13_ = Bitmap.createScaledBitmap(n13_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n15_ = Bitmap.createScaledBitmap(n15_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n19_ = Bitmap.createScaledBitmap(n19_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n21_ = Bitmap.createScaledBitmap(n21_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n23_ = Bitmap.createScaledBitmap(n23_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n25_ = Bitmap.createScaledBitmap(n25_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n29_ = Bitmap.createScaledBitmap(n29_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n53_ = Bitmap.createScaledBitmap(n53_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);
        n57_ = Bitmap.createScaledBitmap(n57_,SysPypots.size(136, PypotsDrawUtils.width),SysPypots.size(62, PypotsDrawUtils.width), true);

        by = Bitmap.createScaledBitmap(by,SysPypots.size(114, PypotsDrawUtils.width),SysPypots.size(27, PypotsDrawUtils.width), true);
        halo = Bitmap.createScaledBitmap(halo, PypotsDrawUtils.width, PypotsDrawUtils.width, true);
    }

    @Override
    public void draw(Canvas canvas, boolean isWallpaper, int x, int y) {
        PypotsDrawUtils.offsetX=x;
        PypotsDrawUtils.offsetY=y;
        PypotsDrawUtils.canvas=canvas;

        canvas.drawOval(PypotsDrawUtils.offsetX, PypotsDrawUtils.offsetY, PypotsDrawUtils.offsetX+ PypotsDrawUtils.width, PypotsDrawUtils.offsetY+ PypotsDrawUtils.height,blackPaint);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat month = new SimpleDateFormat("MMMM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();


        PypotsDrawUtils.drawTime(sdf.format(now.getTime()),n0_,n1_,n2_,n3_,n4_,n5_,n6_,n7_,n8_,n9_,n11_,n13_,n15_,n19_,n21_,n23_,n25_,n29_,n53_,n57_,p2);



        int divs = SysPypots.getInt(SysPypots.SETTINGS_DIVISIONES,2, context);
        if (divs<=3){
            PypotsDrawUtils.drawSecondsMulti(now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND), divs);
        }
        else if(divs<=7){
            PypotsDrawUtils.drawSecondsThin(now.get(Calendar.SECOND)*2+(now.get(Calendar.MILLISECOND)>500?1:0), now.get(Calendar.MILLISECOND), divs-4);
        }
        else if (divs==8){
            PypotsDrawUtils.drawSpin(0xadffffff,4, 1, 0.62f, true, true);
            PypotsDrawUtils.drawSpin(0x99ffffff,6, 2, 0.58f, false, true);
            PypotsDrawUtils.drawSpin(0x67ffffff,9, 3, 0.79f, true, true);
            PypotsDrawUtils.drawSecondsSpin(0xffffffff, 45,now.getTimeInMillis());
        }
        else if (divs==9){
            PypotsDrawUtils.drawSeconds2(0xffffffff, now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND));
        }

        if (SysPypots.getBoolean(SysPypots.SETTINGS_HALO,true, context)) {
            PypotsDrawUtils.drawHalo(halo);
        }




        double lat = 42.3d;
        double lon = -8.41d;

        try{
            lat = preferences.getLong("latitude", (long) 42.3);
            lon = preferences.getLong("longitude", (long) -8.41);
        }catch (Exception ex){

        }

        SunTimes times = SunTimes.compute().today().at(lat, lon).execute();




        int monthPerc = Math.round(now.get(Calendar.DAY_OF_MONTH)*100f/ LocalDate.now().lengthOfMonth());
        int yearPerc = Math.round(LocalDate.now().getDayOfYear()*100f/ LocalDate.now().lengthOfYear());

        PypotsDrawUtils.drawDayTimes(0xffeeaa22, sdf.format(times.getRise()),sdf.format(times.getNoon()), sdf.format(times.getSet()), sdf.format(times.getNadir()),now, p2, !SysPypots.getBoolean(SysPypots.SETTINGS_DISCRETO,false, context));
        PypotsDrawUtils.drawLeftComplication(0xffff4466, yearPerc,now.get(Calendar.YEAR)+"",""+yearPerc, p2, !SysPypots.getBoolean(SysPypots.SETTINGS_DISCRETO,false, context));
        PypotsDrawUtils.drawRightComplication(0xff00bbee, monthPerc, month.format(now.getTime()).substring(0,3), day.format(now.getTime()), p2, !SysPypots.getBoolean(SysPypots.SETTINGS_DISCRETO,false, context));

        PypotsDrawUtils.drawDate(now,  font2);
        PypotsDrawUtils.drawNoNotifications(by,p2);

    }


}
