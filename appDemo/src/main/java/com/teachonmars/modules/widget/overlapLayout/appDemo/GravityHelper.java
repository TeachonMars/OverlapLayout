package com.teachonmars.modules.widget.overlapLayout.appDemo;

import android.view.Gravity;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.Gravity.NO_GRAVITY;

class GravityHelper {
    private static ArrayList<Integer> valueList;
    private static ArrayList<String>  stringList;

    static {
        valueList = new ArrayList<>(Arrays.asList(NO_GRAVITY,
                Gravity.RIGHT, Gravity.LEFT,
                Gravity.START, Gravity.END,
                Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL, Gravity.CENTER,
                Gravity.TOP, Gravity.BOTTOM));

        stringList = new ArrayList<>(Arrays.asList("noGravity",
                "right", "left",
                "start", "end",
                "centerVertical", "centerHorizontal", "center",
                "top", "bottom"));
    }

    public static int positionOf(Integer gravity) {
        return valueList.indexOf(gravity);
    }

    public static ArrayList<String> getSringList() {
        return stringList;
    }

    public static ArrayList<Integer> getValueList() {
        return valueList;
    }

}
