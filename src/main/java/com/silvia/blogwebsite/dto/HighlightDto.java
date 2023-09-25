package com.silvia.blogwebsite.dto;

public class HighlightDto {
    private int[] idList;
    private boolean status;

    public int[] getIdList() {
        return idList;
    }

    public void setIdList(int[] idList) {
        this.idList = idList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public HighlightDto(int[] idList, boolean status){
        this.idList = idList;
        this.status = status;
    }

    public HighlightDto(){}

}
