package com.espn.cricinfo.infrastructure.repository;

import com.espn.cricinfo.infrastructure.entity.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Tournament entities.
 * Provides data access operations for cricket tournaments.
 */
@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {

    /**
     * Find tournament by name.
     */
    Optional<TournamentEntity> findByName(String name);

    /**
     * Find tournament by short name.
     */
    Optional<TournamentEntity> findByShortName(String shortName);

    /**
     * Find tournaments by format.
     */
    List<TournamentEntity> findByFormat(TournamentEntity.TournamentFormat format);

    /**
     * Find tournaments by status.
     */
    List<TournamentEntity> findByStatus(TournamentEntity.TournamentStatus status);

    /**
     * Find tournaments by organizing body.
     */
    List<TournamentEntity> findByOrganizingBody(String organizingBody);

    /**
     * Find active tournaments.
     */
    @Query("SELECT t FROM TournamentEntity t WHERE t.status = 'IN_PROGRESS'")
    List<TournamentEntity> findActiveTournaments();

    /**
     * Find completed tournaments.
     */
    @Query("SELECT t FROM TournamentEntity t WHERE t.status = 'COMPLETED'")
    List<TournamentEntity> findCompletedTournaments();

    /**
     * Find upcoming tournaments.
     */
    @Query("SELECT t FROM TournamentEntity t WHERE t.status = 'UPCOMING' AND t.startDate > :currentDate")
    List<TournamentEntity> findUpcomingTournaments(@Param("currentDate") LocalDate currentDate);

    /**
     * Find tournaments in date range.
     */
    @Query("SELECT t FROM TournamentEntity t WHERE " +
           "(t.startDate BETWEEN :startDate AND :endDate) OR " +
           "(t.endDate BETWEEN :startDate AND :endDate)")
    List<TournamentEntity> findTournamentsInDateRange(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);

    /**
     * Find tournaments by host country.
     */
    List<TournamentEntity> findByHostCountry(String hostCountry);

    /**
     * Find tournaments containing team.
     */
    @Query("SELECT t FROM TournamentEntity t JOIN t.teams team WHERE team.id = :teamId")
    List<TournamentEntity> findTournamentsByTeam(@Param("teamId") Long teamId);

    /**
     * Find tournaments at venue.
     */
    @Query("SELECT t FROM TournamentEntity t JOIN t.venues venue WHERE venue.id = :venueId")
    List<TournamentEntity> findTournamentsByVenue(@Param("venueId") Long venueId);

    /**
     * Get tournament statistics summary.
     */
    @Query("SELECT " +
           "COUNT(t) as totalTournaments, " +
           "SUM(t.prizeMoneyMillion) as totalPrizeMoney, " +
           "AVG(t.totalTeams) as avgTeams, " +
           "COUNT(DISTINCT t.organizingBody) as organizingBodies " +
           "FROM TournamentEntity t WHERE t.status = 'COMPLETED'")
    Object[] getTournamentStatisticsSummary();

    /**
     * Find tournaments by prize money range.
     */
    @Query("SELECT t FROM TournamentEntity t WHERE t.prizeMoneyMillion BETWEEN :minPrize AND :maxPrize")
    List<TournamentEntity> findTournamentsByPrizeMoneyRange(@Param("minPrize") Double minPrize,
                                                           @Param("maxPrize") Double maxPrize);

    /**
     * Count tournaments by format.
     */
    @Query("SELECT t.format, COUNT(t) FROM TournamentEntity t GROUP BY t.format")
    List<Object[]> countTournamentsByFormat();

    /**
     * Count tournaments by status.
     */
    @Query("SELECT t.status, COUNT(t) FROM TournamentEntity t GROUP BY t.status")
    List<Object[]> countTournamentsByStatus();

    /**
     * Find tournaments with winners.
     */
    @Query("SELECT t FROM TournamentEntity t WHERE t.winner IS NOT NULL")
    List<TournamentEntity> findTournamentsWithWinners();

    /**
     * Find tournaments by player of tournament.
     */
    @Query("SELECT t FROM TournamentEntity t WHERE t.playerOfTournament = :playerName")
    List<TournamentEntity> findTournamentsByPlayerOfTournament(@Param("playerName") String playerName);

    /**
     * Find high-profile tournaments (large prize money).
     */
    @Query("SELECT t FROM TournamentEntity t WHERE t.prizeMoneyMillion >= :minPrize ORDER BY t.prizeMoneyMillion DESC")
    List<TournamentEntity> findHighProfileTournaments(@Param("minPrize") Double minPrize);

    /**
     * Search tournaments by name or description.
     */
    @Query("SELECT t FROM TournamentEntity t WHERE " +
           "LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<TournamentEntity> searchTournaments(@Param("query") String query);

    /**
     * Find tournaments hosted in multiple cities.
     */
    @Query("SELECT t FROM TournamentEntity t WHERE t.hostCities LIKE '%,%'")
    List<TournamentEntity> findMultiCityTournaments();

    /**
     * Get tournament winners statistics.
     */
    @Query("SELECT t.winner.name, COUNT(t) FROM TournamentEntity t " +
           "WHERE t.winner IS NOT NULL GROUP BY t.winner.name ORDER BY COUNT(t) DESC")
    List<Object[]> getTournamentWinnersStatistics();
}
