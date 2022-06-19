package emperatriz.fluviatilis.widgets.custom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import org.jetbrains.annotations.NotNull;
import org.shredzone.commons.suncalc.SunTimes;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import emperatriz.fluviatilis.liveWallpaper.R;
import emperatriz.fluviatilis.widgets.WidgetDrawer;
import emperatriz.fluviatilis.widgets.pypots.SysPypots;


public class CustomDrawer implements WidgetDrawer {

    SharedPreferences preferences;


    public Bitmap by;

    Paint paint, paint2, p2;
    int x;
    int y;
    int width;
    int height;
    Context ctx;
    Typeface font2;

    @Override
    public void init(@NotNull Context context, int x, int y, int width, int height) {
        this.height = height;
        this.width = width;
        this.x=100;
        this.y=520;
        this.ctx=ctx;

        CustomDrawUtils.offsetX=x-width/2;
        CustomDrawUtils.offsetY=y-width/2;
        CustomDrawUtils.height=height;
        CustomDrawUtils.width =width;
        CustomDrawUtils.p20=Math.round(width/20);

        preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE);

        font2 = Typeface.createFromAsset(context.getAssets(), "fonts/Square.ttf");
        p2 = new Paint();
        p2.setTypeface(font2);

        paint = new Paint();
        paint.setAntiAlias(true);

        paint2 = new Paint();
        paint2.setAntiAlias(true);

