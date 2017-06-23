package com.davidstranders.sportclub.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DS on 18-1-2017.
 */
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Member> members;

    public Role(){}

    public Role(String name) {
        this.name = name;
        this.members = new HashSet<Member>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
