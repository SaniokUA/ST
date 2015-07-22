package azaza.myapplication.Libs.Image;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import azaza.myapplication.GlobalData.UserData;
import azaza.myapplication.Settings.EditSettings;

/**
 * Created by Alex on 20.07.2015.
 */
public class ConvertImageBase64 {

    public static void saveImageInBase64(InputStream stream, Activity activity){
        Bitmap realImage = BitmapFactory.decodeStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        realImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        SharedPreferences settings =  activity.getSharedPreferences("CallManager", Context.MODE_PRIVATE);
        EditSettings.saveUserImage(settings, encodedImage);
    }

    public static void loadImageInBase64(String image){
        if( !image.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            UserData.setUserPhotoDrawble(bitmap);
        }
    }

}
