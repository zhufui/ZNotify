package com.zhufu.library;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import java.util.List;

/**
 * author  : zhufu
 * email   : zhufui@sina.com
 * time    : 2018/04/01
 * desc    : 通知的工具类
 * version : 1.0
 */
public class ZNotify {

    private Context mContext;
    private NotificationManager mNm;
    private int NOTIFICATION_ID;
    private NotificationCompat.Builder mCBuilder;
    private PendingIntent mPIntent;
    private PendingIntent mFullScreenIntent;
    //顶部状态栏的小图标
    private int smallIcon = -1;
    //设置大图标
    private int largeIcon = -1;
    //在顶部状态栏中的提示信息
    private String ticker;
    //通知栏标题
    private String title;
    //通知栏内容
    private String content;
    //显示在通知栏时间下面的内容
    private String contentInfo;
    //是否有声音
    private boolean defaultSound = false;
    //是否振动
    private boolean defaultVibrate = false;
    //是否闪光
    private boolean defaultLights = false;
    private boolean autoCancel = true;
    //设置通知的优先级
    //从Android4.1开始，可以通过以下方法，设置notification的优先级
    //优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
    private int priority = NotificationCompat.PRIORITY_DEFAULT;
    private boolean ongoing = false;
    //振动的频率
    private long[] pattern;
    private Uri uri;
    private Lights lights;
    private Progress progress;
    private int visibility = NotificationCompat.VISIBILITY_PUBLIC;
    private Channel mChannel;

    public ZNotify(Context context) {
        mContext = context;
        // 获取系统服务来初始化对象
        mNm = (NotificationManager) mContext.getApplicationContext()
                .getSystemService(Activity.NOTIFICATION_SERVICE);
        mCBuilder = new NotificationCompat.Builder(mContext);
    }

    public void build() {
        setChannel();

        if (mPIntent != null) {
            mCBuilder.setContentIntent(mPIntent);
        }

        setIcon();

        if (!TextUtils.isEmpty(ticker)) {
            mCBuilder.setTicker(ticker);
        }

        if (!TextUtils.isEmpty(title)) {
            mCBuilder.setContentTitle(title);
        }

        if (!TextUtils.isEmpty(content)) {
            mCBuilder.setContentText(content);
        }

        if (!TextUtils.isEmpty(contentInfo)) {
            mCBuilder.setContentInfo(contentInfo);
        }

        //设置显示的时间
        mCBuilder.setWhen(System.currentTimeMillis());

        //将AutoCancel设为true后，当你点击通知栏的notification后，
        //它会自动被取消消失,不设置的话点击消息后也不清除，但可以滑动删除
        mCBuilder.setAutoCancel(autoCancel);

        //设置通知的优先级
        mCBuilder.setPriority(priority);

        setSoundVibrateLights();

        //如果为true,通知不能被删除,一般下载文件或者音乐播放器时使用,设置为true后只能通过代码删除
        mCBuilder.setOngoing(ongoing);

        if (progress != null) {
            mCBuilder.setProgress(progress.getMax(), progress.getProgress(), progress.isIndeterminate());
        }

        //Android 5.0(API level 21)开始，通知可以显示在锁屏上。用户可以通过设置选择是否允许敏感的通知内容显示在安全的锁屏上
        //VISIBILITY_PRIVATE : 显示基本信息，如通知的图标，但隐藏通知的全部内容
        //VISIBILITY_PUBLIC : 显示通知的全部内容
        //VISIBILITY_SECRET : 不显示任何内容，包括图标
        mCBuilder.setVisibility(visibility);

        mCBuilder.setChannelId(mChannel.getChannelId());

        //悬浮通知,Android5.0(API级别是21)
        if (mFullScreenIntent != null) {
            mCBuilder.setFullScreenIntent(mPIntent, true);
        }
    }

