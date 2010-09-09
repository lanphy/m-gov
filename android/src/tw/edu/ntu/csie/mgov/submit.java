package tw.edu.ntu.csie.mgov;

import java.io.File;
import java.util.zip.Inflater;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class submit extends MapActivity {

	ImageView myImage;
	final int select_click_pic = 1;
	String pic_dialog_option = "";
	TextView take_pic, select_pic;
	private maplocationviewer mpviewLayout;
	private MapView mapview;
	private EditText submit_name,submit_desc;
	Button submit_type_btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit);
		
		findview();
		
		
		Bitmap myBitmap = BitmapFactory.decodeFile("/sdcard/myImage.jpg");
		myImage.setImageBitmap(myBitmap);
		
		myImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(select_click_pic);
			}
		});
		
		submit_type_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(submit.this, NoPicSubmit.class);
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case select_click_pic:
			return submitDialog();
		
		}
		return super.onCreateDialog(id);
	}
	
	Dialog submitDialog(){
		
		LayoutInflater inflater = LayoutInflater.from(this);
		final View textEntryView = inflater.inflate(R.layout.picture_dialog,null);
		
		take_pic = (TextView)textEntryView.findViewById(R.id.take_pic);
		select_pic = (TextView)textEntryView.findViewById(R.id.select_pic);
		
		take_pic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				take_pic.setTextColor(Color.GREEN);
				select_pic.setTextColor(Color.WHITE);
				pic_dialog_option = "take";
			}
		});
		
		select_pic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				select_pic.setTextColor(Color.GREEN);
				take_pic.setTextColor(Color.WHITE);
				pic_dialog_option = "select";
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon);
		builder.setTitle("Select or Take a Picture");
		builder.setView(textEntryView);
		
		builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						if(pic_dialog_option.equals("take"))
						{
							Intent intent = new Intent();
							intent.setClass(submit.this, CameraActivity.class);
							startActivity(intent);
						}
						else
						{
//							Intent intent = new Intent();
//							intent.setClass(submit.this, CameraActivity.class);
//							startActivity(intent);
						}
					}
				});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				take_pic.setTextColor(Color.WHITE);
				select_pic.setTextColor(Color.WHITE);
			}
		});
		
		return builder.create();
	}
	
	void findview(){
		myImage = (ImageView) findViewById(R.id.pic);		
		mpviewLayout = (maplocationviewer) findViewById(R.id.mapview);
		mpviewLayout.setcontext(submit.this);
		mapview = mpviewLayout.getMapView();
		submit_name = (EditText)findViewById(R.id.submit_name);
		submit_desc = (EditText)findViewById(R.id.submit_suggestion);
		submit_type_btn = (Button)findViewById(R.id.submit_type_btn);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}