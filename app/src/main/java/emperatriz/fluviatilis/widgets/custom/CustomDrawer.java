package emperatriz.fluviatilis.widgets.custom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Calendar;

import emperatriz.fluviatilis.widgets.WidgetDrawer;
import emperatriz.fluviatilis.widgets.spin.SpinDrawUtils;

public class CustomDrawer implements WidgetDrawer {

    SharedPreferences preferences;

    Paint paint;
    int x;
    int y;
    int width;
    int height;
    Context ctx;

    @Override
    public void init(@NotNull Context context, int x, int y, int width, int height) {
        this.height = height;
        this.width = width;
        this.x=100;
        this.y=520;
        this.ctx=ctx;


        preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE);


        paint = new Paint();

    }

    @Override
    public void resize(int width, int height) {
        this.height = height;
        this.width = width;
    }

    @Override
    public void refresh(int width, int height) {
        this.height = height;
        this.width = width;
    }



    @Override
    public void draw(@NotNull Canvas canvas, boolean isWallpaper, int x, int y) {

        int colorEsfera = 0xffff99ff;
        int colorAgujas = 0xffffffff;
        int colorSegundo = 0xffff0000;

        float longitudHora = 0.5f;
        float longitudMinuto = 0.8f;
        float longitudSegundo = 0.85f;


        float radius = width/2f;

        x = Math.round(x+width/2f);
        y = Math.round(y+height/2f);

        paint.setColor(colorEsfera);
        canvas.drawCircle(x,y, radius,paint);

        Calendar c = Calendar.getInstance();
        float millis = c.getTimeInMillis()%60000;

        float percentageS = (c.getTimeInMillis()%60000)/60000f;
        float percentageM = (c.getTimeInMillis()%3600000)/3600000f;
        float percentageH = (c.getTimeInMillis()%43200000*2)/43200000f*2;

        double anguloHora = (Math.PI*percentageH*2f) - Math.PI / 2;
        double anguloMinuto = (Math.PI*percentageM*2f) - Math.PI / 2;
        double anguloSegundo = (Math.PI*percentageS*2f) - Math.PI / 2;


        paint.setColor(colorAgujas);
        canvas.drawLine(x, y, (float) (x + Math.cos(anguloHora) * radius * longitudHora), (float) (y + Math.sin(anguloHora) * radius * longitudHora),paint);
        canvas.drawLine(x, y, (float) (x + Math.cos(anguloMinuto) * radius * longitudMinuto), (float) (y + Math.sin(anguloMinuto) * radius * longitudMinuto),paint);
        paint.setColor(colorSegundo);
        canvas.drawLine(x, y, (float) (x + Math.cos(anguloSegundo) * radius * longitudSegundo), (float) (y + Math.sin(anguloSegundo) * radius * longitudSegundo),paint);

    }

    @Override
    public boolean isInitialized() {
        return this.ctx!=null;
    }
}
