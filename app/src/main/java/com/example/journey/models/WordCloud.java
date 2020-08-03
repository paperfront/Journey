package com.example.journey.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import timber.log.Timber;

public class WordCloud {
    private int height = 250;
    private int width = 300;
    private float maxFontSize = 40;
    private static final int paddingX = 7;
    private static final int paddingY = 7;
    private static final int numWords = 15;
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
        return 40;//Math.min(maxFontSize, height / (totalCount / largestCount));
    }

    public Bitmap createBitmap() {

        wordCounts = getTopNWords(numWords, wordCounts);

        List<Word> wordList = new ArrayList<>();

        List<String> wordKeys = new ArrayList<>(wordCounts.keySet());
        Collections.shuffle(wordKeys);
        for (String word : wordKeys) {
            wordList.add(new Word(word, wordCounts.get(word), getWordSize(wordCounts.get(word))));
        }
        fitWordsEnhanced(wordList);
        Bitmap canvasBitmap =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(canvasBitmap);
        for (Word word : wordList) {
            canvas.drawText(word.getWord(), word.getX(), word.getY(), word.getWordPaint());
        }
        return canvasBitmap;
    }

    private HashMap<String, Integer> getTopNWords(int n, HashMap<String, Integer> wordCounts) {
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(wordCounts.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        int counter = 0;
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            if (counter >= n) {
                break;
            }
            temp.put(aa.getKey(), aa.getValue());
            counter += 1;
        }
        return temp;
    }

    private float getWordSize(int wordCount) {
        float calculatedSize = maxFontSize * ((float) wordCount / largestCount);
        return calculatedSize;
    }


    private void fitWords(List<Word> wordList) {
        if (wordList.isEmpty()) {
            return;
        }

        int currentX = wordList.get(0).getWordRectangle().width();
        int currentY = wordList.get(0).getWordRectangle().height();

        for (int i = 1; i < wordList.size(); i++) {

            Word currentWord = wordList.get(i);
            Rect currentRect = currentWord.getWordRectangle();
            if (currentX + currentRect.width() + paddingX > width) {
                currentRect.offsetTo(0, currentY + paddingY);
                currentY += currentRect.height() + paddingY;
                currentX = currentRect.width();
            } else {
                currentRect.offsetTo(currentX + paddingX, currentRect.top);
                currentX += currentRect.width() + paddingX;
            }

            Rect intersect = intersectingRectangle(currentWord, wordList);
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
                currentRect.offsetTo(currentRect.left,
                        currentRect.top + paddingY);
        }
    }

    private void fitWordsEnhanced(List<Word> wordList) {
        if (wordList.isEmpty()) {
            return;
        }
        int placed = 0;
        int attempts = 0;
        while (placed < wordList.size() && attempts < 15) {
            Word currentWord = wordList.get(placed);
            Rect currentRect = currentWord.getWordRectangle();
            Point testPoint = getRandomCanvasPoint();
            currentRect.offsetTo(testPoint.x, testPoint.y);
            if (isOnScreen(currentRect) && intersectingRectangle(currentWord, wordList) == null) {
                placed += 1;
                attempts = 0;
            }
        }
        if (placed != wordList.size()) {
            Timber.e("Failed to properly place all words.");
        }
    }

    private Point getRandomCanvasPoint() {
        int x = new Random().nextInt(width + 1);
        int y = new Random().nextInt(height + 1);
        return new Point(x, y);
    }

    private boolean isOnScreen(Rect rect) {
        return rect.right < width && rect.bottom < height;
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
