package com.example.demo1;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText partner1Name, partner2Name;
    private Button calculateButton;
    private LottieAnimationView heartAnimation, resultAnimation;
    private TextView resultText;
    private RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        partner1Name = findViewById(R.id.partner1_name);
        partner2Name = findViewById(R.id.partner2_name);
        calculateButton = findViewById(R.id.calculate_button);
        heartAnimation = findViewById(R.id.heart_animation);
        resultText = findViewById(R.id.result_text);
        mainLayout = findViewById(R.id.main_layout);

        // Create Lottie view programmatically for result animation
        resultAnimation = new LottieAnimationView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(500, 500);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        resultAnimation.setVisibility(View.GONE);
        mainLayout.addView(resultAnimation, params);

        calculateButton.setOnClickListener(v -> {
            shakeButton(calculateButton);
            new Handler().postDelayed(this::calculateLovePercentage, 2000);
        });
    }

    private void calculateLovePercentage() {
        String name1 = partner1Name.getText().toString().trim();
        String name2 = partner2Name.getText().toString().trim();

        if (name1.isEmpty() || name2.isEmpty()) {
            resultText.setText("Please enter both names!");
            resultText.setVisibility(View.VISIBLE);
            return;
        }

        calculateButton.setVisibility(View.GONE);
        resultText.setVisibility(View.GONE);
        heartAnimation.setVisibility(View.VISIBLE);
        heartAnimation.playAnimation();

        new Handler().postDelayed(() -> {
            heartAnimation.cancelAnimation();
            heartAnimation.setVisibility(View.GONE);
            calculateButton.setVisibility(View.VISIBLE);

            Random random = new Random();
            int lovePercentage = random.nextInt(101);

            // Select the appropriate Lottie animation based on percentage
            if (lovePercentage < 40) {
                resultAnimation.setAnimation(R.raw.broke);
            } else if (lovePercentage < 75) {
                resultAnimation.setAnimation(R.raw.moderate);
            } else {
                resultAnimation.setAnimation(R.raw.more);
            }

            // Start the pop-up animation from the bottom
            showResultWithAnimation();

            resultText.setText(name1 + " & " + name2 + "\nLove Percentage: " + lovePercentage + "%");
            resultText.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> resultAnimation.setVisibility(View.GONE), 4000);

        }, 2000);
    }

    private void showResultWithAnimation() {
        resultAnimation.setVisibility(View.VISIBLE);

        // Animation from bottom to its normal position
        TranslateAnimation slideUp = new TranslateAnimation(
                0, 0, resultAnimation.getHeight(), 0);
        slideUp.setDuration(700);  // Animation duration
        slideUp.setFillAfter(true);  // Keeps the view in place
        resultAnimation.startAnimation(slideUp);

        resultAnimation.playAnimation();
    }

    private void shakeButton(View view) {
        TranslateAnimation shake = new TranslateAnimation(0, 20, 0, 20);
        shake.setDuration(100);
        shake.setInterpolator(new CycleInterpolator(10));  // 10 cycles in 1 second
        view.startAnimation(shake);
    }
}