package com.henrique.simpleflashlight;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	ImageButton btnSwitch;
	 
    private Camera camera;
    private boolean isFlashOn;
    Parameters params;
    MediaPlayer mp; 

 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
        
        getCamera();
        
        btnSwitch.setOnClickListener(new View.OnClickListener() {     
                	
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });
	}
	
	private void getCamera(){
		if(camera == null) {
			try{
				camera = Camera.open();
				params = camera.getParameters();
		} catch(RuntimeException e) {
			Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
		}
			
		}
	}
	
	private void toggleButtonImage(){
		if(isFlashOn){
			btnSwitch.setImageResource(R.drawable.on);
		} else{
			btnSwitch.setImageResource(R.drawable.off);
		}
	}
	
	private void playSound(){
		 mp = MediaPlayer.create(MainActivity.this, R.raw.switch_sound);
		 
		 mp.setOnCompletionListener(new OnCompletionListener() {
			 
		        @Override
		        public void onCompletion(MediaPlayer mp) {
	            mp.release();
		        }
		    }); 
		    mp.start();
	}	
	
	private void turnOnFlash() {
		if(!isFlashOn){
			if(camera == null || params == null) return;
		}
		
		playSound();
		
		params = camera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(params);
		camera.startPreview();
		isFlashOn = true;
		
		toggleButtonImage();
	}
	
	private void turnOffFlash() {
		if(isFlashOn){
			if(camera == null || params == null) return;			
		}
		
		playSound();
		
		params = camera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(params);
		camera.startPreview();
		isFlashOn = false;
		
		toggleButtonImage();
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	}
	 
	@Override
	protected void onPause() {
	    super.onPause();
	     
	    turnOffFlash();
	}
	 
	@Override
	protected void onRestart() {
	    super.onRestart();
	}
	 
	@Override
	protected void onResume() {
	    super.onResume();
	     
	    turnOnFlash();
	}
	 
	@Override
	protected void onStart() {
	    super.onStart();
	     
	    getCamera();
	}
	 
	@Override
	protected void onStop() {
	    super.onStop();
	     
	    if (camera != null) {
	        camera.release();
	        camera = null;
	    }
	}
}
	
