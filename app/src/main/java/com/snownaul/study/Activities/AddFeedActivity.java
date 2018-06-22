package com.snownaul.study.Activities;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.snownaul.study.G;
import com.snownaul.study.Manifest;
import com.snownaul.study.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.GONE;

public class AddFeedActivity extends AppCompatActivity {

    ToggleButton tgCheud;
    LinearLayout pictures;
    ImageView iv;
    EditText etContents;
    boolean isSubmitable=true;


    String imgPath;



    static final int EXTERNAL_STORAGE_PERMISSION=100;
    static final int CAMERA_PERMISSION=110;
    static final int CAMERA_IMAGE=10;
    static final int PHOTO_IMAGE=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tgCheud=findViewById(R.id.tg_cheud);
        pictures=findViewById(R.id.pictures);
        iv=findViewById(R.id.iv);
        etContents=findViewById(R.id.et_contents);

        //외부 저장소 읽고쓰기 권한 퍼미션 체크 및 요청
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                //허용이 안되어 있는 상태이므로 퍼미션 다이얼로그 보이기
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_STORAGE_PERMISSION);

            }
            if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},CAMERA_PERMISSION);
            }
        }







        tgCheud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pictures.setVisibility(GONE);
                }else{
                    pictures.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addset_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.submit:
                if(isSubmitable)
                    uploadNewFeed();
                break;
        }




        return super.onOptionsItemSelected(item);
    }

    public void clickCamera(View v){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if(intent.resolveActivity(getPackageManager())!=null)
            startActivityForResult(intent,CAMERA_IMAGE);


    }

    public void clickPhoto(View v){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case CAMERA_IMAGE:
                if(resultCode==RESULT_OK){
                    Uri uri=data.getData();

                    if(uri!=null){
                        Log.i("MyTag","사진 업로드 uri로 나왔습니다. : "+uri.toString());
                        //Toast.makeText(this, uri.toString(),Toast.LENGTH_SHORT).show();

                        Glide.with(this).load(uri).into(iv);
                        imgPath=getRealPathFromUri(uri);
                    }else{
                        Bitmap bm=(Bitmap)data.getExtras().get("data");
                        Log.i("MyTag","비트맵으로 나왔습니다.");
                        if(bm!=null){
                            //Glide.with(this).load(bm).into(iv);
                            Uri uriFromBm=saveBitmapAndGetUri(bm);

                            if(uriFromBm!=null) {
                                Log.i("MyTag","bitmap 저장하고 uri로 바꿨습니다 : "+uriFromBm.toString());
                                Glide.with(this).load(uriFromBm).into(iv);
                                //Toast.makeText(this, uri.toString() + "", Toast.LENGTH_SHORT).show();
                                imgPath = uriFromBm.getPath();
                            }else{
                                Log.i("MyTag","널입니다 ㅠㅠㅠ 왜죠?");

                            }
                            //Toast.makeText(this, "uri는 널이다..", Toast.LENGTH_SHORT).show();

                            
                        }
                    }
                }

                break;
            case PHOTO_IMAGE:
                if(resultCode==RESULT_OK){
                    Uri uri=data.getData();

                    if(uri!=null){
                        //Toast.makeText(this, uri.toString(),Toast.LENGTH_SHORT).show();
                        Glide.with(this).load(uri).into(iv);
                        imgPath=getRealPathFromUri(uri);
                    }
                }
                break;
            case EXTERNAL_STORAGE_PERMISSION:

                 break;
            case CAMERA_PERMISSION:

                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    String getRealPathFromUri(Uri uri){
        String[] proj={MediaStore.Images.Media.DATA};
        CursorLoader loader=new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor=loader.loadInBackground();
        int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();

        Log.i("MyTag","REAL PATH : "+result);
        return result;

    }

    Uri saveBitmapAndGetUri(Bitmap bitmap){
        File path= Environment.getExternalStorageDirectory();

        String time=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file=new File(path,time+".jpg");

        Uri uri=null;

        try{
            FileOutputStream fos=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);

            fos.flush();
            fos.close();

            Log.i("MyTag","빝맵파일 저장 완료!!");
            Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            uri=Uri.parse(file.getPath());
            intent.setData(uri);

            sendBroadcast(intent);

            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void uploadNewFeed(){
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/uploadNewFeed.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MyTag","upload New Feed : "+response);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag","upload New Feed ERROR : "+error.getMessage());
                finish();

            }
        });

        multiPartRequest.addStringParam("userID", G.getUserId()+"");
        multiPartRequest.addStringParam("contents",etContents.getText().toString()+"");

        if(imgPath!=null)
            multiPartRequest.addFile("imgPath",imgPath);

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);
        isSubmitable=false;

    }

}
