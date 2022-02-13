package com.example.artify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Random;

public class Puzzlegame extends AppCompatActivity {
    //For the grid
    private static final int COLUMNS = 4;
    private static final int DIMENSIONS = COLUMNS * COLUMNS;
    private static GestureDetectGridView mGridView;
    private static int mColumnWidth, mColumnHeight;
    private static String[] tileList;
    private static int[] rotations;

    //For the informations
    private Chronometer chronometer;
    private TextView chronometerLabelInLayout;
    private TextView movesTextView;
    private static int mCounter = 0;
    private TextView movesLabelInLayout;
    public ImageView imageHelp;

    //For the movements
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    //For the animations
    private static int flag = 0;
    private ConstraintLayout constraintTop;
    private ViewGroup.LayoutParams topParams;
    private int topParamsHeight;
    private int topParamsWidth;
    private ConstraintLayout constraintLayout;
    private static float xOriginImage;
    private static float yOriginImage;
    private static float xScaleOriginImage;
    private static float yScaleOriginImage;

    //For the image
    private String URLOp;
    public static StorageReference gsRef;

    //Go back
    private Button backToOperaButton;
    private static ArrayList<Button> buttons;
    private static Puzzlegame instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzlegame);
        Intent fromLista = getIntent();
        URLOp = fromLista.getStringExtra("URLOpera");

        instance = this;

        init();
        setImage();

        if(savedInstanceState == null) {
            scramble();
        } else {
            chronometer.stop();
            tileList = savedInstanceState.getStringArray("tileList");
            rotations = savedInstanceState.getIntArray("rotations");
            mCounter = savedInstanceState.getInt("moves");
            movesTextView.setText(Integer.toString(mCounter));
            chronometer.setBase(savedInstanceState.getLong("chronometerTime"));
            chronometer.start();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putStringArray("tileList", tileList);
        state.putIntArray("rotations", rotations);
        state.putInt("moves", mCounter);
        state.putLong("chronometerTime", chronometer.getBase());
    }

    /**
     * Funzione per l'inizializzazione delle risorse
     */
    private void init() {
        //For the grid
        mGridView = (GestureDetectGridView) findViewById(R.id.gridView);
        mGridView.setNumColumns(COLUMNS);
        tileList = new String[DIMENSIONS];
        rotations = new int[DIMENSIONS];
        for(int i = 0; i < DIMENSIONS; i++)
        {
            tileList[i] = String.valueOf(i);
            rotations[i] = 0;
        }

        //For the informations
        imageHelp = (ImageView) findViewById(R.id.imageHelp);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometerLabelInLayout = (TextView) findViewById(R.id.chronometerLabel);
        chronometer.start();
        movesTextView = (TextView) findViewById(R.id.moves);
        movesLabelInLayout = (TextView) findViewById(R.id.movesLabel);
        mCounter = 0;
        movesTextView.setText(Integer.toString(mCounter));

        //For the animations
        constraintTop = (ConstraintLayout) findViewById(R.id.constraintTop);
        topParams = constraintTop.getLayoutParams();
        topParamsHeight = topParams.height;
        topParamsWidth = topParams.width;
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutPuzzleGame);

        //Go back
        backToOperaButton = (Button) findViewById(R.id.back_to_opera_button);
    }

    /**
     * Funzione per il posizionamento randomico dei pezzi
     */
    private void scramble() {
        int index;
        String temp;
        Random random = new Random();

        for(int i = tileList.length - 1; i > 0; i--) {
            index = random.nextInt(i+1);
            temp = tileList[index];
            tileList[index] = tileList[i];
            tileList[i] = temp;

            rotations[i] = random.nextInt(4);
        }
    }

    /**
     * Funzione per il ridimensionamento dell'immagine
     */
    private void setDimensions() {
        ViewTreeObserver vto = mGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridView.getMeasuredWidth();
                int displayHeight = mGridView.getMeasuredHeight();

                if(displayWidth > displayHeight)
                    displayWidth = displayHeight;
                else
                    displayHeight = displayWidth;

                int statusBarHeight = getStatusBarHeight(getApplicationContext());
                int requiredHeight = displayHeight - statusBarHeight;

                if(requiredHeight > displayWidth)
                    requiredHeight = displayWidth;
                else
                    displayWidth = requiredHeight;

                mGridView.getLayoutParams().height = requiredHeight;
                mGridView.getLayoutParams().width = displayWidth;

                mColumnWidth = mGridView.getLayoutParams().width / COLUMNS;
                mColumnHeight = mGridView.getLayoutParams().height / COLUMNS;

                display(getApplicationContext());
            }
        });
    }

    /**
     * funzione per la definizione dell'altezza della status bar
     * @param context: context di riferimento
     * @return altezza della status bar
     */
    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Funzione per il display del puzzle
     * @param context: context di riferimento
     */
    private void display(Context context) {
        buttons = new ArrayList<>();
        Button button;
        ArrayList<Drawable> imageSplitted = split(context);

        for(int i = 0; i < tileList.length; i++) {
            button = new Button(context);

            if (tileList[i].equals("0"))
                button.setBackground(imageSplitted.get(0));
            else if (tileList[i].equals("1"))
                button.setBackground(imageSplitted.get(1));
            else if (tileList[i].equals("2"))
                button.setBackground(imageSplitted.get(2));
            else if (tileList[i].equals("3"))
                button.setBackground(imageSplitted.get(3));
            else if (tileList[i].equals("4"))
                button.setBackground(imageSplitted.get(4));
            else if (tileList[i].equals("5"))
                button.setBackground(imageSplitted.get(5));
            else if (tileList[i].equals("6"))
                button.setBackground(imageSplitted.get(6));
            else if (tileList[i].equals("7"))
                button.setBackground(imageSplitted.get(7));
            else if (tileList[i].equals("8"))
                button.setBackground(imageSplitted.get(8));
            else if (tileList[i].equals("9"))
                button.setBackground(imageSplitted.get(9));
            else if (tileList[i].equals("10"))
                button.setBackground(imageSplitted.get(10));
            else if (tileList[i].equals("11"))
                button.setBackground(imageSplitted.get(11));
            else if (tileList[i].equals("12"))
                button.setBackground(imageSplitted.get(12));
            else if (tileList[i].equals("13"))
                button.setBackground(imageSplitted.get(13));
            else if (tileList[i].equals("14"))
                button.setBackground(imageSplitted.get(14));
            else if (tileList[i].equals("15"))
                button.setBackground(imageSplitted.get(15));

            button.setRotation((float) 90.0 * rotations[i]);

            button.setId(i);
            buttons.add(button);
        }

        mGridView.setAdapter(new CustomAdapter(buttons, mColumnWidth, mColumnHeight));
    }

    /**
     * funzione per effettuare la rotazione dei singoli pezzi del puzzle
     * @param context: context di riferimento
     * @param pos: posizione del pezzo
     */
    public void rotate(Context context, int pos) {
        if (rotations[pos] == 3)
            rotations[pos] = 0;
        else
            rotations[pos]++;

        buttons.get(pos).setRotation((float) 90.0 * rotations[pos]);
        mCounter++;
        movesTextView.setText(Integer.toString(mCounter));

        display(context);

        if (isSolved()) {
            chronometer.stop();
            Intent completed = new Intent(context, PuzzleCompleted.class);
            completed.putExtra("Chronometer", chronometer.getText().toString());
            completed.putExtra("Moves", Integer.toString(mCounter));
            context.startActivity(completed);
            finish();
        }
    }

    /**
     * Funzione per il setting dell'immagine da ricomporre
     */
    private void setImage() {
        gsRef = FirebaseStorage.getInstance().getReferenceFromUrl(URLOp);

        gsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Puzzlegame.this).load(uri.toString()).into(imageHelp);
                setDimensions();
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Puzzlegame.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Funzione per lo split dell'immagine
     * @param context:context di riferimento
     * @return immagine splittata
     */
    private ArrayList<Drawable> split(Context context) {
        ArrayList<Bitmap> chunkedImage = new ArrayList<>(DIMENSIONS);
        ArrayList<Drawable> chunkedImageDrawable = new ArrayList<>(DIMENSIONS);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageHelp.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, mColumnWidth*COLUMNS, mColumnHeight*COLUMNS, true);

        int yCoord = 0;
        for(int i = 0; i < COLUMNS; i++) {
            int xCoord = 0;
            for(int j = 0; j < COLUMNS; j++) {
                chunkedImage.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, mColumnWidth, mColumnHeight));
                xCoord += mColumnWidth;
            }
            yCoord += mColumnHeight;
        }

        for(int i = 0; i < DIMENSIONS; i++) {
            chunkedImageDrawable.add(new BitmapDrawable(context.getResources(), chunkedImage.get(i)));
        }
        return chunkedImageDrawable;
    }

    /**
     * Funzione per il movimento dei pezzi
     * @param context: context di riferimento
     * @param direction: direzione di spostamento
     * @param position: posizione del pezzo
     */
    public void moveTiles(Context context, String direction, int position) {

        //Upper-left-corner tile
        if(position == 0) {
            if (direction.equals(RIGHT)) swap(context, position, 1);
            else if (direction.equals(DOWN)) swap(context, position, COLUMNS);
            else Toast.makeText(context, R.string.invalid_move, Toast.LENGTH_SHORT).show();

            //Upper-center tiles
        }else if(position > 0 && position < COLUMNS - 1) {
            if(direction.equals(LEFT)) swap(context, position, -1);
            else if(direction.equals(DOWN)) swap(context, position, COLUMNS);
            else if(direction.equals(RIGHT)) swap(context, position, 1);
            else Toast.makeText(context, R.string.invalid_move, Toast.LENGTH_SHORT).show();

            //Upper-right-corner tile
        }else if(position == COLUMNS - 1) {
            if(direction.equals(LEFT)) swap(context, position, -1);
            else if(direction.equals(DOWN)) swap(context, position, COLUMNS);
            else Toast.makeText(context, R.string.invalid_move, Toast.LENGTH_SHORT).show();

            //Left-side tiles (but NOT upper-left-corner and bottom-left-corner)
        }else if(position > COLUMNS -1 && position < DIMENSIONS - COLUMNS && position % COLUMNS == 0) {
            if(direction.equals(UP)) swap(context, position, -COLUMNS);
            else if(direction.equals(RIGHT)) swap(context, position, 1);
            else if(direction.equals(DOWN)) swap(context, position, COLUMNS);
            else Toast.makeText(context, R.string.invalid_move, Toast.LENGTH_SHORT).show();

            //Right-side AND bottom-right-corner tiles (but NOT upper-right-corner)
        }else if(position == COLUMNS * 2 - 1 || position == COLUMNS * 3 - 1 || position == COLUMNS * 4 - 1) {
            if(direction.equals(UP)) swap(context, position, -COLUMNS);
            else if(direction.equals(LEFT)) swap(context, position, -1);
            else if(direction.equals(DOWN)) {
                if(position <= DIMENSIONS - COLUMNS - 1) swap(context, position, COLUMNS);
                else Toast.makeText(context, R.string.invalid_move, Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(context, R.string.invalid_move, Toast.LENGTH_SHORT).show();

            //Bottom-left-corner tile
        }else if(position == DIMENSIONS - COLUMNS) {
            if(direction.equals(UP)) swap(context, position, -COLUMNS);
            else if(direction.equals(RIGHT)) swap(context, position, 1);
            else Toast.makeText(context, R.string.invalid_move, Toast.LENGTH_SHORT).show();

            //Bottom-center tiles
        }else if(position < DIMENSIONS - 1 && position > DIMENSIONS - COLUMNS) {
            if(direction.equals(UP)) swap(context, position, -COLUMNS);
            else if(direction.equals(LEFT)) swap(context, position, -1);
            else if(direction.equals(RIGHT)) swap(context, position, 1);
            else Toast.makeText(context, R.string.invalid_move, Toast.LENGTH_SHORT).show();

            //Center tiles
        }else {
            if(direction.equals(UP)) swap(context, position, -COLUMNS);
            else if(direction.equals(LEFT)) swap(context, position, -1);
            else if(direction.equals(RIGHT)) swap(context, position, 1);
            else swap(context, position, COLUMNS);
        }
    }

    /**
     * Funzione per la gestione degli scambi tra pezzi
     * @param context: context di riferimento
     * @param position: posizione del pezzo
     * @param swap: indica il numero di celle di cui il pezzo si deve spostare
     */
    private void swap(Context context, int position, int swap) {
        String tempImage = tileList[position + swap];
        tileList[position + swap] = tileList[position];
        tileList[position] = tempImage;

        int tempRotation = rotations[position + swap];
        rotations[position + swap] = rotations[position];
        rotations[position] = tempRotation;

        mCounter++;
        movesTextView.setText(Integer.toString(mCounter));

        display(context);

        if(isSolved()) {
            chronometer.stop();
            Intent completed = new Intent(context, PuzzleCompleted.class);
            completed.putExtra("URLOp", URLOp);
            completed.putExtra("Chronometer", chronometer.getText().toString());
            completed.putExtra("Moves", Integer.toString(mCounter));
            context.startActivity(completed);
            finish();
        }
    }

    /**
     * funzione per la verifica della risoluzione del puzzle
     * @return true/false
     */
    private boolean isSolved() {
        boolean solved = false;

        for(int i = 0; i < tileList.length; i++) {
            if(tileList[i].equals(String.valueOf(i)) && rotations[i] == 0) {
                solved = true;
            }else{
                solved = false;
                break;
            }
        }
        return solved;
    }

    /**
     * Funzione per visualizzare l'immagine di aiuto al click
     * @param imageHelp
     */
    public void imageHelpClick(View imageHelp) {
        imageHelp.setClickable(false);

        imageHelpClickImplementation();

        imageHelp.postDelayed(() -> imageHelp.setClickable(true), 1100);
    }


    private void imageHelpClickImplementation() {
        float xOriginChronometerLabel = chronometerLabelInLayout.getX();
        float xOriginChronometer = chronometer.getX();
        float xOriginMovesLabel = movesLabelInLayout.getX();
        float xOriginMoves = movesTextView.getX();

        float yOriginChronometerLabel = chronometerLabelInLayout.getY();
        float yOriginChronometer = chronometer.getY();
        float yOriginMovesLabel = movesLabelInLayout.getY();
        float yOriginMoves = movesTextView.getY();

        float xImage = imageHelp.getX();
        float yImage = imageHelp.getY();
        float xScaleImage = imageHelp.getScaleX();
        float yScaleImage = imageHelp.getScaleY();

        int constraintHeight = constraintLayout.getHeight();
        int constraintWidth = constraintLayout.getWidth();

        if(flag == 0) {
            xOriginImage = xImage;
            yOriginImage = yImage;
            xScaleOriginImage = xScaleImage;
            yScaleOriginImage = yScaleImage;
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            flag = portraitAnimation(flag, xOriginChronometerLabel, xOriginChronometer, xOriginMovesLabel, xOriginMoves, xScaleImage, yScaleImage, constraintHeight, constraintWidth);
        } else {
            flag = landscapeAnimation(flag, yOriginChronometerLabel, yOriginChronometer, yOriginMovesLabel, yOriginMoves, xScaleImage, yScaleImage, constraintHeight, constraintWidth);
        }

        constraintTop.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        constraintTop.getLayoutTransition().setDuration(LayoutTransition.CHANGING, 900);
        constraintTop.setLayoutParams(topParams);
    }

    private int portraitAnimation(int f, float xOCL, float xOC, float xOML, float xOM, float xSI, float ySI, int constraintH, int constraintW) {
        //f == 0 -> move image to center; f == 1 -> move image back
        if(f == 0) {
            f = 1;
            topParams.height = constraintH;
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) imageHelp.getLayoutParams();
            float marginStart = (float) lp.getMarginStart();
            chronometerLabelInLayout.animate().x(xOCL - (float)constraintW/9).setDuration(1000).start();
            chronometer.animate().x(xOC - (float)constraintW/9).setDuration(1000).start();
            movesLabelInLayout.animate().x(xOML - (float)constraintW/9).setDuration(1000).start();
            movesTextView.animate().x(xOM - (float)constraintW/9).setDuration(1000).start();
            imageHelp.animate().x((float)constraintW/3 + marginStart).setDuration(1000).start();
            imageHelp.animate().y((float)constraintH/2).setDuration(1000).start();
            imageHelp.animate().scaleX(xSI +2);
            imageHelp.animate().scaleY(ySI +2);
            mGridView.setVisibility(View.INVISIBLE);
            backToOperaButton.setVisibility(View.INVISIBLE);
        }else{
            f = 0;
            topParams.height = topParamsHeight;
            chronometerLabelInLayout.animate().x(xOCL + (float)constraintW/9).setDuration(1000).start();
            chronometer.animate().x(xOC + (float)constraintW/9).setDuration(1000).start();
            movesLabelInLayout.animate().x(xOML + (float)constraintW/9).setDuration(1000).start();
            movesTextView.animate().x(xOM + (float)constraintW/9).setDuration(1000).start();
            imageHelp.animate().x(xOriginImage).setDuration(1000).start();
            imageHelp.animate().y(yOriginImage).setDuration(1000).start();
            imageHelp.animate().scaleX(xScaleOriginImage);
            imageHelp.animate().scaleY(yScaleOriginImage);
            mGridView.setVisibility(View.VISIBLE);
            backToOperaButton.setVisibility(View.VISIBLE);

            //sets grid's buttons un-clickable during the animation
            for(int i = 0; i < buttons.size(); i++) {
                int finalI = i;
                buttons.get(i).setClickable(false);
                buttons.get(i).postDelayed(() -> buttons.get(finalI).setClickable(true), 1100);
            }
        }
        return f;
    }

    private int landscapeAnimation(int f, float yOCL, float yOC, float yOML, float yOM, float xSI, float ySI, int constraintH, int constraintW) {
        //f == 0 -> move image to center; f == 1 -> move image back
        if(f == 0) {
            f = 1;
            topParams.width = constraintW;
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) imageHelp.getLayoutParams();
            float marginBottom = (float) lp.bottomMargin;
            chronometerLabelInLayout.animate().y(yOCL + (float)constraintH/4).setDuration(1000).start();
            chronometer.animate().y(yOC + (float)constraintH/4).setDuration(1000).start();
            movesLabelInLayout.animate().y(yOML + (float)constraintH/4).setDuration(1000).start();
            movesTextView.animate().y(yOM + (float)constraintH/4).setDuration(1000).start();
            imageHelp.animate().x((float)constraintW/2).setDuration(1000).start();
            imageHelp.animate().y((float)constraintH/3 - marginBottom/2).setDuration(1000).start();
            imageHelp.animate().scaleX(xSI +1);
            imageHelp.animate().scaleY(ySI +1);
            mGridView.setVisibility(View.INVISIBLE);
            backToOperaButton.setVisibility(View.INVISIBLE);
        }else{
            f = 0;
            topParams.width = topParamsWidth;
            chronometerLabelInLayout.animate().y(yOCL - (float)constraintH/4).setDuration(1000).start();
            chronometer.animate().y(yOC - (float)constraintH/4).setDuration(1000).start();
            movesLabelInLayout.animate().y(yOML - (float)constraintH/4).setDuration(1000).start();
            movesTextView.animate().y(yOM - (float)constraintH/4).setDuration(1000).start();
            imageHelp.animate().x(xOriginImage).setDuration(1000).start();
            imageHelp.animate().y(yOriginImage).setDuration(1000).start();
            imageHelp.animate().scaleX(xScaleOriginImage);
            imageHelp.animate().scaleY(yScaleOriginImage);
            mGridView.setVisibility(View.VISIBLE);
            backToOperaButton.setVisibility(View.VISIBLE);

            //sets grid's buttons un-clickable during the animation
            for(int i = 0; i < buttons.size(); i++) {
                int finalI = i;
                buttons.get(i).setClickable(false);
                buttons.get(i).postDelayed(() -> buttons.get(finalI).setClickable(true), 1100);
            }
        }
        return f;
    }

    public static Puzzlegame getInstance() {
        return instance;
    }

    /**
     * Funzione per tornare all'opera di riferimento
     * @param view: view di riferimento
     */
    public void backToOpera(View view) {
        finish();
    }
}