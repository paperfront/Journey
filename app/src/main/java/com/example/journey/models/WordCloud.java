package com.example.journey.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class WordCloud {
    private int height = 300;
    private int width = 300;
    private float maxFontSize = 40;
    private int largestCount;
    private HashMap<String, Integer> wordCounts;


    public WordCloud(HashMap<String, Integer> wordCounts) {
        this.wordCounts = wordCounts;
        this.maxFontSize = getMaxFontSize();
    }

    private float getMaxFontSize() {
        int totalCount = 0;
        largestCount = 0;
        for (int count : wordCounts.values()) {
            totalCount += count;
            largestCount = count > largestCount ? count : largestCount;
        }
        return Math.min(maxFontSize, height / (totalCount / largestCount));
    }

    public Bitmap createBitmap() {
        List<Word> wordList = new ArrayList<>();
        for (String word : wordCounts.keySet()) {
            wordList.add(new Word(word, wordCounts.get(word), getWordSize(wordCounts.get(word))));
        }
        fitWords(wordList);
        Bitmap canvasBitmap =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(canvasBitmap);
        for (Word word : wordList) {
            canvas.drawText(word.getWord(), word.getWordRectangle().left, word.getWordRectangle().top, word.getWordPaint());
        }
        return canvasBitmap;
    }

    private float getWordSize(int wordCount) {
        float calculatedSize = maxFontSize * (wordCount / largestCount);
        return  calculatedSize;
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
