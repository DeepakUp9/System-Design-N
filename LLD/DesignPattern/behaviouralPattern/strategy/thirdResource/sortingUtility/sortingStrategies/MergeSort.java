package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility.sortingStrategies;

import java.util.Arrays;

public class MergeSort implements SortingStrategies{

    @Override
    public void sort(int[] arr) {
        System.out.println("array sorting using MergeSort");
        Arrays.sort(arr);    
    }
    
}
