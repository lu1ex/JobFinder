package com.example.jobfinder.entity;

import com.example.jobfinder.enums.Role;
import com.example.jobfinder.forms.RegisterForm;
import com.example.jobfinder.requestBodyModels.UserRequestBodyModel;
import com.example.jobfinder.services.PasswordService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;/*
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;*/

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity /*implements UserDetails*/ {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;
    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean activeAccount;
    private boolean subscriber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preferences", referencedColumnName = "id")
    private UserPreferencesEntity userPreferences;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "matched_offers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id"))
    private Set<UnifiedOfferEntity> matchedOffers = new HashSet<>();


    public UserEntity(RegisterForm registerForm) {
        this.id = UUID.randomUUID().toString();
        this.name = registerForm.getName();
        this.surname =  registerForm.getSurname();
        this.login = registerForm.getLogin();
        this.password = PasswordService.codePassword(registerForm.getPassword());
        this.email = registerForm.getEmail();
        this.role = Role.LIMITED_USER;
        this.activeAccount = false;
        this.subscriber = false;
    }

    public UserEntity(UserRequestBodyModel userRequestBodyModel) {
        this.id = UUID.randomUUID().toString();
        this.name = userRequestBodyModel.getName();
        this.surname =  userRequestBodyModel.getSurname();
        this.login = userRequestBodyModel.getLogin();
        this.password = PasswordService.codePassword(userRequestBodyModel.getPassword());
        this.email = userRequestBodyModel.getEmail();
        this.activeAccount = userRequestBodyModel.isActiveAccount();
        this.subscriber = userRequestBodyModel.isSubscriber();
    }
   /* @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
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
    }*/
}
