package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility;

import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility.SelectAlgoForSorting.SelectAlgoForSorting;
import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility.sortingStrategies.QuickSort;
import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility.sortingStrategies.BubblerSort;
import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility.SelectAlgoForSorting.HighTimeComplecity;
import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility.SelectAlgoForSorting.LowTimeComplexity;
public class Solution {
    public static void main(String[] args) {
        SelectAlgoForSorting quickSorting = new HighTimeComplecity(new QuickSort());
        int arr[] = {90, 121, -1, 323 , 4 ,2, 4, 0};
        //quickSorting.sort(arr);
        for(int elem:arr) System.out.print(elem + " ");

        SelectAlgoForSorting bubbleSort = new LowTimeComplexity(new BubblerSort());
        bubbleSort.sort(arr);
         for(int elem:arr) System.out.print(elem + " ");

    }
}
