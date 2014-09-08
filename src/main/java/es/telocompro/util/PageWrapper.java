package es.telocompro.util;

import java.util.ArrayList;
import java.util.List;

import static es.telocompro.util.Constant.ITEMS_PER_PAGE;

import org.springframework.data.domain.Page;

public class PageWrapper<T> {
    private Page<T> page;
    private List<PageItem> items;
    private int currentNumber;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PageWrapper(Page<T> page, String url){
        this.page = page;
        this.url = url;
        items = new ArrayList<PageItem>();

        currentNumber = page.getNumber() + 1; //start from 1 to match page.page

        int start, size;
        if (page.getTotalPages() <= ITEMS_PER_PAGE){
            start = 1;
            size = page.getTotalPages();
        } else {
            if (currentNumber <= ITEMS_PER_PAGE - ITEMS_PER_PAGE/2){
                start = 1;
                size = ITEMS_PER_PAGE;
            } else if (currentNumber >= page.getTotalPages() - ITEMS_PER_PAGE/2){
                start = page.getTotalPages() - ITEMS_PER_PAGE + 1;
                size = ITEMS_PER_PAGE;
            } else {
                start = currentNumber - ITEMS_PER_PAGE/2;
                size = ITEMS_PER_PAGE;
            }
        }

        for (int i = 0; i<size; i++){
            items.add(new PageItem(start+i, (start+i)==currentNumber));
        }
    }

    public List<PageItem> getItems(){
        return items;
    }

    public int getNumber(){
        return currentNumber;
    }

    public List<T> getContent(){
        return page.getContent();
    }

    public int getSize(){
        return page.getSize();
    }

    public int getTotalPages(){
        return page.getTotalPages();
    }

    public boolean isFirstPage(){
//        return page.isFirstPage();
    	return page.isFirst();
    }

    public boolean isLastPage(){
//        return page.isLastPage();
    	return page.isLast();
    }

    public boolean isHasPreviousPage(){
//        return page.hasPreviousPage();
    	return page.hasPrevious();
    }

    public boolean isHasNextPage(){
//        return page.hasNextPage();
    	return page.hasNext();
    }

    public class PageItem {
        private int number;
        private boolean current;
        public PageItem(int number, boolean current){
            this.number = number;
            this.current = current;
        }

        public int getNumber(){
            return this.number;
        }

        public boolean isCurrent(){
            return this.current;
        }
    }
}
