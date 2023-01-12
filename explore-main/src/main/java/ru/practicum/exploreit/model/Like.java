package ru.practicum.exploreit.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@Table(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @JsonManagedReference
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "is_like")
    private boolean isLike;
}
