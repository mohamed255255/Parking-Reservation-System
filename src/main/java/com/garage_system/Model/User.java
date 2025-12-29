package com.garage_system.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Users")
@Setter
@Getter
@NoArgsConstructor
public class User {

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Column(nullable = false)
    private String name;
    
    @Column(unique = true , nullable = false)
    private String email;
   
    @Column(nullable = false)
    private String password;
  
    @Column(nullable = false)
    private String phone ;
   
    @Column(name = "verification_code")
    private String verificationCode ;

    @Column(name = "code_expiration_time")
    private LocalDateTime expirationTime;
   
    @Column(name = "is_verified" , columnDefinition ="boolean default false")
    private boolean isVerified ;
 
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;
   
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Bill> bills ;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservationList ;

    @OneToMany(mappedBy = "user")
    private List<Vehicle> vehicles ;

  
}
