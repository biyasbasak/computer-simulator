package com.csci6461.gw.simulator.ui;

import javafx.beans.property.SimpleStringProperty;

public class CacheTable {
    private final SimpleStringProperty cacheIndex;
    private final SimpleStringProperty cacheOffset;
    private final SimpleStringProperty cacheTag;
    private final SimpleStringProperty cacheBinary;

    public CacheTable(String cacheIndex, String cacheOffset, String cacheTag, String cacheBinary){
        super();
        this.cacheIndex = new SimpleStringProperty(cacheIndex);
        this.cacheOffset = new SimpleStringProperty(cacheOffset);
        this.cacheTag = new SimpleStringProperty(cacheTag);
        this.cacheBinary = new SimpleStringProperty(cacheBinary);
    }

    public String getCacheIndex(){
        return cacheIndex.get();
    }
    public String getCacheOffset(){
        return cacheOffset.get();
    }
    public String getCacheTag(){
        return cacheTag.get();
    }
    public String getCacheBinary(){
        return cacheBinary.get();
    }
}
