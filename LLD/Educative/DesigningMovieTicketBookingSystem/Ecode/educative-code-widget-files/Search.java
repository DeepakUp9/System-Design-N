import java.util.*;

public interface Search {
    List<Movie> searchMovieTitle(String title);
    List<Movie> searchMovieLanguage(String language);
    List<Movie> searchMovieGenre(String genre);
    List<Movie> searchMovieReleaseDate(Date date);
}
