package com.example.movie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyCanvas extends View {
    Bitmap resize =null;
    Bitmap mBitmap;
    Canvas mCanvas;
    Paint mPaint = new Paint();
    Paint paint = new Paint();
    String operationType = "";

    public MyCanvas(Context context) {
        super(context);

        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint.setColor(Color.BLACK);
        paint.setColor(Color.BLACK);
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint.setColor(Color.BLACK);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            if (operationType.equalsIgnoreCase("eraser")) {
                clear();
            } else if(operationType.equalsIgnoreCase("open")){
                canvas.drawBitmap(resize,(getWidth()-resize.getWidth())/2, (getHeight() - resize.getHeight())/2, paint);
            }

            canvas.drawBitmap(mBitmap, 0, 0, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.WHITE);
        mCanvas.save();
    }

    int oldX = -1, oldY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == event.ACTION_DOWN) {
            oldX = x;
            oldY = y;
        } else if (event.getAction() == event.ACTION_MOVE) {
            if (oldX != -1) {
                mCanvas.drawLine(oldX, oldY, x, y, paint);

                invalidate();

                oldX = x;
                oldY = y;
            }

        } else if (event.getAction() == event.ACTION_UP) {
            if (oldX != -1) {
                mCanvas.drawLine(oldX, oldY, x, y, paint);

                invalidate();
            }
            oldX = -1;
            oldY = -1;
        }

        return true;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;

        invalidate();
    }

    private void clear() {
        mBitmap.eraseColor(Color.WHITE);
        mCanvas.restore();
        mCanvas.save();

        this.setOperationType("");
    }

    public void open(String file_name, Context context) {
        File file = new File(file_name);

        if (!file.exists()) {
            Toast.makeText(context.getApplicationContext(), "저장된 파일이 없습니다.", Toast.LENGTH_SHORT).show();

            return ;
        }

        clear();

        Bitmap bitmap = BitmapFactory.decodeFile(file_name);

        resize = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, false);
        mCanvas.drawBitmap(resize,(getWidth()-resize.getWidth())/2, (getHeight() - resize.getHeight())/2, mPaint);

        this.invalidate();

        Toast.makeText(context.getApplicationContext(), "불러오기 완료", Toast.LENGTH_SHORT).show();
    }

    public void setPenColor(Boolean b){
        if(b) paint.setColor(Color.RED);
        else paint.setColor(Color.BLUE);
    }

    public void setPenWidth(boolean b){
        if(b) paint.setStrokeWidth(5);
        else paint.setStrokeWidth(3);
    }

    public boolean save(String file_name) {
        try {
            FileOutputStream out = new FileOutputStream(file_name, false);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            return true;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }

        return false;
    }
}
