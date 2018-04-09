package com.zhufu.znotify;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.zhufu.library.Channel;
import com.zhufu.library.ZNotify;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt)
    Button bt;

    @BindView(R.id.bt1)
    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt)
    void btOnClick() {
        PendingIntent pendingIntent = getDefalutIntent(Intent.FLAG_ACTIVITY_NEW_TASK);
        ZNotify zNotify = new ZNotify(this);
        zNotify.setChannel(new Channel("1", "channel1"));
        zNotify.setTicker("你收到一条消息");
        zNotify.setTitle("通知标题");
        zNotify.setContent("通知内容");
        zNotify.setPIntent(pendingIntent);
        zNotify.setFullScreenIntent(pendingIntent);
        zNotify.setLargeIcon(R.mipmap.ic_launcher);
        zNotify.setSmallIcon(R.mipmap.ic_launcher);
        zNotify.setNOTIFICATION_ID(1);
        zNotify.setContentInfo("内容详情");
        zNotify.build();
        zNotify.send();
    }

    @OnClick(R.id.bt1)
    void bt1OnClick() {
        ZNotify zNotify = new ZNotify(this);
        zNotify.cancel(1);
    }

    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }
}
