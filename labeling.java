package com.example.aaron.searchwhy;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import java.util.Stack;

public class labeling{
	Bitmap i_image;
	Bitmap j_image;
	Bitmap sub_image;
	int index = 1;
	int spotArea = 0;

	public labeling(Bitmap a, Bitmap b){
		this.i_image = a;
		this.j_image = b;
	}
	public Bitmap labelingDo(){

		 
		 Stack<Point> labelstack = new Stack();
		 Point point;


		int width = i_image.getWidth();
		int height = i_image.getHeight();
		 
		 Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		 sub_image = Bitmap.createBitmap(width, height, conf);
		int areaSize = 0;

		for (int i=0; i < width; i++){
			 for (int j=0; j < height; j++){
				 int i_color = i_image.getPixel(i, j);
				 int j_color = j_image.getPixel(i, j);
				 int i_rgbRed = Color.red(i_color);
				 int i_rgbBlue = Color.blue(i_color);
				 int i_rgbGreen = Color.green(i_color);
				 int j_rgbRed = Color.red(j_color);
				 int j_rgbBlue = Color.blue(j_color);
				 int j_rgbGreen = Color.green(j_color);
						 
				 int newrgbRed = i_rgbRed - j_rgbRed;
				 int newrgbBlue = i_rgbBlue - j_rgbBlue;
				 int newrgbGreen = i_rgbGreen - j_rgbGreen;
				 
				 if (newrgbRed < 0){
					 newrgbRed *= -1;
				 }
				 if (newrgbBlue < 0){
					 newrgbBlue *= -1;
				 }
				 if (newrgbGreen < 0){
					 newrgbGreen *= -1;
				 }
				 
				 int newColor = Color.rgb(newrgbRed, newrgbBlue, newrgbGreen);
				 sub_image.setPixel(i, j, newColor);
			 }
		 }
		 
		 for (int i = 0; i < width; i++) {
			 for (int j = 0; j < height; j++) {
				 int color = sub_image.getPixel(i, j);
				 if (Color.red(color) != 0 && (Color.blue(color) != 0 || Color.green(color) != 0)) {
					 labelstack.push(new Point(i, j));
					 sub_image.setPixel(i, j, Color.rgb(0, index, Color.green(color)));
					 spotArea++;
					 areaSize = 0;
					 areaSize++;
					 while (!labelstack.empty()) {
						 point = labelstack.pop();
						 if (point.x > 0) {
							 color = sub_image.getPixel(point.x - 1, point.y);
							 if (Color.red(color) != 0 && (Color.green(color) != 0 || Color.blue(color) != 0)) {
								 labelstack.push(new Point(point.x - 1, point.y));
								 sub_image.setPixel(point.x - 1, point.y, Color.rgb(0, index, Color.green(color)));
								 spotArea++;
								 areaSize++;
							 }
							 if (point.y > 0) {
							 	color = sub_image.getPixel(point.x - 1, point.y - 1);
							 	if (Color.red(color) != 0 && (Color.green(color) != 0 || Color.blue(color) != 0)) {
								 	labelstack.push(new Point(point.x - 1, point.y - 1));
								 	sub_image.setPixel(point.x - 1, point.y - 1, Color.rgb(0, index, Color.blue(color)));
								 	spotArea++;
								 	areaSize++;
							 	}
							 }
						 }

						 if (point.x + 1 < width) {
							 color = sub_image.getPixel(point.x + 1, point.y);
							 if (Color.red(color) != 0 && (Color.green(color) != 0 || Color.blue(color) != 0)) {
								 labelstack.push(new Point(point.x + 1, point.y));
								 sub_image.setPixel(point.x + 1, point.y, Color.rgb(0, index, Color.blue(color)));
								 spotArea++;
								 areaSize++;
							 }

							 if (point.y > 0) {
								 color = sub_image.getPixel(point.x + 1, point.y - 1);
								 if (Color.red(color) != 0 && (Color.green(color) != 0 || Color.blue(color) != 0)) {
									 labelstack.push(new Point(point.x + 1, point.y - 1));
									 sub_image.setPixel(point.x + 1, point.y - 1, Color.rgb(0, index, Color.blue(color)));
									 spotArea++;
									 areaSize++;
								 }
							 }
							 if (point.y + 1 < height) {
								 color = sub_image.getPixel(point.x + 1, point.y + 1);
								 if (Color.red(color) != 0 && (Color.green(color) != 0 || Color.blue(color) != 0)) {
									 labelstack.push(new Point(point.x + 1, point.y + 1));
									 sub_image.setPixel(point.x + 1, point.y + 1, Color.rgb(0, index, Color.blue(color)));
									 spotArea++;
									 areaSize++;
								 }
							 }
						 }

						 if (point.y > 0) {
							 color = sub_image.getPixel(point.x, point.y - 1);
							 if (Color.red(color) != 0 && (Color.green(color) != 0 || Color.blue(color) != 0)) {
								 labelstack.push(new Point(point.x, point.y - 1));
								 sub_image.setPixel(point.x, point.y - 1, Color.rgb(0, index, Color.green(color)));
								 spotArea++;
								 areaSize++;
							 }
						 }

						 if (point.y + 1 < height) {
							 color = sub_image.getPixel(point.x, point.y + 1);
							 if (Color.red(color) != 0 && (Color.green(color) != 0 || Color.blue(color) != 0)) {
								 labelstack.push(new Point(point.x, point.y + 1));
								 sub_image.setPixel(point.x, point.y + 1, Color.rgb(0, index, Color.green(color)));
								 spotArea++;
								 areaSize++;
							 }
							 if (point.x > 0) {
								 color = sub_image.getPixel(point.x - 1, point.y + 1);
								 if (Color.red(color) != 0 && (Color.green(color) != 0 || Color.blue(color) != 0)) {
									 labelstack.push(new Point(point.x - 1, point.y + 1));
									 sub_image.setPixel(point.x - 1, point.y + 1, Color.rgb(0, index, Color.blue(color)));
									 spotArea++;
									 areaSize++;
								 }
							 }
						 }
						 if (spotArea > width*height/10){
							return sub_image;
						 }
					 }
					 if (areaSize > 30) {
						 index++;
					 }
				 }

			 }
		 }

		return sub_image;
	}
	public int getIndex(){
		return this.index;
	}
	public int getSpotArea(){
		return this.spotArea;
	}
}


