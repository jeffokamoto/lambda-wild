package com.gaminggrunts;

import java.util.ArrayList;
import java.util.List;

public class Roll {

    private List<Group> groupList;
    private int result;
    private String rollResult;

    public Roll() {
        this.groupList = new ArrayList<>();
        this.result = 0;
        this.rollResult = "";
    }

    public int getResult() {
        return result;
    }

    public Roll setResult(final int result) {
        this.result = result;
        return this;
    }

    public String getRollResult() {
        return rollResult;
    }

    public Roll setRollResult(final String rollResult) {
        this.rollResult = rollResult;
        return this;
    }

    public Roll add(final Group group) {
        groupList.add(group);
        return this;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void calculate() {
        for (int i = 0; i < groupList.size(); i++) {
            Group group = groupList.get(i);
            group.calculate();
            String resultText = group.getText();
            // Handle leading plus sign in first group (but leave leading minus)
            if (i == 0) {
                resultText = resultText.replaceAll("^\\s*\\+\\s*", "");
            } else {
                resultText = " " + resultText;
            }
            setResult(getResult() + group.getResult());
            setRollResult(getRollResult() + resultText);
        }
    }

    @Override
    public String toString() {
        return rollResult;
    }
}
