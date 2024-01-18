package com.slackunderflow.slackunderflow.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BodyEntity {

    private String title;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "Question_Topic",
            joinColumns = {@JoinColumn(name = "question_id")},
            inverseJoinColumns = {@JoinColumn(name = "topic_id")})
    private Set<Topic> topics;

}
