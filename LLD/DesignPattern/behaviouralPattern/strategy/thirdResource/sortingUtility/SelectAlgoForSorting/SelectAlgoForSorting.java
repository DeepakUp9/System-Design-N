package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility.SelectAlgoForSorting;

import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.sortingUtility.sortingStrategies.SortingStrategies;

public class SelectAlgoForSorting {
    private SortingStrategies sortingStrategies;

    public SelectAlgoForSorting(SortingStrategies sortingStrategies){
        this.sortingStrategies = sortingStrategies;
    }

    public void sort(int arr[]){
        sortingStrategies.sort(arr);
    }


    
}
