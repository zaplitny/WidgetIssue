package com.chronometer.widgetissue;

class CustomTimer {

    private long id;
    private long start;
    private int status = 0; // 1 for stopped

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    long getStart() {
        return start;
    }

    void setStart(long start) {
        this.start = start;
    }

    int getStatus() {
        return status;
    }

    void setStatus(int status) {
        this.status = status;
    }
}
