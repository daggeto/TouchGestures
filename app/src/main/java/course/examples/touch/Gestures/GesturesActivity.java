package course.examples.touch.Gestures;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GesturesActivity extends Activity implements
		OnGesturePerformedListener {
	private static final String NO = "No";
	private static final String YES = "Yes";
	private static final String PREV = "Prev";
	private static final String NEXT = "Next";
	private GestureLibrary mLibrary;
	private int mBgColor = 0;
	private int mFirstColor, mStartBgColor = Color.GRAY;
	private LinearLayout mLayout;
    private TextView mDecode;
    private ImageButton mDelete;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        mDecode = (TextView) findViewById(R.id.decode);
        mDelete = (ImageButton) findViewById(R.id.delete);
        mDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mDecode.setText(stripLast(mDecode.getText()));
            }
        });

		mBgColor = new Random().nextInt(0xFFFFFF) | 0xFF000000;
		mFirstColor = mBgColor;

		mLayout = (LinearLayout) findViewById(R.id.main);
		mLayout.setBackgroundColor(mStartBgColor);

		mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mLibrary.load()) {
			finish();
		}

		// Make this the target of gesture detection callbacks
		GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		gestureView.addOnGesturePerformedListener(this);
        gestureView.setUncertainGestureColor(Color.TRANSPARENT);

	}

    private CharSequence stripLast(CharSequence text){
        int end = text.length() - 1;
        if(end < 0){
            return null;
        }
        return text.subSequence(0, end);
    }

    private void addSymbol(Prediction prediction){
        mDecode.append(prediction.name);
    }

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

		// Get gesture predictions
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

		// Get highest-ranked prediction
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);


			if (prediction.score > 3.0) {
				addSymbol(prediction);
			} else {
				Toast.makeText(this, "Wrong symbol", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}