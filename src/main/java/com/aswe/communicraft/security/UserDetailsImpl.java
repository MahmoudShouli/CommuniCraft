package com.aswe.communicraft.security;

import com.aswe.communicraft.models.entities.*;
import com.aswe.communicraft.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails {

    private int id;
    private String username;
    private String email;
    private String name;
    @JsonIgnore
    private String password;
    private Role role;
    private CraftEntity craft;
    private ProjectEntity project;
    private String levelOfSkill;
    private boolean isDeleted;
    private boolean isLeader;
    private TaskEntity task;
    private List<MaterialEntity> materials;
    private static Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(int id, String email, String name, String password, Role role , CraftEntity craft,
                           String levelOfSkill, boolean isDeleted,boolean isLeader,TaskEntity task,List<MaterialEntity> materials,Collection<? extends GrantedAuthority> authorities) {
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
        UserDetailsImpl.authorities = authorities;
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
                userEntity.getMaterial(),
                authorities);
    }

    public static UserEntity build(UserDetailsImpl user) {

        //Create new User from UserDetailsImpl object.

        return new UserEntity(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getCraft(),
                user.getProject(),
                user.isDeleted(),
                user.getLevelOfSkill(),
                user.isLeader(),
                user.getTask(),
                user.getMaterials());

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
