package com.espn.cricinfo.infrastructure.repository;

import com.espn.cricinfo.infrastructure.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Venue entities.
 * Provides data access operations for cricket venues/stadiums.
 */
@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {

    /**
     * Find venue by name.
     */
    Optional<VenueEntity> findByName(String name);

    /**
     * Find venues by city.
     */
    List<VenueEntity> findByCity(String city);

    /**
     * Find venues by country.
     */
    List<VenueEntity> findByCountry(String country);

    /**
     * Find venues by city and country.
     */
    List<VenueEntity> findByCityAndCountry(String city, String country);

    /**
     * Find venues with capacity greater than specified value.
     */
    List<VenueEntity> findByCapacityGreaterThan(Integer capacity);

    /**
     * Find venues by pitch type.
     */
    List<VenueEntity> findByPitchType(String pitchType);

    /**
     * Find high-scoring venues.
     */
    @Query("SELECT v FROM VenueEntity v WHERE v.averageFirstInningsScore >= :minScore")
    List<VenueEntity> findHighScoringVenues(@Param("minScore") Double minScore);

    /**
     * Find batting-friendly venues.
     */
    @Query("SELECT v FROM VenueEntity v WHERE v.averageRunsPerWicket >= :minRunsPerWicket")
    List<VenueEntity> findBattingFriendlyVenues(@Param("minRunsPerWicket") Double minRunsPerWicket);

    /**
     * Find bowling-friendly venues.
     */
    @Query("SELECT v FROM VenueEntity v WHERE v.averageRunsPerWicket <= :maxRunsPerWicket")
    List<VenueEntity> findBowlingFriendlyVenues(@Param("maxRunsPerWicket") Double maxRunsPerWicket);

    /**
     * Find balanced venues.
     */
    @Query("SELECT v FROM VenueEntity v WHERE v.averageRunsPerWicket BETWEEN :minRuns AND :maxRuns")
    List<VenueEntity> findBalancedVenues(@Param("minRuns") Double minRuns, @Param("maxRuns") Double maxRuns);

    /**
     * Find venues by rating criteria.
     */
    @Query("SELECT v FROM VenueEntity v WHERE " +
           "v.bounceRating >= :bounceRating AND " +
           "v.paceRating >= :paceRating AND " +
           "v.spinRating >= :spinRating")
    List<VenueEntity> findVenuesByPitchCharacteristics(@Param("bounceRating") Integer bounceRating,
                                                       @Param("paceRating") Integer paceRating,
                                                       @Param("spinRating") Integer spinRating);

    /**
     * Find venues with coordinates (for mapping).
     */
    @Query("SELECT v FROM VenueEntity v WHERE v.latitude IS NOT NULL AND v.longitude IS NOT NULL")
    List<VenueEntity> findVenuesWithCoordinates();

    /**
     * Find venues in a specific geographical area.
     */
    @Query("SELECT v FROM VenueEntity v WHERE " +
           "v.latitude BETWEEN :minLat AND :maxLat AND " +
           "v.longitude BETWEEN :minLng AND :maxLng")
    List<VenueEntity> findVenuesInArea(@Param("minLat") Double minLat,
                                      @Param("maxLat") Double maxLat,
                                      @Param("minLng") Double minLng,
                                      @Param("maxLng") Double maxLng);

    /**
     * Count venues by country.
     */
    @Query("SELECT v.country, COUNT(v) FROM VenueEntity v GROUP BY v.country ORDER BY COUNT(v) DESC")
    List<Object[]> countVenuesByCountry();

    /**
     * Find venues by capacity range.
     */
    @Query("SELECT v FROM VenueEntity v WHERE v.capacity BETWEEN :minCapacity AND :maxCapacity")
    List<VenueEntity> findVenuesByCapacityRange(@Param("minCapacity") Integer minCapacity,
                                                @Param("maxCapacity") Integer maxCapacity);

    /**
     * Get venue statistics summary.
     */
    @Query("SELECT " +
           "COUNT(v) as totalVenues, " +
           "AVG(v.capacity) as avgCapacity, " +
           "MAX(v.capacity) as maxCapacity, " +
           "AVG(v.averageFirstInningsScore) as avgFirstInningsScore " +
           "FROM VenueEntity v")
    Object[] getVenueStatisticsSummary();

    /**
     * Find recently hosted venues (based on matches).
     */
    @Query("SELECT DISTINCT v FROM VenueEntity v " +
           "JOIN MatchEntity m ON m.venue = v " +
           "WHERE m.startTime >= :sinceDate " +
           "ORDER BY v.updatedAt DESC")
    List<VenueEntity> findRecentlyActiveVenues(@Param("sinceDate") java.time.LocalDateTime sinceDate);

    /**
     * Search venues by name or city.
     */
    @Query("SELECT v FROM VenueEntity v WHERE " +
           "LOWER(v.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(v.city) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<VenueEntity> searchVenues(@Param("query") String query);
}
