package jp.xdomain.katsura131.camera;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.widget.Toast;

public class PhotoView extends SurfaceView implements Callback, PictureCallback{
	private Camera camera;
	private Size psize;
	private float preW;
	private float preH;
	private float picW;
	private float picH;
	private List<Size> previewSizes;
	private ImageButton shutter = null;
	private Toast newestText = null;
	
	MainActivity activity;
	public String path;
	
	public PhotoView(Context context, MainActivity activity){
		super(context);
		this.activity = activity;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void setShutter(ImageButton shutter){
		this.shutter = shutter;
	}
	
	private Camera.PictureCallback self = this;
	View.OnClickListener getShutterClickListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shutter.setEnabled(false);
				camera.takePicture(null,null,self);
			}
		};
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera = Camera.open();
			camera.setPreviewDisplay(holder);
		} catch(IOException e) {
		}

		List<Size> pictureSizes = camera.getParameters().getSupportedPictureSizes();
	    picW = pictureSizes.get(0).width;
	    picH = pictureSizes.get(0).height;

	    previewSizes = camera.getParameters().getSupportedPreviewSizes();
	    psize = previewSizes.get(0);
        //Toast.makeText(getContext(), "a:size:"+psize.width+"/"+psize.height, Toast.LENGTH_LONG).show();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		Camera.Parameters p = camera.getParameters();
		android.view.ViewGroup.LayoutParams lp = getLayoutParams();
		int ch = p.getPreviewSize().height;
		int cw = p.getPreviewSize().width;
		if(ch/cw > psize.height/psize.width){
			lp.width = psize.width;
			lp.height = psize.width * ch/cw;
		}else{
			lp.width = psize.height * cw/ch;
			lp.height = psize.height;
		}
        //Toast.makeText(getContext(), "b:size:"+lp.width+"/"+lp.height, Toast.LENGTH_LONG).show();
		camera.setParameters(p);
		camera.startPreview();

		setLayoutParams(lp);
        preW = lp.width;
        preH = lp.height;
        activity.printWatermark(preW,picW);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		
        if (data != null) {
            FileOutputStream myFOS = null;
            try {
            	String path = Environment.getExternalStorageDirectory().getPath() + "/camera_test.jpg";
                myFOS = new FileOutputStream(path);
                myFOS.write(data);
                myFOS.close();
                if(newestText != null){
                	newestText.cancel();
                }
                newestText = Toast.makeText(getContext(), path + " Ç…ï€ë∂ÇµÇ‹ÇµÇΩ", Toast.LENGTH_LONG);
    			newestText.show();
            } catch (Exception e) {
                e.printStackTrace();
                if(newestText != null){
                    newestText.cancel();
                }
                newestText = Toast.makeText(getContext(), "ó·äOÇ™î≠ê∂ÇµÇ‹ÇµÇΩ", Toast.LENGTH_LONG);
    		    newestText.show();
            }
            camera.startPreview();
        }
        activity.printWatermark(preW,picW);
		shutter.setEnabled(true);
	}
	

}