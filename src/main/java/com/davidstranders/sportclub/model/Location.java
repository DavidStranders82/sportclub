package com.davidstranders.sportclub.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by DS on 5-2-2017.
 */
@Document
public class Location implements Comparable<Location>{

    @Id
    private String id;
    private String field;
    private boolean external;

    public Location(){}

    public Location(String field, boolean external) {
        this.field = field;
        this.external = external;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    @Override
    public int compareTo(Location o) {
        return this.field.compareTo(o.getField());
    }
}
