package com.csci6461.gw.simulator.ui;

import javafx.beans.property.SimpleStringProperty;

public class CacheTable {
    private final SimpleStringProperty cacheIndex;
    private final SimpleStringProperty cacheTag;
    private final SimpleStringProperty cacheOffset;
    private final SimpleStringProperty cacheBinary;

    public CacheTable(String cacheIndex, String cacheTag, String cacheOffset, String cacheBinary){
        this.cacheIndex = new SimpleStringProperty(cacheIndex);
        this.cacheTag = new SimpleStringProperty(cacheTag);
        this.cacheOffset = new SimpleStringProperty(cacheOffset);
        this.cacheBinary = new SimpleStringProperty(cacheBinary);
    }

    public String getcacheIndex(){
        return cacheIndex.get();
    }

    public String getcacheTag(){
        return cacheTag.get();
    }

    public String getcacheOffset(){
        return cacheOffset.get();
    }

    public String getcacheBinary(){
        return cacheBinary.get();
    }
}
