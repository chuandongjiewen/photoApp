package com.example.photoapp;

import java.security.Principal;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView uploadText;
	private ImageView praiseImage;
	private ImageView visitImage;
	private TextView praiseNumView;
	private TextView visitNumView;
	
	private ScaleAnimation scaleAnim;
	
	private int praiseNum = 0;
	private int visitNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		bindEvent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void initView(){
		uploadText = (TextView)findViewById(R.id.button_upload);
		praiseImage = (ImageView)findViewById(R.id.button_praise);
		visitImage = (ImageView)findViewById(R.id.button_visit);
		praiseNumView = (TextView)findViewById(R.id.text_praise_num);
		visitNumView = (TextView)findViewById(R.id.text_visit_num);
	}
	
	private void initData(){
		praiseNum = 1000;
		visitNum = 1000;
		praiseNumView.setText(String.valueOf(praiseNum));
		visitNumView.setText(String.valueOf(visitNum));
		scaleAnim = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnim.setDuration(500);
		
	}
	
	
	private void bindEvent(){
		
		uploadText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		praiseImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				praiseNum ++;
				praiseNumView.setText(String.valueOf(praiseNum));
				praiseImage.clearAnimation();
				praiseImage.setAnimation(scaleAnim);
				scaleAnim.startNow();

			}
		});
		
		visitImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				visitNum ++;
				visitNumView.setText(String.valueOf(visitNum));
				visitImage.clearAnimation();
				visitImage.setAnimation(scaleAnim);
				scaleAnim.startNow();
			}
		});
		
		
		
		
		
	}

}
