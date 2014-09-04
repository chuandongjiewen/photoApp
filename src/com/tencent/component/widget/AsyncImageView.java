package com.tencent.component.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.qzonex.module.photo.ui.ImageDownloadAsyncTask;
import com.tencent.component.media.image.processor.OvalProcessor;

/**
 * 这是一个ImageView的增强版,配合{@link com.qzonex.module.photo.ui.ImageDownloadAsyncTask},实现了显示网络图片的功能.
 * 主要为了演示图片下载,缓存,解码等过程.
 * 实际项目中的AsyncImageView会比这个强大很多
 * Created by fantouch on 14-8-4.
 */
public class AsyncImageView extends RoundedImageView {


    /**
     * 以下3个为Android View类的3个标准构造方法.
     * 一般情况下,第一个是代码实例化时候使用,第二个是xml内实例化时候使用,第三个是xml内实例化,并指定了style
     */
    public AsyncImageView(Context context) {
        super(context);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 实际项目中,这方法指定图片处理器为圆形图片,但OvalProcessor太庞大,不方便移植过来独立项目中.
     * 所以这方法啥都不干,仅保持代码结构不变,这种方法叫做"代码插桩""Code Stub"
     * 本例子中,圆形图片处理,由父类RoundedImageView完成了
     */
    public void setAsyncImageProcessor(OvalProcessor ovalProcessor) { /*Code Stub*/ }

    /** 异步加载网络图片,实现方法被精简的插桩代码{@link com.qzonex.module.photo.ui.ImageDownloadAsyncTask}所代替 */
    public void setAsyncImage(final String logoUrl) {
        //由于ListView Adapter的复用机制,这个方法在ListView滑动的时候会被多次调用,每次调用传入的logoUrl都不一样.
        // 这里保存最新的url.供图片下载完毕时候比较url是否已经改变
        setTag(logoUrl);

        new ImageDownloadAsyncTask(getContext()) {//处理耗时的下载任务

            /**
             * 这是AsyncTask一般都会重写的方法
             * 父类的doInBackground方法执行完毕后,此方法会在UI线程(主线程)被调用
             * 这时候应当进行UI刷新操作
             * @param bitmap 下载好的图片
             */
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                String url = (String) AsyncImageView.this.getTag();//获得最新的url
                if (TextUtils.equals(url, logoUrl) && bitmap != null) {//如果最新的url与当初发起请求的url一致,则显示对应的图片
                    setImageBitmap(bitmap);
                }
            }

        }.execute(logoUrl);

    }

}
