package com.example.mustafakurugollumasters;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ActivityGP_2_SignUpFinal3 extends AppCompatActivity {

    private String username;
    private FrameLayout FL;
    private TypedArray imgs;
    private List<List<Integer>> points;
    private Polygon.Point p;
    private Polygon.Point[] polygon;
    private ArrayList<Polygon.Point[]> allPolygons;
    private int n;
    private boolean isInside;
    private ArrayList<Integer> presses;
    private ArrayList<Integer> selectedImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_2__log_in_final1);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
            selectedImages = extras.getIntegerArrayList("selectedImages");
            presses = extras.getIntegerArrayList("presses");
        }
        allPolygons = new ArrayList<Polygon.Point[]>();
        points = new ArrayList<List<Integer>>();
        imgs = getResources().obtainTypedArray(R.array.images);
        isInside = false;
        presses = new ArrayList<Integer>();

        int screenWidth =  Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;

        int pi = getRandomNumOfPassIcons();

        for (int i = 0; i<pi; i++){
            addImageRandomly(screenWidth, screenHeight, selectedImages.get(i));
        }

        if(pi > 3){
            int[][] pointsArray = new int[points.size()][];
            for(int i = 0; i < points.size(); i++){
                pointsArray[i] = new int[points.get(i).size()];
            }
            for(int i=0; i<points.size(); i++) {
                for (int j = 0; j < points.get(i).size(); j++) {
                    pointsArray[i][j] = points.get(i).get(j);
                }
            }
            Combination.printCombination(pointsArray, pi, 3);

            for(int i=0, j=Combination.combPolygons.size(); i<j; i+=3){
                polygon = new Polygon.Point[3];
                /*int idx = 0;
                for(int[] ii : Combination.combPolygons){

                    idx++;
                }*/
                polygon[0] = new Polygon.Point(Combination.combPolygons.get(i)[0], Combination.combPolygons.get(i)[1]);
                polygon[1] = new Polygon.Point(Combination.combPolygons.get(i+1)[0], Combination.combPolygons.get(i+1)[1]);
                polygon[2] = new Polygon.Point(Combination.combPolygons.get(i+2)[0], Combination.combPolygons.get(i+2)[1]);

                allPolygons.add(polygon);
            }

        }
        else{
            polygon = new Polygon.Point[points.size()];
            int idx = 0;
            for(List<Integer> ii : points){
                polygon[idx] = new Polygon.Point(ii.get(0), ii.get(1)) ;
                idx++;
                //Log.i("point", "" + ii.get(0) + "," + ii.get(1));
            }
            allPolygons.add(polygon);

        }

        FL = (FrameLayout) findViewById(R.id.gp2_fl_1);
        FL.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.i("TAG", "touch down: (" + x + ", " + y + ")");
                        p = new Polygon.Point(x, y);

                        for(Polygon.Point[] poly: allPolygons){
                            n = poly.length;
                            if (Polygon.isInside(poly, n, p))
                            {
                                Log.v("isInside?", "yes");
                                isInside = true;
                                break;
                            }
                        }

                        openActivityGP_2_LogInFinal();

                        break;



                    case MotionEvent.ACTION_UP:
                        break;
                }

                return false;
            }
        });

    }

    @SuppressLint("NewApi")
    private void addImageRandomly(int screenW, int screenH, int img){

        int w = getRandomW(screenW);
        int h = getRandomH(screenH);
        List<Integer> point = new ArrayList<Integer>();
        point.add(w+50);
        point.add(h+50);
        points.add(point);

        FL = (FrameLayout) findViewById(R.id.gp2_fl_1);
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageResource(imgs.getResourceId(img, 0));
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param.leftMargin = w;
        param.topMargin = h;
        param.height = 100;
        param.width = 100;
        iv.setLayoutParams(param);

        FL.addView(iv);

    }

    private int getRandomW(int screenW){
        final int min = 0;
        final int max = screenW-200;
        final int random = new Random().nextInt((max-min)+1 ) + min;
        return random;
    }

    private int getRandomH(int screenH){
        final int min = 30;
        final int max = screenH-200;
        final int random = new Random().nextInt((max-min)+1 ) + min;
        return random;
    }

    private int getRandomNumOfPassIcons(){
        final int min = 3;
        final int max = 5;
        final int random = new Random().nextInt((max-min)+1 ) + min;
        return random;
    }

    public void openActivityGP_2_LogInFinal() {
        if(isInside){
            presses.add(1);
        }
        else
            presses.add(0);

        Intent intent;
        Bundle extras = new Bundle();
        extras.putString("username", username);
        extras.putIntegerArrayList("selectedImages", selectedImages);

        if(presses.contains(0))
            intent = new Intent(this, ActivityGP_2_SignUpFinalFailure.class);
        else
            intent = new Intent(this, ActivityGP_2_SignUpFinalSuccess.class);

        intent.putExtras(extras);
        startActivity(intent);
    }
}