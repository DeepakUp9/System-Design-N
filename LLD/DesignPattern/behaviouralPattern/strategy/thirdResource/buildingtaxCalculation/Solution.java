package LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation;

import java.util.InputMismatchException;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.Tax.Tax;
import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy.EUROPETaxStrategy;
import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy.IndiaTaxStrategy;
import LLD.DesignPattern.behaviouralPattern.strategy.thirdResource.buildingtaxCalculation.TaxStrategy.USATaxStrategy;



public class Solution {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("Inter your region..");
        String str = s.next();

        Tax tax;
        if(str.equals("India")){
            tax = new Tax(new IndiaTaxStrategy());
        }else if(str.equals("USA")){
             tax = new Tax(new USATaxStrategy());
        }else if (str.equals("Europe")){
             tax = new Tax(new EUROPETaxStrategy());
        }else{
            throw new InputMismatchException("not applicable");
        }
        
        System.out.println(tax.taxCalculaton());
        System.out.println(tax.taxPolicies());
    }
}
