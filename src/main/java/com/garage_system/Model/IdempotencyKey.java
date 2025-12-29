package  com.garage_system.Model;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "idempotency_keys")
@Getter
@Setter
public class IdempotencyKey{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idempotency_key;

    @Column(nullable = false)
    private String status ;

    @Column(nullable = false)
    private String payload;

    @Lob
    private String response_body ; 

    @Column(columnDefinition= "SMALLINT")
    private int response_code ; 

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

  
}