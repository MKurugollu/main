package com.example.mustafakurugollumasters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import java.util.Random;


import java.util.ArrayList;

public class ActivityGP_1_SignUpFinal4 extends AppCompatActivity {

    private String username;
    private ArrayList<Integer> selectedImages;
    private TypedArray imgs;
    private ArrayList<Integer> selectedPass;

    private GridView gridViewGP1_5;
    private EditText coordsEnter;
    private int coords;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_1__sign_up_final4);
        selectedPass = new ArrayList<Integer>();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
            selectedImages = extras.getIntegerArrayList("selectedImages");
            selectedPass = extras.getIntegerArrayList("selectedPass");
        }

        imgs = getResources().obtainTypedArray(R.array.images);
        ArrayList<Integer> prePasswordImages = getRandomImgsArray();

        gridViewGP1_5 = findViewById(R.id.gridViewGP1_5);
        CustomAdapter customAdapter = new CustomAdapter(prePasswordImages, this);
        gridViewGP1_5.setAdapter(customAdapter);

        gridViewGP1_5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPass.add(prePasswordImages.get(position));
                openActivityGP_1_SignUpFinal5();
            }
        });
        coordsEnter = (EditText) findViewById(R.id.coords4);
        coordsEnter.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    coords = Integer.parseInt(coordsEnter.getText().toString());
                    int digits = (int)(Math.log10(coords)+1);
                    if (digits == 2){
                        int col = Character.getNumericValue(coordsEnter.getText().toString().charAt(0));
                        int row = Character.getNumericValue(coordsEnter.getText().toString().charAt(1));
                        int coordToIndex = ((row - 1)*4 + col)-1;
                        selectedPass.add(prePasswordImages.get(coordToIndex));

                        openActivityGP_1_SignUpFinal5();
                    }
                    else{
                        selectedPass.add(-1);
                        openActivityGP_1_SignUpFinal5();
                    }
                    return true;
                }
                return false;
            }
        });


    }

    public class CustomAdapter extends BaseAdapter {
        private ArrayList<Integer> imagesPhoto;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(ArrayList<Integer> imagesPhoto, Context context) {
            this.imagesPhoto = imagesPhoto;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagesPhoto.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = layoutInflater.inflate(R.layout.row_items_password, viewGroup, false);

            }

            ImageView imageView = view.findViewById(R.id.imageViewPassword);

            int imgID = imagesPhoto.get(i);
            imageView.setImageResource(imgs.getResourceId(imgID, 0));


            return view;
        }
    }

    public ArrayList<Integer> getRandomImgsArray(){
        ArrayList<Integer> prePasswordImages = new ArrayList<Integer>();
        while (prePasswordImages.size() < 28){
            final int min = 0;
            final int max = 27;
            final int randomIndex = new Random().nextInt((max - min) + 1) + min;
            if (!prePasswordImages.contains(randomIndex)){
                prePasswordImages.add(randomIndex);
            }
        }
        return prePasswordImages;
    }

    public void openActivityGP_1_SignUpFinal5() {
        if(selectedImages.equals(selectedPass)){
            Intent intent = new Intent(this, ActivityGP_1_SignUpFinalSuccess.class);
            Bundle extras = new Bundle();
            extras.putString("username", username);
            extras.putIntegerArrayList("selectedImages", selectedImages);
            extras.putIntegerArrayList("selectedPass", selectedPass);
            intent.putExtras(extras);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ActivityGP_1_SignUpFinalFailure.class);
            Bundle extras = new Bundle();
            extras.putString("username", username);
            extras.putIntegerArrayList("selectedImages", selectedImages);
            extras.putIntegerArrayList("selectedPass", selectedPass);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}