package com.example.journey.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import timber.log.Timber;

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
        float calculatedSize = maxFontSize * ((float) wordCount / largestCount);
        return calculatedSize;
    }


    private void fitWords(List<Word> wordList) {
        if (wordList.isEmpty()) {
            return;
        }

        int currentX = wordList.get(0).getX();
        int currentY = wordList.get(0).getY();

        for (int i = 1; i < wordList.size(); i++) {

            Word currentWord = wordList.get(i);
            Rect currentRect = currentWord.getWordRectangle();
            if (currentX + currentRect.width() > width) {
                currentRect.offsetTo(0, currentY);
                currentY += currentRect.height();
                currentX = currentRect.width();
            } else {
                currentRect.offsetTo(currentX, currentRect.height());
                currentX += currentRect.width();
            }
            Rect intersect = intersectingRectangle(currentWord, wordList);
            if (intersect == null) {
                continue;
            } else {
                while (intersect != null) {
                    /*
                    int newX = intersect.right;
                    if (newX + currentRect.width() > width) {
                        int newY = intersect.bottom;
                        if (newY + currentRect.height() > height) {
                            // Word does not fit anywhere
                            Timber.e("Failed to correctly populate word cloud.");
                            return;
                        } else {
                            currentRect.offsetTo(currentWord.getX(), newY);
                        }
                    } else {
                        currentRect.offsetTo(newX, currentWord.getY());
                    }
                    intersect = intersectingRectangle(currentWord, wordList);
                     */

                    int diffY = Math.abs(intersect.bottom - currentRect.top);
                    currentRect.offsetTo(currentRect.left,
                            currentRect.top + diffY);
                    intersect = intersectingRectangle(currentWord, wordList);


                }
            }
        }
    }

    private Rect intersectingRectangle(Word currentWord, List<Word> allWords) {
        for (Word word : allWords) {
            if (!currentWord.equals(word) && Rect.intersects(currentWord.getWordRectangle(), word.getWordRectangle())) {
                return word.getWordRectangle();
            }
        }
        return null;
    }


}
