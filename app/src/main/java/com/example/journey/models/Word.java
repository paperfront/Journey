package com.example.journey.models;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Word {

    public static final int BASE_FONT_SIZE = 10;
    public static final int FONT_ADDER = 2;
    private String word;
    private int wordCount;
    private int yOffset;
    private float wordSize;
    private Paint wordPaint;
    private Rect wordRectangle;

    public Word(String word, int wordCount, float wordSize) {
        this.word = word;
        this.wordCount = wordCount;
        this.wordSize = wordSize;
        this.wordRectangle = new Rect();
        calculateBoundingRectangle();
    }

    private void calculateBoundingRectangle() {
        this.wordPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        wordPaint.setTextSize(wordSize);
        wordPaint.setStyle(Paint.Style.FILL);
        wordPaint.setTextAlign(Paint.Align.LEFT);
        Random rnd = new Random();
        wordPaint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        wordPaint.getTextBounds(word, 0, word.length(), wordRectangle);
        this.yOffset = Math.abs(wordRectangle.top);
        wordRectangle.offsetTo(0, 0);
    }

    public String getWord() {
        return word;
    }

    public int getY() {
        return wordRectangle.top + yOffset;
    }

    public int getX() {
        return wordRectangle.left;
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

    public void setTextSize(float textSize) {
        this.wordSize = textSize;
        this.wordPaint.setTextSize(this.wordSize);
        this.wordPaint.getTextBounds(word, 0, word.length(), wordRectangle);
        wordRectangle.offsetTo(0, wordRectangle.height());
    }
}
