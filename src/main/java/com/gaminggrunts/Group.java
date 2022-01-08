package com.gaminggrunts;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private List<Die> dieList;
    private int plusminus;
    private int result;
    private String text;

    public Group() {
        this.dieList = new ArrayList<>();
        this.plusminus = 1;
        this.result = 0;
        this.text = "";
    }

    public Group(final int modifier) {
        this.dieList = new ArrayList<>();
        this.plusminus = modifier;
        this.result = 0;
        this.text = "";
    }

    public Group addDieList(final List<Die> list) {
        this.dieList.addAll(list);
        return this;
    }

    public Group addDie(final Die die) {
        this.dieList.add(die);
        return this;
    }

    public int getResult() {
        return this.result;
    }

    public Group setResult(final int result) {
        this.result = result;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public Group setText(final String text) {
        this.text = text;
        return this;
    }

    public int getPlusminus() {
        return this.plusminus;
    }

    public List<Die> getDieList() {
        return this.dieList;
    }

    public void calculate() {
        int total = 0;
        ArrayList<String> list = new ArrayList<>();
        for (Die die: dieList) {
            die.roll();
            total += die.getResult();
            list.add(die.getRoll());
        }
        total *= this.plusminus;
        this.setResult(total);
        this.setText((this.getPlusminus() == 1 ? "+ " : "- ") + String.join(" ", list));
    }

    @Override
    public String toString() {
        return this.text;
    }

}
