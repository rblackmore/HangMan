package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.controllers.HangManController;

import java.io.IOException;

public class MyActivity extends Activity {

    HangManController hmc;
    TextView your_word;
    TextView welcome_message;
    TextView txtWrongs;
    TextView txtGuessedLetters;
    AlertDialog.Builder endMessageBuilder;
    AlertDialog.Builder errorMessageBuilder;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        welcome_message = (TextView) findViewById(R.id.welcome_message);
        your_word = (TextView) findViewById(R.id.your_word);
        txtWrongs = (TextView) findViewById(R.id.intWrongs);
        txtGuessedLetters = (TextView) findViewById(R.id.lstGuessedLetters);

        your_word.setTextSize(20);
        welcome_message.setTextSize(25);


        try {
            hmc = new HangManController(getApplicationContext());
        } catch (IOException e) {

        }

        this.newGame();

    }

    public void newGame() {

            hmc.newGame();

            //set your_word TextView (this here should result in 1 underscore for each letter)
            String blankword = hmc.getStrBlankWord();
            your_word.setText(blankword);

            //set intWrongs TextView (this should be 0 here)
            String intWrongs = hmc.getWrong();
            txtWrongs.setText(intWrongs);

            String strGuessedLetters = hmc.getStrGuessedLetters();
            txtGuessedLetters.setText(strGuessedLetters);
    }

    public void guess(View view) {

        EditText searchLetter = (EditText) findViewById(R.id.edit_character);
        String strSearchLetter = searchLetter.getText().toString();

        searchLetter.setText("");

        if (strSearchLetter.length() < 1 || strSearchLetter.length() > 1) {
            createError();

            AlertDialog msgError = errorMessageBuilder.create();
            msgError.show();
            return;

        }

        hmc.searchLetter(strSearchLetter);

        String blankword = hmc.getStrBlankWord();
        your_word.setText(blankword);
        String intWrong = hmc.getWrong();
        txtWrongs.setText(intWrong);
        String strGuessedLetters = hmc.getStrGuessedLetters();
        txtGuessedLetters.setText(strGuessedLetters);
        //Check for win/lose state
        if (hmc.testWin()) {
            //you win message

            createWinDialog();

            AlertDialog msgYouWin = endMessageBuilder.create();
            msgYouWin.show();


        } else if (hmc.testLose()) {
            //you lose message
            createLoseDialog();

            AlertDialog msgYouLose = endMessageBuilder.create();
            msgYouLose.show();
        }

    }

    private void createWinDialog() {
        endMessageBuilder = new AlertDialog.Builder(this);
        //set Dialog Title
        endMessageBuilder.setTitle("YOU WIN!!!");
        //set dialog message
        endMessageBuilder
                .setMessage("Would you like to Play again?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        newGame();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyActivity.this.finish();
                    }
                }) ;
    }

    private void createLoseDialog() {
        endMessageBuilder = new AlertDialog.Builder(this);
        //set Dialog Title
        endMessageBuilder.setTitle("YOU LOSE :(, the word was: " + hmc.getStrGuessWord());
        //set dialog message
        endMessageBuilder
                .setMessage("Would you like to Play again?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        newGame();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyActivity.this.finish();
                    }
                }) ;
    }

    private void createError() {
        errorMessageBuilder = new AlertDialog.Builder(this);
        //set Dialog Title
        errorMessageBuilder.setTitle("Invalid Input");
        //set dialog message
        errorMessageBuilder
                .setMessage("You must input only 1 character")
                .setCancelable(true);
    }


}
