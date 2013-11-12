package jp.xdomain.katsura131.camera;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends Activity {
	private MainActivity self = this;
	private ImageView iv;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        RelativeLayout l = new RelativeLayout(this);
        //l.setGravity(Gravity.BOTTOM);
        //l.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        setContentView(l);

        //preview
        PhotoView pv = new PhotoView(this,self);
        pv.setId(0);
        RelativeLayout.LayoutParams param_pv = new RelativeLayout.LayoutParams(WC, WC);
        param_pv.addRule(RelativeLayout.CENTER_IN_PARENT); 
        l.addView(pv,param_pv);

        //watermark
        View wm = getLayoutInflater().inflate(R.drawable.imageview, null);
        wm.setId(1);
        RelativeLayout.LayoutParams param_wm = new RelativeLayout.LayoutParams(WC, WC);
        param_wm.addRule(RelativeLayout.CENTER_IN_PARENT, 0); 
        l.addView(wm,param_wm);

        //shutter button
        RelativeLayout.LayoutParams param_shutter = new RelativeLayout.LayoutParams(WC, WC);
        param_shutter.addRule(RelativeLayout.ALIGN_LEFT, 0); 
        param_shutter.addRule(RelativeLayout.CENTER_VERTICAL); 
        l.addView(getLayoutInflater().inflate(R.layout.shutter,null),param_shutter);
        ImageButton shutter = (ImageButton)findViewById(R.id.shutter);
        shutter.setOnClickListener(pv.getShutterClickListener());
        pv.setShutter(shutter);
	}

	public void printWatermark(float preW, float picW){
    	String path = Environment.getExternalStorageDirectory().getPath() + "/camera_test.jpg";        
    	iv = (ImageView) findViewById(1);
        iv.setImageAlpha(128);
        iv.setImageBitmap(BitmapFactory.decodeFile(path));
        iv.setScaleX(picW / preW);
        iv.setScaleY(picW / preW);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
