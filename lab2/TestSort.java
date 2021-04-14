package lab2;
import java.util.Arrays;

import static lab2.SortRunningTimeSurvey.*;

public class TestSort {

    public static void main(String[] args) {
        int[] list = {5, 8, 9, 4, 2, 1, 3, 7, 6};
//        insertionSort(list.length, list);
//        selectionSort(list.length, list);
        quickSort(list.length, list);
        System.out.println(Arrays.toString(list));
    }

}
