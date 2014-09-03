package com.qzonex.module.photo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 继承AsyncTask,实现后台下载图片文件的功能
 */
public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {

    /*httpClient配置参数*/
    private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    private static final int DEFAULT_HOST_CONNECTIONS = 10;
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 4 * 1024;

    /**
     * http客户端,下载图片文件的时候会用到.
     * 声明为static,避免生成多个对象.它会依赖于类而存在,无论创建多少个AsyncImageView对象,都共用一个httpClient对象.
     * 但也造成不能自动释放.
     * 需要手动释放,见onDetachedFromWindow()方法
     */
    private volatile static HttpClient httpClient;

    /** app上下文,一般用于获取资源,缓存目录等 */
    private Context mContext;

    /** 用于缓存曾经计算过的md5值 */
    private Map<String, String> mMd5CacheMap = new HashMap<String, String>();

    /**
     * 构造方法
     * @param context app上下文,一般用于获取资源,缓存目录等
     */
    public ImageDownloadAsyncTask(Context context) {
        mContext = context;
    }

    /**
     * 这是AsyncTask必须重写的方法
     * 不在UI线程(主线程)被调用,而是交由系统的线程池处理,耗时的操作都应该在这里执行
     * 执行完后返回结果,结果被传递给onPostExecute方法,UI展示结果.
     * 这里返回给UI线程的是Bitmap(图片)对象
     */
    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        File cacheFile = getCacheFile(url);
        if (cacheFile == null || !cacheFile.exists()) {//如果没有缓存文件
            cacheFile = downloadFile(url);//下载文件,这里会阻塞直到文件下载完成
        }
        if (cacheFile != null) {
            return BitmapFactory.decodeFile(cacheFile.getAbsolutePath());//解码缓存的图片文件为文件对象
        } else {
            return null;
        }
    }


    /** 根据url查找指定的缓存文件 */
    private File getCacheFile(String url) {
        File cacheDir = mContext.getCacheDir();//获得缓存目录
        String md5FileName = md5(url);//构造url对应的md5值,作为文件名
        File cacheFile = new File(cacheDir, md5FileName);//创建缓存文件对象
        if (cacheFile.exists()) {//如果缓存文件对象指向的文件真实存在,则返回文件对象
            return cacheFile;
        } else {
            return null;
        }
    }

    /**
     * 根据url下载文件到本地
     * @return 如果下载失败, 则返回null
     */
    private File downloadFile(String url) {
        InputStream httpResponseInputStream = null;
        FileOutputStream fileOutputStream = null;
        File cacheFile = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpResponseInputStream = getHttpClient().execute(httpGet).getEntity().getContent();//从响应对象中获得数据输入流
            File cacheDir = mContext.getCacheDir();//缓存目录
            String md5FileName = md5(url);//url转换成md5作为文件名
            cacheFile = new File(cacheDir, md5FileName);//缓存文件
            fileOutputStream = new FileOutputStream(cacheFile);//创建指向缓存文件的数据输出流

            byte[] buffer = new byte[64 * 1024];//开辟64k内存作为数据缓冲区
            int readBytesCount = 0;//从数据输入流的读取到的字节数
            while (readBytesCount != -1) {//根据文档,如果数据流读取完毕,则值为-1
                readBytesCount = httpResponseInputStream.read(buffer);//填满缓冲区buffer,readBytesCount为实际读取到buffer中的字节数(每次读取一个buffer长度的数据,到文件末尾,可能不足以填满buffer)
                if (readBytesCount != -1) {
                    fileOutputStream.write(buffer, 0, readBytesCount);//把buffer中的数据写入文件,readResult表示buffer中有效数据长度(文件末尾,可能不足以填满buffer)
                }
            }
        } catch (IOException e) {//捕获异常
            e.printStackTrace();
            if (cacheFile != null && cacheFile.exists()) {//发生任何异常情况,删除缓存文件,不留下脏文件
                cacheFile.delete();
            }
        } finally {//最终操,典型的 try…catch…finally 结构
            if (httpResponseInputStream != null) {
                try {
                    httpResponseInputStream.close();//释放资源
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();//释放资源
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cacheFile;
    }

    /** 把url转换成md5字符串 */
    private String md5(String string) {
        String md5String = mMd5CacheMap.get(string);
        if (md5String != null) {//优先使用缓存值
            return md5String;

        } else {//没有缓存值就实时计算
            //声明为synchronized方法,表明此代码块不允许多线程同时调用.因为MessageDigest不支持多线程
            //而且 Map<String, String> mMd5CacheMap也不支持多线程写入
            synchronized (ImageDownloadAsyncTask.class) {
                byte[] hash = null;
                try {
                    hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();//MD5算法肯定支持,此异常不可能发生
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();//utf-8编码肯定支持,此异常不可能发生
                }

                //以下计算代码摘自网上,不明觉厉
                StringBuilder hex = new StringBuilder(hash.length * 2);
                for (byte b : hash) {
                    if ((b & 0xFF) < 0x10) hex.append("0");
                    hex.append(Integer.toHexString(b & 0xFF));
                }

                md5String = hex.toString();
                mMd5CacheMap.put(string, md5String);//缓存计算值
                return md5String;
            }
        }
    }

    /** 单例模式获取HttpClient实例 */
    private HttpClient getHttpClient() {

        if (httpClient == null) {//经典的双重检查锁定写法,需要配合httpClient的volatile声明
            synchronized (ImageDownloadAsyncTask.class) {
                if (httpClient == null) {

                    // http://blog.csdn.net/suiyuansanren/article/details/8663824
                    final HttpParams httpParams = new BasicHttpParams();
                    ConnManagerParams.setTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
                    HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
                    HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
                    HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
                    ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
                    ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));
                    HttpProtocolParams.setUseExpectContinue(httpParams, true);
                    HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
                    HttpClientParams.setRedirecting(httpParams, false);
                    HttpConnectionParams.setTcpNoDelay(httpParams, true);
                    HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
                    HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

                    String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
                    HttpProtocolParams.setUserAgent(httpParams, userAgent);

                    SchemeRegistry schemeRegistry = new SchemeRegistry();
                    schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                    schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

                    ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
                    httpClient = new DefaultHttpClient(manager, httpParams);
                }
            }
        }

        return httpClient;
    }
}
