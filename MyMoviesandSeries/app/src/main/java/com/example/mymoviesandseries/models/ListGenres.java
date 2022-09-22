package com.example.mymoviesandseries.models;

public enum ListGenres {
    Action ("Action", 0),
    Adventure ("Adventure", 1),
    Animation ("Animation", 2),
    Biography ("Biography", 3),
    Crime ("Crime", 4),
    Comedy ("comedy", 5),
    Drama ("Drama", 6),
    Fantasy ("Fantasy", 7),
    History ("History", 8),
    Horror ("Horror", 9),
    Mistery ("Mistery", 10),
    Romance ("Romance", 11),
    SciFi ("Sci-Fi", 12),
    Sports ("Sports", 13),
    Thriller ("Thriller", 14),
    Otro ("Otro", 15);

    private final String text;
    private final int index;

    ListGenres(String text, int index) {
        this.text = text;
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public int getIndex() {
        return index;
    }

    public static String[] getNames() {
        String[] result = new String[ListGenres.values().length];
        for (ListGenres genre : ListGenres.values()) {
            result[genre.ordinal()] = genre.text;
        }
        return result;
    }

    public static int getIndex(String nameGenre) {
        int index = 0;
        for (ListGenres genre : ListGenres.values()) {
            if(nameGenre.equals(genre.text))
                index = genre.index;
        }
        return index;
    }

}
