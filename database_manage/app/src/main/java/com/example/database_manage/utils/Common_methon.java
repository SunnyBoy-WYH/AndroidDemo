package com.example.database_manage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Common_methon {
    public static Bitmap compressBoundsBitmap(Context context, Uri uri, int targetWidth, int targetHeight) {
        InputStream input = null;
        Bitmap bitmap = null;

        try {
            input = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, (Rect)null, options);
            input.close();
            int originalWidth = options.outWidth;
            int originalHeight = options.outHeight;
            if(originalWidth != -1 && originalHeight != -1) {
                boolean be1 = true;
                int widthBe = 1;
                if(originalWidth > targetWidth) {
                    widthBe = originalWidth / targetWidth;
                }

                int heightBe = 1;
                if(originalHeight > targetHeight) {
                    heightBe = originalHeight / targetHeight;
                }

                int be2 = widthBe > heightBe?heightBe:widthBe;
                if(be2 <= 0) {
                    be2 = 1;
                }

                options.inJustDecodeBounds = false;
                options.inSampleSize = be2;
                input = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(input, (Rect)null, options);
                input.close();
                input = null;
            } else {
                Object be = null;
            }
        } catch (FileNotFoundException var23) {
            ;
        } catch (IOException var24) {
            ;
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch (IOException var22) {
                    ;
                }
            }

            return bitmap;
        }
    }
}
