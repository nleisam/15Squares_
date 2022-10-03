package com.example.a15squares;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

import java.util.Random;

public class Game extends AppCompatActivity {

    private int clearX=3; //available x/y coordinates
    private int clearY=3;
    private RelativeLayout cluster;
    private Button[][] buttons;
    private int[] squares;
    private Button buttonReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //List of methods that make the game work
        loadButtons();
        loadNumbers();
        createNumbers();
        showData();
    }

    /*
    void showData() displays the init layout
     */
    private void showData() {
        clearX = 3;
        clearY = 3;
        for (int i = 0; i < cluster.getChildCount() - 1 ; i++) {
            buttons[i/4][i%4].setText(String.valueOf(squares[i]));
            buttons[i/4][i%4].setBackgroundResource(android.R.drawable.btn_default);
        }

        buttons[clearX][clearY].setText("");
        buttons[clearX][clearY].setBackgroundColor(ContextCompat.getColor(this,R.color.EmptyButton));
    }

    /*
    createNumbers() generates random numbers on the squares
     */
    private void createNumbers() {
        int n = 15;

        Random random = new Random();
        while (n > 1) {
            int randomNum = random.nextInt(n--);
            int value = squares[randomNum];
            squares[randomNum] = squares[n];
            squares[n] = value;
        }
        if (!isWinnable())
            createNumbers();
    }

    /*
    isWinnable() checks to see if the layout of numbers is solvable.
     */
    private boolean isWinnable() {
        int countSwitches = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < i; j++) {
                if (squares[j] > squares[i])
                    countSwitches++;
            }
        }
        return countSwitches % 2 == 0;
    }

    /*
    loadNumbers() ensures 15 numbers are created
     */
    private void loadNumbers() {
        squares = new int[16];
        for (int i = 0; i < cluster.getChildCount() - 1; i++) {
            squares[i] = i + 1;
        }
    }

    /*
    loadButtons(): loads squares
     */
    private void loadButtons() {
        cluster=findViewById(R.id.group);
        buttonReset = findViewById(R.id.button_reset);
        buttons = new Button[4][4];

        for (int i = 0; i < cluster.getChildCount(); i++) {
            buttons[i/4][i%4] = (Button) cluster.getChildAt(i);
        }
        buttonReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createNumbers();
                showData();
            }
        });
    }

    public void buttonClick(View view) {
        Button button = (Button) view;
        int x = button.getTag().toString().charAt(0)-'0';
        int y = button.getTag().toString().charAt(1)-'0';

        if ((Math.abs(clearX - x) == 1 && clearY == y) || (Math.abs(clearY - y) == 1 && clearX == x)) {
            buttons[clearX][clearY].setText(button.getText().toString());
            buttons[clearX][clearY].setBackgroundResource(android.R.drawable.btn_default);
            button.setBackgroundColor(ContextCompat.getColor(this,R.color.EmptyButton));
            clearX = x;
            clearY = y;
            checkWin();
        }
    }

    /*
    checkWin() checks to see if the numbers are in order
     */
    private void checkWin() {
        boolean hasWon = false;
        if (clearX == 3 && clearY == 3) {
            for (int i = 0; i < cluster.getChildCount() - 1; i++) {
                if (buttons[i/4][i%4].getText().toString().equals(String.valueOf(i+1))) {
                    hasWon = true;
                } else {
                    hasWon = false;
                    break;
                }
            }
        }
        if (hasWon) {
        for (int i = 0; i < cluster.getChildCount(); i++) {
            buttons[i/4][i%4].setClickable(false);
        }
            buttonReset.setClickable(false);
        }
    }
}