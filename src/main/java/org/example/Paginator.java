package org.example;

import java.util.List;

import static java.lang.Math.min;

public class Paginator {
    private List<Listable> items;
    private int pageSize;

    public Paginator(List<Listable> items, int pageSize) {
        this.items = items;
        this.pageSize = pageSize;
    }

    public void listItems(String prompt, int pageNumber){
        int numberOfPages = (int) Math.ceil((double) items.size() / pageSize);
        pageNumber = min(pageNumber, numberOfPages);
        System.out.println("Page " + pageNumber + " of " + numberOfPages + " (" + pageSize + " items per page):");
        if(pageNumber == 0)
            return;
        int pageStart = (pageNumber-1) * pageSize;
        int pageEnd = pageStart + pageSize;
        int index = pageStart;
        while (index < items.size() && index < pageEnd) {
            System.out.println((index+1) + ". " + items.get(index));
            index++;
        }
        if(index < items.size()){
            System.out.println("To return to the next page, run the query as follows:");
            System.out.println(prompt + " " + (pageNumber+1));
        }
    }

}
