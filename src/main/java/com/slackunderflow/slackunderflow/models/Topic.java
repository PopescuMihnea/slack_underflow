package com.slackunderflow.slackunderflow.models;

import com.slackunderflow.slackunderflow.enums.TopicEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @Column(name = "id")
    private TopicEnum topic;
}
