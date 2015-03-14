package com.plataformaam.mobile.clientefinal.helpers;

import java.util.ArrayList;
import java.util.List;

public class MyFilter {

    public MyFilter() {
        filter = new ArrayList<MyFilterItem>();
    }

    private List<MyFilterItem> filter;

    /**
     * @return the filter
     */
    public List<MyFilterItem> getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(List<MyFilterItem> filter) {
        this.filter = filter;
    }

    public void addFilter(MyFilterItem f){
        filter.add(f);
    }



}
