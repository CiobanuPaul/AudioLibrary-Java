package org.example;

import java.util.List;

import static java.lang.Math.min;

/**
 * Is used to list to stdout {@code Listable} items in a paginated way.
 */
public class Paginator {
    private List<Listable> items;
    private int pageSize;

    /**
     *
     * @param items The items to be listed.
     * @param pageSize How many items to be printed on a page.
     */
    public Paginator(List<Listable> items, int pageSize) {
        this.items = items;
        this.pageSize = pageSize;
    }

    /**
     * Lists the items at the page number given.
     * @param prompt The command entered with its arguments(except the pageNumber argument). Used to tell the user what command to enter in order to view the next page.
     * @param pageNumber
     */
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
