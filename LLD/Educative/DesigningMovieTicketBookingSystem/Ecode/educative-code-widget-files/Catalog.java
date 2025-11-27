import java.util.*;

public class Catalog implements Search {
    public HashMap<String, List<Movie>> movieTitles = new HashMap<>();
    public HashMap<String, List<Movie>> movieLanguages = new HashMap<>();
    public HashMap<String, List<Movie>> movieGenres = new HashMap<>();
    public HashMap<Date, List<Movie>> movieReleaseDates = new HashMap<>();

    public List<Movie> searchMovieTitle(String title) {
        return movieTitles.getOrDefault(title, new ArrayList<>());
    }
    public List<Movie> searchMovieLanguage(String language) {
        return movieLanguages.getOrDefault(language, new ArrayList<>());
    }
    public List<Movie> searchMovieGenre(String genre) {
        return movieGenres.getOrDefault(genre, new ArrayList<>());
    }
    public List<Movie> searchMovieReleaseDate(Date date) {
        return movieReleaseDates.getOrDefault(date, new ArrayList<>());
    }
}
