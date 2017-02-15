package com.udemy.sportclub.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by Dell on 5-2-2017.
 */
@Entity
public class Location implements Comparable<Location>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    private String field;

    @NotNull
    private boolean external;

    public Location(){}

    public Location(String field, boolean external) {
        this.field = field;
        this.external = external;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
