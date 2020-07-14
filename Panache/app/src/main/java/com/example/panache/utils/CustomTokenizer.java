/*
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * //            © Copyright 2019 JangleTech Systems Private Limited, Thane, India
 * //
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.example.panache.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;

import org.apache.commons.text.StringEscapeUtils;

public class CustomTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    private String tokens, addPostChar, addPreChar, preToken;
    private Tag tag;

    public CustomTokenizer(String tokens, String addPostChar, String addPreChar, String preToken, Tag tag) {
        this.tokens = tokens;
        this.addPostChar = addPostChar;
        this.addPreChar = addPreChar;
        this.preToken = preToken;
        this.tag = tag;
    }

    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;
        while (i > 1 && (!tokens.contains(text.charAt(i - 1) + "") && !preToken.contains(text.charAt(i - 1) + "")))
            i--;
        if (text.length() > 0 && i != cursor && tokens.contains(text.charAt(i - 1) + "")) {
            String searchTag = text.toString().substring(i, cursor);
            tag.tagSearch(searchTag, text.charAt(i - 1));
            if (StringEscapeUtils.escapeJava(searchTag).contains("\\u"))
                tag.escapedOnEmoji(text.toString().substring(0, i - 1) +
                        text.toString().substring(i, text.length()));
        }
        i = cursor;
        while (i > 0 && !tokens.contains(text.charAt(i - 1) + "")) i--;
        while (i < cursor && text.charAt(i) == ' ') i++;
        return i;
    }

    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();
        while (i < len) if (text.charAt(i) == ' ') return i;
        else i++;
        return len;
    }

    public CharSequence terminateToken(CharSequence text) {
        tag.selectedSuggestions(text.toString());
        if (text.charAt(0)=='#' && text.toString().contains("(") && text.toString().contains(")")) {
            text = text.toString().substring(0, text.toString().lastIndexOf("("));
        }
        if (tokens.contains(text.charAt(0) + "")) {
            text = text.subSequence(1, text.length());
        }
        int i = text.length();
        while (i > 0 && text.charAt(i - 1) == ' ') i--;
        if (i > 0 && text.charAt(i - 1) == ' ') return text;
        else {
            if (text instanceof Spanned) {
                SpannableString sp = new SpannableString(addPreChar + text + addPostChar);
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                        Object.class, sp, 0);
                return sp;
            } else return addPreChar + text + addPostChar;
        }
    }

    public interface Tag {
        void tagSearch(String search, char token);

        void escapedOnEmoji(String finalText);

        void selectedSuggestions(String selectedKey);
    }
}
