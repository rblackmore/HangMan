package com.example.controllers;

import android.content.Context;
import android.content.res.AssetManager;
import android.provider.UserDictionary;

import java.io.*;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ryan Blackmore
 * @Date: 29/11/12
 * @Time: 3:26 PM
 * @Version: 1.0
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Description:
 */
public class HangManController {

    private List<String> wordList;
    private String strGuessWord;
    private String strBlankWord;
    private List<String> lstGuessLetters;
    private List<Boolean> foundLetters;
    private BufferedReader br = null;
    private int wrong;
    private String strGuessedLetters;



    private final int MAX_WRONGS = 8;

    public HangManController(Context myContext) throws IOException {
        //initialize new arralylists to store already guess letters, and a list of possible words

        wordList = new ArrayList<String>();
        foundLetters = new ArrayList<Boolean>();
        lstGuessLetters = new ArrayList<String>();

        //Create an AssetManager and an inputstream to grab the asset "Words.csv"
        AssetManager mngr = myContext.getAssets();
        InputStream stream = mngr.open("Words.csv");

        //create a buffered reader base don the stream created above
        br = new BufferedReader(new InputStreamReader(stream));

        //Read the first line in the bufferedreader and store it in a string
        String lineWord = br.readLine();

        //loop through ever line in the buffered reader adding each word to
        //an Arraylist until the bufferedreader finds a blank line(null)
        while (lineWord != null) {
            wordList.add(lineWord);
            lineWord = br.readLine();
        }
        //close the bufferedreader
        br.close();


        newGame();

    }

    public void newGame() {

        //clear previous game
        wrong = 0;
        strGuessedLetters = "";
        if (foundLetters.size() > 0) {
            foundLetters.clear();
        }

        if (lstGuessLetters.size() > 0) {
            lstGuessLetters.clear();
        }



        strBlankWord = "";
        strGuessWord = "";

        //set up a new game

        pickWord();
        setFoundLetters();
        setStrBlankWord();


    }

    public void pickWord() {
        //pick a random number from 0 to wordList array size
        int wordPicker = (int) (Math.random() * wordList.size());

        strGuessWord = wordList.get(wordPicker);

        lstGuessLetters = new ArrayList<String>(Arrays.asList(strGuessWord.split("")));
        lstGuessLetters.remove(0);

        //return strGuessWord;
    }

    public void setFoundLetters() {


        for (int i = 0; i < strGuessWord.length(); i++) {
            this.foundLetters.add(false);
        }
    }

    public String getStrBlankWord() {
        return strBlankWord;
    }

    public void setStrBlankWord() {

         int j = 0;

        strBlankWord = new String();

         for (String tempChar : lstGuessLetters) {

             if (foundLetters.get(j)) {
                strBlankWord += tempChar;
             } else {
                strBlankWord += "_ ";
             }

             j++;
         }

    }

    public String getStrGuessWord() {
        return strGuessWord;
    }

    public void searchLetter(String letter) {
        //searches the string for a letter
        //sets the foundeletters boolean list to true if the letter was found
        Boolean found = false;

        //for each letter in the word
        int k = 0;

        for (String tempChar : lstGuessLetters) {

            if (tempChar.equalsIgnoreCase(letter)) {
                foundLetters.set(k, true);
                found = true;
            }
            k++;
        }

        strGuessedLetters += letter;

        setStrBlankWord();

        if (found == false) {
            wrong += 1;
        }

    }

    public boolean testWin() {

        for (boolean tempBln : foundLetters) {
            if (tempBln == false) {
                return false;
            }

        }
        return true;
    }

    public boolean testLose() {

        if (wrong == MAX_WRONGS) {
            return true;
        }
        return false;
    }

    public String getWrong() {
        return String.valueOf(wrong);
    }

    public String getStrGuessedLetters() {
        return strGuessedLetters;
    }
}

