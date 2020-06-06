package com.jack.codingTask.model;

import java.io.Serializable;
import java.util.Objects;

public class StockKey implements Serializable {

    private String location;
    private String code;

    public StockKey() {
    }

    public StockKey(String location, String code) {
        this.location = location;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockKey stockKey = (StockKey) o;
        return location.equals(stockKey.location) &&
                code.equals(stockKey.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, code);
    }
}