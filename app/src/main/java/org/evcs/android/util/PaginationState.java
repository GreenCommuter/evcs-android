package org.evcs.android.util;

public class PaginationState {
    private int page = 1;
    private int totalPages;

    public boolean pagesLeft() {
        return page < totalPages;
    }

    public void updateTotal(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isOnFirstPage() {
        return page == 1;
    }

    public int getPage() {
        return page;
    }

    public void advancePage() {
        page++;
    }
}