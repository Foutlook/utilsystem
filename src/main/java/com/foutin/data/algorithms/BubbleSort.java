package com.foutin.data.algorithms;

import java.util.Arrays;

/**
 * @description
 * @author xingkai.fan
 * @date 2022/7/25 15:00
 **/
public class BubbleSort {


    public static void main(String[] args) {

        int[] array = new int[]{2,3,9,7,1,5,8,6};
        int[] sort = sort(array);
        System.out.println(Arrays.toString(sort));

    }

    public static int[] sort(int[] array) {
        int length = array.length;
        if (length <= 1) {
            return array;
        }

        int temp;
        for (int i = 0; i < length; i++) {
            boolean sw = false;
            for (int j = 1; j < length - i; j++) {
                if (array[j-1] > array[j]) {
                    temp = array[j];
                    array[j] = array[j-1];
                    array[j-1] = temp;
                    sw = true;
                }
            }
            if (!sw) {
                break;
            }
        }

        return array;

    }
}
