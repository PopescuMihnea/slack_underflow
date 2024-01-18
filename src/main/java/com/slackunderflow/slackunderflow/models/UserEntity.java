package com.slackunderflow.slackunderflow.models;

import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Utilizator")

public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String password;

    @Column(unique = true)
    private String username;

    private Integer points = 0;

    private BadgeEnum badge = BadgeEnum.BEGINNER;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utilizator_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> authorities;


    public UserEntity() {
        super();
        this.authorities = new HashSet<Role>();
    }


    private UserEntity(Long id, String username, String password, Integer points, BadgeEnum badge, Set<Role> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.points = points;
        this.badge = badge;
        this.authorities = authorities;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public BadgeEnum getBadge() {
        return badge;
    }

    public void setBadge(BadgeEnum badge) {
        this.badge = badge;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
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


    public static class Builder {
        private Long id;
        private String password;
        private String username;
        private Integer points = 0;
        private BadgeEnum badge = BadgeEnum.BEGINNER;
        private Set<Role> authorities = new HashSet<>();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder points(Integer points) {
            this.points = points;
            return this;
        }

        public Builder badge(BadgeEnum badge) {
            this.badge = badge;
            return this;
        }

        public Builder authorities(Set<Role> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(id, username, password, points, badge, authorities);
        }
    }


    public static Builder builder() {
        return new Builder();
    }
}