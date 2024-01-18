package com.slackunderflow.slackunderflow.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion extends BodyEntity {

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;


}
