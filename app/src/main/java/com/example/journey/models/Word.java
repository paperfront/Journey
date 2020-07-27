package com.example.journey.models;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class Word {

    private String word;
    private int wordCount;
    private float wordSize;
    private Paint wordPaint;
    private Rect wordRectangle;


    public static final int BASE_FONT_SIZE = 10;
    public static final int FONT_ADDER = 2;

    public Word(String word, int wordCount) {
        this.word = word;
        this.wordCount = wordCount;
        this.wordSize = BASE_FONT_SIZE + (wordCount * FONT_ADDER);
        this.wordRectangle = new Rect();
        calculateBoundingRectangle();
    }

    private void calculateBoundingRectangle() {
        this.wordPaint = new Paint();
        wordPaint.setTextSize(wordSize);
        wordPaint.setStyle(Paint.Style.FILL);
        wordPaint.setTextAlign(Paint.Align.LEFT);
        wordPaint.getTextBounds(word, 0, word.length(), wordRectangle);
        wordRectangle.offsetTo(0, 0);
    }

    public String getWord() {
        return word;
    }

    public int getWordCount() {
        return wordCount;
    }

    public float getWordSize() {
        return wordSize;
    }

    public Paint getWordPaint() {
        return wordPaint;
    }

    public Rect getWordRectangle() {
        return wordRectangle;
    }
}
