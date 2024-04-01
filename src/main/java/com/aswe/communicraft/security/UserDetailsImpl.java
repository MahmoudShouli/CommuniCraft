package com.aswe.communicraft.security;

import com.aswe.communicraft.models.entities.*;
import com.aswe.communicraft.models.enums.Role;
import com.aswe.communicraft.models.enums.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails, Serializable {

    private int id;
    private String username;
    private String email;
    private String name;
    @JsonIgnore
    private String password;
    private Role role;
    private transient CraftEntity craft;
    private transient ProjectEntity project;
    private Skill levelOfSkill;
    private boolean isDeleted;
    private boolean isLeader;
    private transient TaskEntity task;
    private transient List<MaterialEntity> materials;
    private static Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(int id, String email, String name, String password, Role role , CraftEntity craft,
                           Skill levelOfSkill, boolean isDeleted,boolean isLeader,TaskEntity task,List<MaterialEntity> materials) {
        this.id = id;
        this.username = name;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.craft = craft;
        this.levelOfSkill= levelOfSkill;
        this.isDeleted = isDeleted;
        this.isLeader = isLeader;
        this.task = task;
        this.materials = materials;
    }

    public static UserDetailsImpl build(UserEntity userEntity) {

        //Create new UserDetailsImpl from User object.

        return new UserDetailsImpl(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getUserName(),
                userEntity.getPassword(),
                userEntity.getRole(),
                userEntity.getCraft(),
                userEntity.getLevelOfSkill(),
                userEntity.isDeleted(),
                userEntity.isLeader(),
                userEntity.getTask(),
                userEntity.getMaterial());
    }

    public static void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        UserDetailsImpl.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o){
        if (o == null)
            return false;

        return o instanceof UserDetailsImpl;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
