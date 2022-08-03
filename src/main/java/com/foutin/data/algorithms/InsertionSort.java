package com.foutin.data.algorithms;

import java.util.Arrays;

/**
 * @author xingkai.fan
 * @description
 * @date 2022/8/3
 */
public class InsertionSort {

    public static void main(String[] args) {
        int[] array = new int[]{2, 3, 9, 7, 1, 5, 8, 6};
        int[] sort = sort(array);
        System.out.println(Arrays.toString(sort));
    }

    public static int[] sort(int[] array) {
        int length = array.length;
        if (length <= 1) {
            return array;
        }
        for (int i = 1; i < length; i++) {
            int j = i - 1;
            int value = array[i];
            for (; j >= 0; j--) {
                if (array[j] > value) {
                    array[j + 1] = array[j];
                } else {
                    break;
                }
            }
            array[j+1] = value;
        }

        return array;
    }
}
