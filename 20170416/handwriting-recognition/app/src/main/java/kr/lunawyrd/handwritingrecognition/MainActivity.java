package kr.lunawyrd.handwritingrecognition;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kr.lunawyrd.handwritingrecognition.network.APIService;
import kr.lunawyrd.handwritingrecognition.network.RetrofitManager;
import kr.lunawyrd.handwritingrecognition.view.DrawingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.lunawyrd.handwritingrecognition.R.id.drawing_view;

public class MainActivity extends AppCompatActivity {

    private DrawingView mDrawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawingView = (DrawingView) findViewById(drawing_view);

        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView view = new ImageView(MainActivity.this);
                Bitmap image = MainActivity.getBitmapFromView(mDrawingView);
                int width = image.getWidth();
                int height = image.getHeight();
                int scaleWidth = 28;
                int scale = width / scaleWidth;
                int scaleHeight = height/scale;

                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                Bitmap newImage = Bitmap.createScaledBitmap(image, scaleWidth, scaleHeight, false);
//                Toast.makeText(MainActivity.this, "width : " + newImage.getWidth() + ", height : " + newImage.getHeight(), Toast.LENGTH_SHORT).show();
                view.setImageBitmap(newImage);

                List<Float> pixelList = new ArrayList<>();
                StringBuilder builder = new StringBuilder();

                for(int w=0; w<newImage.getWidth(); w++) {
                    pixelList.add(0.0f);
                }

                for(int w=0; w<newImage.getWidth(); w++) {
                    pixelList.add(0.0f);
                }

                for(int h=0; h<newImage.getHeight(); h++) {
                    builder.setLength(0);
                    for(int w=0; w<newImage.getWidth(); w++) {
                        float color = newImage.getPixel(w, h);
                        color = color == -1 ? 0.0f : 1.0f;
                        builder.append(color + " ");
                        pixelList.add(color);
                    }
                }

                for(int w=0; w<newImage.getWidth(); w++) {
                    pixelList.add(0.0f);
                }

                Log.d("TEST", new Gson().toJson(pixelList));

//                new AlertDialog.Builder(MainActivity.this).setView(view).show();
                mDrawingView.clear();

                image.recycle();

                callAnalysis(pixelList);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void callAnalysis(List<Float> lineList){
        APIService apiService = RetrofitManager.getInstance().create(APIService.class);
        apiService.analysis(lineList).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    int code = response.code();
                    if (code != 200) {
                        throw new Exception("Error : " + code + " : " + response.message());
                    }

                    String httpResponse = response.body();
                    Toast.makeText(MainActivity.this, httpResponse, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