    /**
     * 设置振动，声音，三色灯
     */
    private void setSoundVibrateLights() {
        //Notification.DEFAULT_VIBRATE //添加默认震动提醒 需要 VIBRATE permission
        //Notification.DEFAULT_SOUND // 添加默认声音提醒
        //Notification.DEFAULT_LIGHTS// 添加默认三色灯提醒
        //Notification.DEFAULT_ALL// 添加默认以上3种全部提醒
        int defaults = 0;
        if (defaultSound) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        if (defaultVibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (defaultLights) {
            defaults |= Notification.DEFAULT_LIGHTS;
        }
        mCBuilder.setDefaults(defaults);

        //例如：pattern = new long[] {0,300,500,700};
        //延迟0ms，然后振动300ms，在延迟500ms，接着在振动700ms
        //需要权限<uses-permission android:name="android.permission.VIBRATE" />
        if (pattern != null) {
            mCBuilder.setVibrate(pattern);
        }

        //自定义铃声,Uri.parse("file:///sdcard/dance.mp3")
        //获取Android多媒体库内的铃声,Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "5")
        if (uri != null) {
            mCBuilder.setSound(uri);
        }

        //其中ARGB 表示灯光颜色、OnMS 亮持续时间、OffMS 暗的时间
        //需要权限<uses-permission android:name="android.permission.FLASHLIGHT" />
        if (lights != null) {
            mCBuilder.setLights(lights.getArgb(), lights.getOnMs(), lights.getOffMs());
        }
    }

    /**
     * 设置大小icon
     */
    private void setIcon() {
        if (largeIcon != -1) {
            mCBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), largeIcon));
        }

        //sdk大于4.4,小图标设置一个白底的图标
        if (Build.VERSION.SDK_INT > 19) {
            if (smallIcon != -1) {
                mCBuilder.setSmallIcon(smallIcon);
            }
        } else {
            mCBuilder.setSmallIcon(largeIcon);
        }
    }

    /**
     * 设置Channel
     */
    private void setChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            if (mChannel == null) {
                throw new NullPointerException("need set channelId");
            }
            NotificationChannel nChannel = new NotificationChannel(mChannel.getChannelId(),
                    mChannel.getChannelName(), NotificationManager.IMPORTANCE_DEFAULT);
            mNm.createNotificationChannel(nChannel);
        }
    }

    /**
     * 发送通知
     */
    public void send() {
        mNm.notify(NOTIFICATION_ID, mCBuilder.build());
    }

    /**
     * 根据id取消通知
     *
     * @param id 通知id
     */
    public void cancel(int id) {
        mNm.cancel(id);
    }

    /**
     * 取消所有的通知
     */
    public void cancelAll() {
        mNm.cancelAll();
    }

    /**
     *  删除通知渠道
     * @param channelId
     */
    public void deleteChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= 26) {
            mNm.deleteNotificationChannel(channelId);
        }
    }

    /**
     * 删除所有通知渠道
     */
    public void deleteAllChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            List<NotificationChannel> channelList = mNm.getNotificationChannels();
            for (int i = 0, size = channelList.size(); i < size; i++) {
                NotificationChannel channel = channelList.get(i);
                deleteChannel(channel.getId());
            }
        }
    }

    /**
     * 设置通知的id
     *
     * @param id
     */
    public void setNOTIFICATION_ID(int id) {
        NOTIFICATION_ID = id;
    }

    public int getNOTIFICATION_ID() {
        return NOTIFICATION_ID;
    }

    public PendingIntent getPIntent() {
        return mPIntent;
    }

    public void setPIntent(PendingIntent mPIntent) {
        this.mPIntent = mPIntent;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public int getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(int largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentInfo() {
        return contentInfo;
    }

    public void setContentInfo(String contentInfo) {
        this.contentInfo = contentInfo;
    }

    public boolean isDefaultSound() {
        return defaultSound;
    }

    public void setDefaultSound(boolean defaultSound) {
        this.defaultSound = defaultSound;
    }

    public boolean isDefaultVibrate() {
        return defaultVibrate;
    }

    public void setDefaultVibrate(boolean defaultVibrate) {
        this.defaultVibrate = defaultVibrate;
    }

    public boolean isDefaultLights() {
        return defaultLights;
    }

    public void setDefaultLights(boolean defaultLights) {
        this.defaultLights = defaultLights;
    }

    public boolean isAutoCancel() {
        return autoCancel;
    }

    public void setAutoCancel(boolean autoCancel) {
        this.autoCancel = autoCancel;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public long[] getPattern() {
        return pattern;
    }

    public void setPattern(long[] pattern) {
        this.pattern = pattern;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Lights getLights() {
        return lights;
    }

    public void setLights(Lights lights) {
        this.lights = lights;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Channel getChannel() {
        return mChannel;
    }

    public void setChannel(Channel channel) {
        this.mChannel = channel;
    }

    public PendingIntent getFullScreenIntent() {
        return mFullScreenIntent;
    }

    public void setFullScreenIntent(PendingIntent mFullScreenIntent) {
        this.mFullScreenIntent = mFullScreenIntent;
    }
}
