package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility.sortingStrategies;

import java.util.Arrays;

public class QuickSort implements SortingStrategies{

    @Override
    public void sort(int[] arr) {
        System.out.println("array sorting using QuickSort");
        Arrays.sort(arr);    
    }
    
}
