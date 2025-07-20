package org.example.codebase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "otp")
@Table(name = "otp")
public class OTP {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "otp")
    private String otp;

    @Column(name = "number_fail")
    private Integer numberFail;
}
