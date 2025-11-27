package com.example.movieticket.repository;

import com.example.movieticket.domain.model.ShowSeat;
import com.example.movieticket.domain.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShowIdAndSeatCodeIn(Long showId, List<String> seatCodes);

    List<ShowSeat> findByShowId(Long showId);

    List<ShowSeat> findByStatusAndHoldExpiryAtBefore(SeatStatus status, Instant before);

    Optional<ShowSeat> findByShowIdAndSeatCode(Long showId, String seatCode);

    @Modifying
    @Query("update ShowSeat s set s.status = ?2, s.heldByUserId = ?3, s.holdExpiryAt = ?4 where s.id = ?1 and s.status = ?5")
    int conditionalUpdateStatus(Long id, SeatStatus newStatus, Long heldByUserId, Instant expiry, SeatStatus expectedCurrent);
}
