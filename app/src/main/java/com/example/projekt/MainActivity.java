package com.example.projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private GroceryAdapter mAdapter;
    private EditText mEditTextName;
    private TextView mTextViewAmount;
    private int mAmount = 0;
    private Handler mHandler = new Handler();


    Animation bounce;
    Animation slideUp;
    Animation slideDown;
    Animation rotate;
    Animation zoomIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        mAdapter = new GroceryAdapter(this, getAllItems());

        mEditTextName = findViewById(R.id.edittext_name);
        mTextViewAmount = findViewById(R.id.textview_amount);
        final Button buttonIncrease = findViewById(R.id.button_increase);
        final Button buttonDecrease = findViewById(R.id.button_decrease);
        final Button buttonAdd = findViewById(R.id.button_add);
        Button buttonShow = findViewById(R.id.button_show);
        Button buttonInstruction = findViewById(R.id.button_instruction);
        final Button buttonAuthor = findViewById(R.id.button_author);
        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonIncrease.startAnimation(slideUp);
                increase();
            }
        });
        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDecrease.startAnimation(slideDown);
                decrease();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAdd.startAnimation(bounce);
                addItem();
            }
        });

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GroceryList.class);
                startActivity(intent);
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Instruction.class);
                startActivity(intent);
            }
        });

        buttonAuthor.setOnClickListener(new View.OnClickListener() {
            Runnable authorDelayed = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,Author.class);
                    startActivity(intent);
                }
            };
            public void onClick(View v) {
                buttonAuthor.startAnimation(rotate);
                mHandler.postDelayed(authorDelayed,1000);
            }
        });

        bounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slidedown);
        slideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slideup);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        zoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin);

    }

    private void increase() {
        mAmount++;
        mTextViewAmount.setText(String.valueOf(mAmount));
    }

    private void decrease() {
        if (mAmount > 0) {
            mAmount--;
            mTextViewAmount.setText(String.valueOf(mAmount));
        }
    }

    private void addItem() {
        if (mEditTextName.getText().toString().trim().length() == 0 || mAmount == 0) {
            return;
        }
        String name = mEditTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount);
        mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());
        mEditTextName.getText().clear();
    }

    private Cursor getAllItems() {
            return mDatabase.query(
                    GroceryContract.GroceryEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
            );
    }
}