        by = BitmapFactory.decodeResource(context.getResources(), R.drawable.nonot2);
        by = Bitmap.createScaledBitmap(by, SysPypots.size(114, CustomDrawUtils.width),SysPypots.size(27, CustomDrawUtils.width), true);
    }

    @Override
    public void resize(int width, int height) {
        this.height = height;
        this.width = width;

        by = Bitmap.createScaledBitmap(by,SysPypots.size(114, CustomDrawUtils.width),SysPypots.size(27, CustomDrawUtils.width), true);
    }

    @Override
    public void refresh(int width, int height) {
        this.height = height;
        this.width = width;
        by = Bitmap.createScaledBitmap(by,SysPypots.size(114, CustomDrawUtils.width),SysPypots.size(27, CustomDrawUtils.width), true);
    }



    @Override
    public void draw(@NotNull Canvas canvas, boolean isWallpaper, int x, int y) {

        CustomDrawUtils.offsetX=x-width/2;
        CustomDrawUtils.offsetY=y-width/2;
        CustomDrawUtils.canvas=canvas;

        int colorEsfera = preferences.getInt("colorEsfera",0xffffffff);
        int colorAgujas = preferences.getInt("colorAgujas",0xff000000);
        int colorSegundo = preferences.getInt("colorSegundo",0xffff0000);
        int colorBorde = preferences.getInt("colorBorde",0xff555555);
        int colorInfo = preferences.getInt("colorInfo",0xffaaaaaa);

        boolean info = preferences.getBoolean("customInfo",false);

        float longitudHora = 0.55f;
        float longitudMinuto = 0.77f;
        float longitudSegundo = 0.85f;
        float colaSegundo = 0.15f;
        float cuelloAgujas = 0.15f;


        float radius = width/2f;

//        x = Math.round(x+width/2f);
//        y = Math.round(y+height/2f);

        paint.setColor(colorEsfera);
        canvas.drawCircle(x,y, radius,paint);

        float margenBorde = radius/30;

        RectF r1 = new RectF();
        r1.set(x-radius+margenBorde, y-radius+margenBorde,x+radius-margenBorde,radius+y-margenBorde);

        paint2.setColor(colorBorde);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(margenBorde*2);
        paint2.setAntiAlias(true);
        paint2.setStrokeCap(Paint.Cap.BUTT);
        paint2.setFilterBitmap(true);
        for (int i=0;i<=59;i++){
            canvas.drawArc(r1, i*6-0.25f, 0.5f, false, paint2);
        }
        r1.set(x-radius+margenBorde*2, y-radius+margenBorde*2,x+radius-margenBorde*2,radius+y-margenBorde*2);
        paint2.setStrokeWidth(margenBorde*4);
        for (int i=0;i<=11;i++){
            canvas.drawArc(r1, i*30-0.8f, 1.6f, false, paint2);
        }


        if (info){
            double lat = 42.3d;
            double lon = -8.41d;

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            SimpleDateFormat month = new SimpleDateFormat("MMMM");
            SimpleDateFormat day = new SimpleDateFormat("dd");
            Calendar now = Calendar.getInstance();

            try{
                lat = preferences.getLong("latitude", (long) 42.3);
                lon = preferences.getLong("longitude", (long) -8.41);
            }catch (Exception ex){

            }

            SunTimes times = SunTimes.compute().today().at(lat, lon).execute();




            int monthPerc = Math.round(now.get(Calendar.DAY_OF_MONTH)*100f/ LocalDate.now().lengthOfMonth());
            int yearPerc = Math.round(LocalDate.now().getDayOfYear()*100f/ LocalDate.now().lengthOfYear());

            CustomDrawUtils.drawDayTimes(colorInfo, sdf.format(times.getRise()),sdf.format(times.getNoon()), sdf.format(times.getSet()), sdf.format(times.getNadir()),now, p2, false);
            CustomDrawUtils.drawLeftComplication(colorInfo, yearPerc,now.get(Calendar.YEAR)+"",""+yearPerc, p2, false);
            CustomDrawUtils.drawRightComplication(colorInfo, monthPerc, month.format(now.getTime()).substring(0,3), day.format(now.getTime()), p2, false);

            CustomDrawUtils.drawDate(colorInfo, now,  font2);
            CustomDrawUtils.drawNoNotifications(colorInfo,by,p2);

        }

        Calendar c = Calendar.getInstance();
        long millis = c.getTime().getTime();
        float percentageS = (millis%60000)/60000f;
        float percentageM = (millis%3600000)/3600000f;
        float percentageH = (c.get(Calendar.HOUR_OF_DAY)%12)/12f+percentageM/12f;


        double anguloHora = (Math.PI*percentageH*2f) - Math.PI / 2;
        double anguloMinuto = (Math.PI*percentageM*2f) - Math.PI / 2;
        double anguloSegundo = (Math.PI*percentageS*2f) - Math.PI / 2;


        paint.setColor(colorAgujas);
        paint.setStrokeWidth(width/25f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine((float) (x + Math.cos(anguloHora) * radius * cuelloAgujas), (float) (y + Math.sin(anguloHora) * radius * cuelloAgujas), (float) (x + Math.cos(anguloHora) * radius * longitudHora), (float) (y + Math.sin(anguloHora) * radius * longitudHora),paint);
        canvas.drawLine((float) (x + Math.cos(anguloMinuto) * radius * cuelloAgujas), (float) (y + Math.sin(anguloMinuto) * radius * cuelloAgujas), (float) (x + Math.cos(anguloMinuto) * radius * longitudMinuto), (float) (y + Math.sin(anguloMinuto) * radius * longitudMinuto),paint);
        paint.setStrokeWidth(width/60f);
        canvas.drawLine(x, y, (float) (x + Math.cos(anguloHora) * radius * cuelloAgujas), (float) (y + Math.sin(anguloHora) * radius * cuelloAgujas),paint);
        canvas.drawLine(x, y, (float) (x + Math.cos(anguloMinuto) * radius * cuelloAgujas), (float) (y + Math.sin(anguloMinuto) * radius * cuelloAgujas),paint);
        canvas.drawCircle(x,y, radius*cuelloAgujas/3f,paint);
        paint.setColor(colorSegundo);
        paint.setStrokeWidth(width/80f);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawLine((float) (x - Math.cos(anguloSegundo) * radius * colaSegundo), (float) (y-Math.sin(anguloSegundo) * radius * colaSegundo), (float) (x + Math.cos(anguloSegundo) * radius * longitudSegundo), (float) (y + Math.sin(anguloSegundo) * radius * longitudSegundo),paint);
        canvas.drawCircle(x,y, radius*cuelloAgujas/5f,paint);
    }

    @Override
    public boolean isInitialized() {
        return this.ctx!=null && preferences != null;
    }
}
