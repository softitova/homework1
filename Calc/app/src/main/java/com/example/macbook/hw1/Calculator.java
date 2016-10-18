package com.example.macbook.hw1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.StringTokenizer;

public class Calculator extends AppCompatActivity {

    private TextView result;
    static String lexem = "";
    private View.OnClickListener onClickListener;
    static boolean f = false;

    private static final String KEY_OUTPUT_TEXT = "output_text";
    CharSequence outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        result = (TextView) findViewById(R.id.result);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.d0:
                        lexem += "0";
                        break;
                    case R.id.d1:
                        lexem += "1";
                        break;
                    case R.id.d2:
                        lexem += "2";
                        break;
                    case R.id.d3:
                        lexem += "3";
                        break;
                    case R.id.d4:
                        lexem += "4";
                        break;
                    case R.id.d5:
                        lexem += "5";
                        break;
                    case R.id.d6:
                        lexem += "6";
                        break;
                    case R.id.d7:
                        lexem += "7";
                        break;
                    case R.id.d8:
                        lexem += "8";
                        break;
                    case R.id.d9:
                        lexem += "9";
                        break;
                    case R.id.add:
                        lexem += " + ";
                        break;
                    case R.id.sub:
                        lexem += " - ";
                        break;
                    case R.id.mul:
                        lexem += " * ";
                        break;
                    case R.id.div:
                        lexem += " / ";
                        break;
                    case R.id.eqv:
                        try {
                            double ans = calculate(lexem);
                            double res = new BigDecimal(ans).setScale(5, RoundingMode.HALF_UP).doubleValue();
                            lexem = (res - (long) res == 0) ? Long.toString((long) res) : Double.toString(res);
                        } catch (Exception e) {
                            lexem = "INVALID OPERATION";
                            f = true;
                        }
                        System.out.println(lexem);
                        break;
                    default:
                        lexem = "";
                        break;
                }
                result.setText(lexem);
                if (f) {
                    lexem = "";
                    f = false;
                }
            }
        };

        for (int i : new int[]{R.id.d0, R.id.d1, R.id.d2, R.id.d3, R.id.d4, R.id.d5, R.id.d6, R.id.d7, R.id.d8, R.id.d9,
                R.id.clear, R.id.mul, R.id.sub, R.id.div, R.id.eqv, R.id.add}) {
            findViewById(i).setOnClickListener(onClickListener);
        }


        if (savedInstanceState != null) {
            CharSequence x = savedInstanceState.getCharSequence(KEY_OUTPUT_TEXT);
            System.out.println("getting saved text");
            System.out.println(x);
            if (x != null) {
                System.out.println(x);
                result.setText(x);
            }
        }

    }

    private String expression;
    private int pos;

    public double calculate(String d) {
        pos = 0;
        expression = d.toLowerCase();
        try {
            return plusMinus();
        } catch (Exception e) {
            throw e;
        }
    }

    private void skip() {
        while (pos < expression.length() && Character.isWhitespace(expression.charAt(pos))) {
            pos++;
        }
    }

    private double plusMinus() {
        try {
            double value = mulDiv();
            skip();
            while (pos < expression.length()) {
                char op = expression.charAt(pos);
                if (op != '+' && op != '-') {
                    break;
                }
                pos++;
                if (op == '+') {
                    value += mulDiv();
                } else {
                    value -= mulDiv();
                }
                skip();
            }
            return value;
        } catch (Exception e) {
            throw e;
        }
    }

    private double mulDiv() {
        double value = power();
        skip();
        while (pos < expression.length()) {
            char op = expression.charAt(pos);
            if (op != '*' && op != '/') {
                break;
            }
            pos++;
            if (op == '*') {
                value *= power();
            } else {
                double ppp = power();
                if (ppp == 0) {
                    throw new NumberFormatException();
                } else {
                    value /= ppp;
                }
            }
            skip();
        }
        return value;
    }

    private double power() {
        ArrayList<Double> d = new ArrayList<>();
        d.add(unaryOperation());
        skip();
        while (pos < expression.length()) {
            char op = expression.charAt(pos);
            if (op != '^') {
                break;
            }
            pos++;
            d.add(unaryOperation());
            skip();
        }
        double value = 1;
        for (int i = d.size() - 1; i >= 0; i--) {
            value = Math.pow(d.get(i), value);
        }
        return value;
    }

    private int r;

    private double unaryOperation() {
        skip();

        switch (expression.charAt(pos)) {
            case '+':
                pos++;
                return power();
            case '-':
                pos++;
                return -power();
            default:
                r = pos;
                while (r < expression.length()) {
                    int x = isNumberPart(expression.charAt(r));
                    if (x == 0) {
                        break;
                    }
                    r += x;
                }
                double res = Double.parseDouble(expression.substring(pos, r));
                pos = r;
                return res;
        }
    }

    private int isNumberPart(char x) {
        if (Character.isDigit(x) || x == '.') {
            return 1;
        }
        if (x == 'e') {
            return 2;
        }
        return 0;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("saved\n");
        outState.putCharSequence(KEY_OUTPUT_TEXT, result.getText());
    }

}
