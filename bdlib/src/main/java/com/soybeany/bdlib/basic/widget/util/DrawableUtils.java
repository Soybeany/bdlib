package com.soybeany.bdlib.basic.widget.util;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.util.context.BDContext;
import com.soybeany.bdlib.util.file.FileUtils;
import com.soybeany.bdlib.util.file.IInputStreamCallback;

import java.io.File;
import java.io.InputStream;

/**
 * 图片工具类(能有效减少内存消耗)
 * <br>Created by Ben on 2016/6/22.
 */
public class DrawableUtils {

    /**
     * 文本副颜色
     */
    public static final int TEXT_COLOR_SEC = android.R.attr.textColorSecondary;

    private static final Resources RESOURCES = BDContext.getResources(); // 资源

    /**
     * 图标尺寸(默认)
     */
    public static final int ICON_SIZE_DEFAULT = RESOURCES.getDimensionPixelSize(R.dimen.img_normal);

    /**
     * 图标尺寸(大)
     */
    public static final int ICON_SIZE_BIG = RESOURCES.getDimensionPixelSize(R.dimen.img_big);

    /**
     * 图标尺寸(巨大)
     */
    public static final int ICON_SIZE_HUGE = RESOURCES.getDimensionPixelSize(R.dimen.img_huge);

    /**
     * 开始建造
     */
    public static DrawableBuilder start(int resId) {
        return new DrawableBuilder(resId);
    }

    /**
     * 开始建造
     */
    public static DrawableBuilder start(File file) {
        return new DrawableBuilder(file, null);
    }

    /**
     * 开始建造
     */
    public static DrawableBuilder start(File file, BitmapFactory.Options options) {
        return new DrawableBuilder(file, options);
    }

    /**
     * 设置颜色
     */
    public static void setColor(Drawable drawable, int colorResId) {
        DrawableCompat.setTintList(DrawableCompat.wrap(drawable).mutate(), RESOURCES.getColorStateList(colorResId));
    }

    /**
     * 调整图片尺寸
     *
     * @param isDirect 是否直接设置值
     */
    public static Drawable resize(Drawable drawable, int width, int height, boolean isDirect) {
        if (null == drawable) {
            return null;
        }
        int[] size = new int[]{width, height};
        if (!isDirect) {
            // 图片原尺寸
            int oWidth = drawable.getIntrinsicWidth();
            int oHeight = drawable.getIntrinsicHeight();
            // 计算合适的尺寸
            calculateSize(new int[]{oWidth, oHeight}, width, height, size);
        }
        // 设置尺寸
        drawable.setBounds(0, 0, size[0], size[1]);
        return drawable;
    }

    /**
     * 测量指定图片文件的尺寸
     *
     * @return 0:宽度 1:高度
     */
    public static int[] measure(File file) {
        return FileUtils.handleFileStream(file, new IInputStreamCallback<int[]>() {
            @Override
            public int[] onHandle(InputStream stream) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(stream, null, options);
                return new int[]{options.outWidth, options.outHeight};
            }
        });
    }

    /**
     * 计算合适的尺寸
     *
     * @param size   原始尺寸(0:宽度 1:高度)
     * @param width  限制的宽度(小于0时，则不考虑此维度)
     * @param height 限制的高度(小于0时，则不考虑此维度)
     * @param result 结果数组
     * @return 比值
     */
    public static float calculateSize(int[] size, int width, int height, @NonNull int[] result) {
        // 获得合适的初始尺寸
        int oWidth = size[0] > 0 ? size[0] : width;
        int oHeight = size[1] > 0 ? size[1] : height;
        // 获得x、y维度上的比率
        float ratioX = width > 0 ? width * 1.0f / oWidth : Integer.MAX_VALUE;
        float ratioY = height > 0 ? height * 1.0f / oHeight : Integer.MAX_VALUE;
        // 获得最小比率
        float minRatio = Math.min(ratioX, ratioY);
        if (minRatio == Integer.MAX_VALUE) {
            minRatio = 1;
        }
        // 设置结果
        result[0] = (int) (minRatio * oWidth - 0.5);
        result[1] = (int) (minRatio * oHeight - 0.5);
        return minRatio;
    }

    /**
     * 图片构造器
     */
    public static class DrawableBuilder {

        private BitmapDrawable mDrawable; // 生成的图片

        private DrawableBuilder(int resId) {
            mDrawable = FileUtils.handleResStream(resId, new GetDrawableCallback(null));
        }

        DrawableBuilder(File file, BitmapFactory.Options options) {
            mDrawable = FileUtils.handleFileStream(file, new GetDrawableCallback(options));
        }

        /**
         * 调整图片尺寸
         */
        public DrawableBuilder resize(int size) {
            return resize(size, size, false);
        }

        /**
         * 调整图片尺寸
         */
        public DrawableBuilder resize(int width, int height, boolean isDirect) {
            DrawableUtils.resize(mDrawable, width, height, isDirect);
            return this;
        }

        /**
         * 生成图片
         */
        public BitmapDrawable build() {
            return mDrawable;
        }

        /**
         * 获得图片的回调
         */
        private class GetDrawableCallback implements IInputStreamCallback<BitmapDrawable> {

            private BitmapFactory.Options mOptions;

            GetDrawableCallback(BitmapFactory.Options options) {
                mOptions = options;
            }

            @Override
            public BitmapDrawable onHandle(InputStream stream) {
                return (BitmapDrawable) new BitmapDrawable(RESOURCES, BitmapFactory.decodeStream(stream, null, mOptions)).mutate();
            }
        }
    }

}
