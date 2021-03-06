package allan.android.iii.org.tw.myguesscard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends Activity implements OnClickListener {

    HandleDelay hadledelay;
    ArrayList<Integer> arraylist = new ArrayList<Integer>();
    Button reset;
    int cmp = 0;
    ImageView[] card = new ImageView[12];
    ImageView cardA = null, cardB = null;
    private int setImageId[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private int picId[] = {
            R.drawable.picture1, R.drawable.picture1,
            R.drawable.picture2, R.drawable.picture2,
            R.drawable.picture3, R.drawable.picture3,
            R.drawable.picture4, R.drawable.picture4,
            R.drawable.picture5, R.drawable.picture5,
            R.drawable.picture6, R.drawable.picture6};
    private int setImageViewUi[] = {R.id.card0, R.id.card1, R.id.card2, R.id.card3, R.id.card4,
            R.id.card5, R.id.card6, R.id.card7, R.id.card8, R.id.card9, R.id.card10, R.id.card11};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hadledelay = new HandleDelay();
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);

        setImageView();
        initListener();
        setInitImageViewId();
        Random();
    }

    public void onClick(View v) {
        if (v == reset) {
            for (int i = 0; i < 12; i++) {
                card[i].setImageDrawable(getResources().getDrawable(R.drawable.pic1));
            }
            cmp = 0;
            arraylist.clear();
            Random();
            initListener();
            setInitImageViewId();
            return;
        }
        for (int i = 0; i < 12; i++) {
            if (v == card[i]) {
                setBeforeAnimation(card[i], arraylist.get(i));
            }
        }
        if (cardA == null) {
            cardA = (ImageView) v;
            removeListener(cardA, 0);
            reset.setEnabled(false);
            //移除剛剛A的值並且鎖住reset按鈕
        } else if (cardB == null) {
            cardB = (ImageView) v;
            removeListener(cardB, 2);
        }
        if (cardA != null && cardB != null) {
            count();
        }
    }

    public void setBeforeAnimation(ImageView select, int id) {
        float centerX = select.getWidth() / 2f;
        float centerY = select.getHeight() / 2f;
        final Rotate3dAnimation rotation = new Rotate3dAnimation(0, 92,
                centerX, centerY, 360, true);
        // 動畫持續時間300毫秒
        rotation.setDuration(300);
        // 動畫完成後保持完成的狀態
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        // 設置動畫的監聽器
        rotation.setAnimationListener(new TurnToImageViewBefore(select, id));
        select.startAnimation(rotation);
    }

    public void setAfterAnimation(ImageView select) {
        float centerX = select.getWidth() / 2f;
        float centerY = select.getHeight() / 2f;
        final Rotate3dAnimation rotation = new Rotate3dAnimation(360, 270,
                centerX, centerY, 360, true);
        // 動畫持續時間500毫秒
        rotation.setDuration(500);
        // 動畫完成後保持完成的狀態
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        // 設置動畫的監聽器
        rotation.setAnimationListener(new TurnImageViewAfter(select));
        select.startAnimation(rotation);
    }

    class TurnToImageViewBefore implements AnimationListener {
        ImageView imageview;
        int imageId;

        public TurnToImageViewBefore(ImageView imageview, int imageId) {
            this.imageview = imageview;
            this.imageId = imageId;
        }

        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            float centerX = imageview.getWidth() / 2f;
            float centerY = imageview.getHeight() / 2f;
            final Rotate3dAnimation rotation = new Rotate3dAnimation(270, 360,
                    centerX, centerY, 360, false);
            // 持續時間500毫秒
            rotation.setDuration(500);
            // 動畫完成後保持完成的狀態
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            imageview.setImageDrawable(getResources().getDrawable(picId[imageId]));
            imageview.startAnimation(rotation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    class TurnImageViewAfter implements AnimationListener {
        ImageView imageview;

        public TurnImageViewAfter(ImageView imageview) {
            this.imageview = imageview;
        }

        public void onAnimationStart(Animation animation) {
        }

        /**
         * 當ListView的動畫完成後，再啟動的ImageView的動畫，讓ImageView的從不可見變為可見
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            // 獲取中心點位置，以便作旋轉
            float centerX = imageview.getWidth() / 2f;
            float centerY = imageview.getHeight() / 2f;
            //
            final Rotate3dAnimation rotation = new Rotate3dAnimation(270, 180,
                    centerX, centerY, 360, false);
            // 動畫持續時間500毫秒
            rotation.setDuration(500);
            // 動畫完成後保持完成的狀態
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            imageview.setImageDrawable(getResources().getDrawable(R.drawable.pic1));
            imageview.startAnimation(rotation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    public void setInitImageViewId() {
        for (int i = 0; i < 12; i++) {
            card[i].setId(setImageId[i]);
        }
    }

    public void setImageView() {
        for (int i = 0; i < 12; i++) {
            card[i] = (ImageView) findViewById(setImageViewUi[i]);
        }
    }

    public void removeListener(ImageView imageview, int selected) {
        if (selected == 0) {
            for (int i = 0; i < 12; i++) {
                if (imageview.getId() == i) {
                    card[i].setOnClickListener(null);
                }
            }
        } else if (selected == 1) {
            for (int i = 0; i < 12; i++) {
                if (!(card[i].getId() == 1114)) {
                    card[i].setOnClickListener(null);
                }
            }
        } else if (selected == 2) {
            for (int i = 0; i < 12; i++) {
                card[i].setOnClickListener(null);
            }
        }
    }

    public void initListener() {
        for (int i = 0; i < 12; i++) {
            card[i].setOnClickListener(this);
        }
    }

    public void addListener() {
        for (int i = 0; i < 12; i++) {
            if (!(card[i].getId() == 1114)) {
                card[i].setOnClickListener(this);
            }
        }
    }

    private void Compare(ImageView ivA, ImageView ivB) {
        Log.e("ivA", ivA.getDrawable().getConstantState().toString());
        Log.e("ivB", ivB.getDrawable().getConstantState().toString());
        if (ivA.getDrawable().getConstantState() == ivB.getDrawable()
                .getConstantState()) {
            cmp++;
            for (int i = 0; i < 12; i++) {
                if (ivA.getId() == i || ivB.getId() == i) {
                    card[i].setId(1114);
                    removeListener(card[i], 1);
                }
            }
        } else {
            setAfterAnimation(ivA);
            setAfterAnimation(ivB);
        }
        //當配對成功6組，則顯示過關
        if (cmp == 6) {
            Toast.makeText(this, "恭喜破關!可按重新開始!", Toast.LENGTH_LONG).show();
        }
        cardA = null;
        cardB = null;
    }

    private void Random() {
        for (int i = 0; i < 12; i++) {
            arraylist.add(i);
        }
        Collections.shuffle(arraylist);
    }

    public void count() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                for (int i = 1; i <= 3; i++) {
                    msg.what = i;
                    try {
                        Thread.sleep(300);
                        Log.e("Counter", "" + i);
                    } catch (Exception ee) {
                    }
                }
                hadledelay.sendMessage(msg);
            }
        }).start();
    }

    class HandleDelay extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int c = msg.what;
            if (c == 3) {
                Compare(cardA, cardB);
                reset.setEnabled(true);
                addListener();
            }
        }
    }
}