package com.mgtj.airadio.base.utils.soundpool;

public class SoundResource {

    private int id = 0;
    private boolean loadedComplete = false;

    public SoundResource(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLoadedComplete() {
        return this.loadedComplete;
    }

    public void setLoadedComplete(boolean loadedComplete) {
        this.loadedComplete = loadedComplete;
    }
}
