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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.StoQuestionsAdapter;
import com.snownaul.study.study_classes.Question;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

import static android.view.View.GONE;

public class StudyStorageActivity extends AppCompatActivity {

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();
    JellyRefreshLayout jelly;

    RecyclerView recyclerView;
    StoQuestionsAdapter stoQuestionsAdapter;

    TextView tvTitle, tvInfo;

    static final int EXTERNAL_STORAGE_PERMISSION=100;
    static final int CAMERA_PERMISSION=110;
    static final int CAMERA_IMAGE=10;
    static final int PHOTO_IMAGE=20;

    int tPosition;
    Question t;
    boolean isPicSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_storage);

        jelly=findViewById(R.id.jelly);
        jelly.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyTag","1 REFRESH!!!!!!!!");
                        G.loadCurrentSet(StudyStorageActivity.this);
                        setView();
                        jelly.setRefreshing(false);
                    }
                },2000);
            }
        });

        View loadingView= LayoutInflater.from(this).inflate(R.layout.jelly_loading,null);
        jelly.setLoadingView(loadingView);


        helper.setEdgeMode(true).setParallaxMode(true).setParallaxRatio(3).setNeedBackgroundShadow(true).init(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        recyclerView=findViewById(R.id.recycler);
        stoQuestionsAdapter=new StoQuestionsAdapter(this);
        recyclerView.setAdapter(stoQuestionsAdapter);

        tvTitle =findViewById(R.id.tv_title);
        tvInfo=findViewById(R.id.tv_info);

        setView();


    }

    public void clickCamera(int position){

        if(isPicSetting) return;

        if(G.getUserId()==2||G.getUserId()==3){
            //관리자기 때문에 그냥 넘어감 ㅋ

        }else if(G.currentStudySet.getUserID()!=G.getUserId()){
            Toast.makeText(this, getString(R.string.storage_youcannoteditthis)+"", Toast.LENGTH_SHORT).show();
            return;
        }

        permissionCheck();

        isPicSetting=true;
        t=G.currentStudySet.getSgSet().getQuestions().get(position);
        tPosition=position;

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if(intent.resolveActivity(getPackageManager())!=null)
        startActivityForResult(intent,CAMERA_IMAGE);


    }

    public void clickPhoto(int position){

        if(isPicSetting) return;

        if(G.getUserId()==2||G.getUserId()==3){
            //관리자기 때문에 그냥 넘어감 ㅋ

        }else if(G.currentStudySet.getUserID()!=G.getUserId()){
            Toast.makeText(this, getString(R.string.storage_youcannoteditthis)+"", Toast.LENGTH_SHORT).show();
            return;
        }

        permissionCheck();

        isPicSetting=true;
        t=G.currentStudySet.getSgSet().getQuestions().get(position);
        tPosition=position;

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_IMAGE);

    }



    public void permissionCheck(){
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

                        t.setQuestionPic(getRealPathFromUri(uri));


                        //stoQuestionsAdapter.notifyItemChanged(position);
                        updateQuestionPic();
                    }else{
                        Bitmap bm=(Bitmap)data.getExtras().get("data");
                        Log.i("MyTag","비트맵으로 나왔습니다.");
                        if(bm!=null){
                            //Glide.with(this).load(bm).into(iv);
                            Uri uriFromBm=saveBitmapAndGetUri(bm);

                            if(uriFromBm!=null) {
                                Log.i("MyTag","bitmap 저장하고 uri로 바꿨습니다 : "+uriFromBm.toString());

                                t.setQuestionPic(uriFromBm.getPath());
                                updateQuestionPic();
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
                        t.setQuestionPic(getRealPathFromUri(uri));
                        //stoQuestionsAdapter.notifyItemChanged(position);
                        updateQuestionPic();
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
            isPicSetting=false;
            e.printStackTrace();
        } catch (IOException e) {
            isPicSetting=false;
            e.printStackTrace();
        }

        return null;

    }


    public void setView(){
        tvTitle.setText(G.currentStudySet.getSgSet().getTitle());
        tvInfo.setText(G.currentStudySet.getSgSet().getInfo());
        if(tvInfo.getText().length()==0) tvInfo.setVisibility(GONE);
        else tvInfo.setVisibility(View.VISIBLE);
         stoQuestionsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.studystorage_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                helper.finish();
                return true;

            case R.id.edit:

                if(G.getUserId()==2||G.getUserId()==3){
                    //관리자기 때문에 그냥 넘어감 ㅋ

                }else if(G.currentStudySet.getUserID()!=G.getUserId()){
                    Toast.makeText(this, getString(R.string.storage_youcannoteditthis)+"", Toast.LENGTH_SHORT).show();
                    return true;
                }

                Intent intent=new Intent(StudyStorageActivity.this,UpdateSetActivity.class);
                intent.putExtra("mode","update");
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
                startActivity(intent);

                return true;
            case R.id.action_refresh:
                jelly.post(new Runnable() {
                    @Override
                    public void run() {
                        jelly.setRefreshing(true);
                        Log.i("MyTag","2 REFRESH!!!!!!!!");
                    }
                });
                jelly.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jelly.setRefreshing(false);
                        Log.i("MyTag","3 REFRESH!!!!!!!!");
                    }
                },2000);
                return true;

        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        helper.finish();
    }

    public void updateQuestionPic(){
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Set/updateQuestionPic.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MyTag","upload Question Pic : "+response);
                //G.currentStudySet.getSgSet().getQuestions().get(position).setQuestionPic(response+"");
                //stoQuestionsAdapter.notifyItemChanged(position);

                isPicSetting=false;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag","upload Question Pic ERROR : "+error.getMessage());
                isPicSetting=false;

            }
        });

        multiPartRequest.addStringParam("questionID",t.getQuestionID()+"");

        if(t.getQuestionPic()!=null)
            multiPartRequest.addFile("imgPath",t.getQuestionPic());

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);

    }


}
