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
public class Answer extends BodyEntity {
    @Column(name = "rank_")
    private Integer rank;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;


}
