package org.evcs.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaginatedResponse<T> {

    private int currentPage;
    private int perPage;
    private int totalPages;
    private int totalCount;
    @SerializedName("page") private List<T> mList;

    public List<T> getPage() {
        return mList;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
