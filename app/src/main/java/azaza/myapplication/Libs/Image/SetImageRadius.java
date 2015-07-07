package azaza.myapplication.Libs.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Created by Alex on 07.07.2015.
 */
public class SetImageRadius {

    public static Bitmap getCircleMaskedBitmapUsingShader(Bitmap source, int radius)
    {
        if (source == null)
        {
            return null;
        }

        int diam = radius << 1;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Bitmap scaledBitmap = scaleTo(source, diam);
        final Shader shader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        Bitmap targetBitmap = Bitmap.createBitmap(diam, diam, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);

        canvas.drawCircle(radius, radius, radius, paint);

        return targetBitmap;
    }

    public static Bitmap scaleTo(Bitmap source, int size)
    {
        int destWidth = source.getWidth();

        int destHeight = source.getHeight();

        destHeight = destHeight * size / destWidth;
        destWidth = size;

        if (destHeight < size)
        {
            destWidth = destWidth * size / destHeight;
            destHeight = size;
        }

        Bitmap destBitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(destBitmap);
        canvas.drawBitmap(source, new Rect(0, 0, source.getWidth(), source.getHeight()), new Rect(0, 0, destWidth, destHeight), new Paint(Paint.ANTI_ALIAS_FLAG));
        return destBitmap;
    }

    public static Bitmap getRoundedCornersImage(Bitmap source, int radiusPixels) {

        if (source == null) {
            //we cant proccess null image, go out
            return null;
        }
        final int sourceWidth = source.getWidth();
        final int sourceHeight = source.getHeight();
        final Bitmap output = Bitmap.createBitmap(sourceWidth, sourceHeight, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = 0xFF000000;
        final Paint paint = new Paint();
        paint.setColor(color);

        final Rect rect = new Rect(0, 0, sourceWidth, sourceHeight);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, radiusPixels, radiusPixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);

        return output;
    }

}
