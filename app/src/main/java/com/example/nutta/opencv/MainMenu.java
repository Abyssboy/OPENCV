package com.example.nutta.opencv;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

public class MainMenu extends AppCompatActivity {
    ImageView mImageView;
    Integer Request_CAMERA = 1 ,Select_file=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup);
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mImageView=(ImageView) findViewById(R.id.ImageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
    }

    private void SelectImage() {

        final CharSequence[] item = {"Camera", "Gallery", "Cancle"};
        AlertDialog.Builder Builder =  new AlertDialog.Builder(MainMenu.this);
        Builder.setTitle("Add Image");
        Builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(item[i].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,Request_CAMERA);


                }else if(item[i].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Select File"),Select_file);

                }else if(item[i].equals("Cancle")){
                    dialog.dismiss();
                }

            }
        });
        Builder.show();

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main,menu);
            return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && requestCode==Request_CAMERA){
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            mImageView.setImageBitmap(bitmap);
        }else if(resultCode== Activity.RESULT_OK && requestCode==Select_file){
           /* Uri selectedImageUri = data.getData();
            mImageView.setImageURI(selectedImageUri);*/
            Uri selectedImageUri = data.getData();
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("selectedImageUri",selectedImageUri.toString());
            startActivity(intent);
            finish();
        }
    }
}
