package com.davidstranders.sportclub.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DS on 18-1-2017.
 */
@Document
public class Role {

    @Id
    private String id;

    private String name;

    @DBRef
    private Set<Member> members;

    public Role(){}

    public Role(String name) {
        this.name = name;
        this.members = new HashSet<Member>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }
}
