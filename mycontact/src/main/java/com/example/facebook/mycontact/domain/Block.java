package com.example.facebook.mycontact.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Block {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    private String reason;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private LocalDate endDate;

}