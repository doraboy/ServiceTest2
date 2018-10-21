package tw.dora.servicetest2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mesg;
    private MyService myService;
    private boolean isBind;
    private UIHandler uiHandler;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            MyService.LocalBinder binder = (MyService.LocalBinder)iBinder;
            myService = binder.getService();
            myService.setUiHandler(uiHandler);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mesg = findViewById(R.id.mesg);
        uiHandler = new UIHandler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,MyService.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isBind){
            unbindService(serviceConnection);
        }

    }

    public void test1(View view) {
        if(isBind){
            myService.m1("ok");
        }
    }

    public void test2(View view) {
        myService.m2();
    }

    public class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int rand1 = msg.getData().getInt("brad1");
            int rand2 = msg.getData().getInt("brad2");
            String text = msg.getData().getString("brad3");

            mesg.setText(text+":"+rand1+","+rand2);

        }
    }
}
