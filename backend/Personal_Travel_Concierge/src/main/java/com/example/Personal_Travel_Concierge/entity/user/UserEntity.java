package com.example.Personal_Travel_Concierge.entity.user;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String email;
    private String password;
    private boolean verificationStatus = false;

    //details
    private String fullName;
    private String country;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id")
    private RoleEntity role;

    public UserEntity(String username, String email,
                      String password, String fullName,
                      String country, String phoneNumber,
                      String address, LocalDate dateOfBirth,
                      RoleEntity role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", verificationStatus=" + verificationStatus +
                ", fullName='" + fullName + '\'' +
                ", country='" + country + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", role=" + role +
                '}';
    }
}