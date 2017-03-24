package com.example.may.msocial.models;



import java.util.Comparator;


public class ProblemsSorting implements Comparator<MsRow> {


        public int compare(MsRow one, MsRow another){
            int returnVal = 0;

            if(one.getNqueries() < another.getNqueries()){
                returnVal =  - 1;
            }else if(one.getNqueries() > another.getNqueries()){
                returnVal =  1;
            }else if(one.getNqueries() == another.getNqueries()){
                returnVal =  0;
            }
            return returnVal;

        }

}
