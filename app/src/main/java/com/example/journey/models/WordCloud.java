package com.example.journey.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordCloud {
    private int height = 300;
    private int width = 300;
    private int maxFontSize = 40;
    private int minFontSize = Word.BASE_FONT_SIZE;
    private HashMap<String, Integer> wordCounts;


    public WordCloud(HashMap<String, Integer> wordCounts) {
        this.wordCounts = wordCounts;
    }

    public Bitmap createBitmap() {
        List<Word> wordList = new ArrayList<>();
        for (String word : wordCounts.keySet()) {
            wordList.add(new Word(word, wordCounts.get(word)));
        }
        fitWords(wordList);
        Bitmap canvasBitmap =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(canvasBitmap);
        for (Word word : wordList) {
            canvas.drawText(word.getWord(), word.getWordRectangle().left, word.getWordRectangle().top, word.getWordPaint());
        }
        return canvasBitmap;
        
    }


    private void fitWords(List<Word> wordList) {
        if (wordList.isEmpty()) {
            return;
        }
        for (int i = 1; i < wordList.size(); i++) {
            wordList.get(i).getWordRectangle().offsetTo(0, wordList.get(i - 1).getWordRectangle().bottom);
        }
    }

}
