package com.example.harshit.harshitspider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import java.util.Random;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private String[] words;
    private Random rand;
    private String currWord;
    private LinearLayout wordLayout;
    private TextView[] charViews;
    private GridView letters;
    private LetterAdapter ltrAdapt;
    TextView best;
    private ImageView[] bodyParts;

    private int numParts=6;

    private int currPart,bestc;

    private int numChars;

    private int numCorr;
    private int bestScore;
    SharedPreferences prefs;

    SharedPreferences.Editor edt;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        best=(TextView)findViewById(R.id.textView);
        prefs=getSharedPreferences("com.example.harshit.harshitspider", Context.MODE_PRIVATE);

        edt=prefs.edit();

        bestScore=prefs.getInt("best",7);

        best.setText("Best: "+bestScore+" wrong(s)");
        Resources res = getResources();

        words = res.getStringArray(R.array.words);
        rand = new Random();
        currWord = "";

        wordLayout = (LinearLayout)findViewById(R.id.word);
        letters = (GridView)findViewById(R.id.letters);

        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView)findViewById(R.id.head);
        bodyParts[1] = (ImageView)findViewById(R.id.body);
        bodyParts[2] = (ImageView)findViewById(R.id.arm1);
        bodyParts[3] = (ImageView)findViewById(R.id.arm2);
        bodyParts[4] = (ImageView)findViewById(R.id.leg1);
        bodyParts[5] = (ImageView)findViewById(R.id.leg2);
        bestScore=prefs.getInt("best",7);

        best.setText("Best: "+bestScore+" wrong(s)");
        playGame();
    }private void playGame() {

        String newWord = words[rand.nextInt(words.length)];

        while (newWord.equals(currWord))  {
            newWord = words[rand.nextInt(words.length)];
        }
        currWord = newWord;

        charViews = new TextView[currWord.length()];

        wordLayout.removeAllViews();

        for (int c = 0; c < currWord.length(); c++) {
            charViews[c] = new TextView(this);
            charViews[c].setText(""+currWord.charAt(c));

            charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.WHITE);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);

            wordLayout.addView(charViews[c]);

        }
        ltrAdapt=new LetterAdapter(this);
        letters.setAdapter(ltrAdapt);

        currPart=0;
        numChars=currWord.length();
        numCorr=0;

        for(int p = 0; p < numParts; p++) {
            bodyParts[p].setVisibility(View.INVISIBLE);
        }
    }

    public void letterPressed(View view) {

        String ltr=((TextView)view).getText().toString();
        char letterChar = ltr.charAt(0);

        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);


        boolean correct = false;
        for(int k = 0; k < currWord.length(); k++) {
            if (currWord.charAt(k)==letterChar){
                correct = true;
                numCorr++;
                charViews[k].setTextColor(Color.BLACK);
            }
        }

        if (correct) {


            if (numCorr == numChars) {
                if(currPart<bestScore) {
bestScore=currPart;
                    best.setText("Best: "+bestScore+" wrong(s)");
                }
                disableBtns();


                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle("Yay, well done!");
                winBuild.setMessage("You won!\n\nThe answer was:\n\n"+currWord);
                winBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               MainActivity.this.playGame();
                            }});

                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }});

                winBuild.show();
            }
        } else if (currPart < numParts) {

            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        } else {

            disableBtns();


            AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
            loseBuild.setTitle("Oopsie");
            loseBuild.setMessage("You lose!\n\nThe answer was:\n\n"+currWord);
            loseBuild.setPositiveButton("Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.playGame();
                        }});

            loseBuild.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }});

            loseBuild.show();

        }
    }

    public void disableBtns() {Toast.makeText(getApplicationContext(),"CURRENT SCORE OF THE GAME IS "+(7-currPart),Toast.LENGTH_LONG).show();
        int numLetters = letters.getChildCount();
        for (int l = 0; l < numLetters; l++) {
            letters.getChildAt(l).setEnabled(false);
        }
    }
}
