import com.mpatric.mp3agic.Mp3File;
import me.tongfei.progressbar.ProgressBar;
import org.jline.utils.Log;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        int counter = 0;
        // Get folders and initialize the progress bar
        try (Stream<Path> albums = Files.walk(Paths.get("D:\\Java Muzikarchive\\www.javanen-muziekarchief.tv"), 1); ProgressBar pb = new ProgressBar("Writing", 3770)) {
            albums.filter(Files::isDirectory).forEach(album -> {
                // Make sure that folder has songs in it
                try(Stream<Path> songs = Files.walk(Paths.get(album.toString()+"\\myjukebox_files"))) {
                    // Get album image to assign
                    byte[] image = Files.readAllBytes(Paths.get(album.toString()+"\\artiest-groot.jpg"));
                    songs.filter(Files::isRegularFile).forEach(song -> {
                        try {
                            // Assign album image to songs
                            Mp3File songmp3 = new Mp3File(song.toString());
                            songmp3.getId3v2Tag().setAlbumImage(image, "image/jpeg");
                            // Save as a new name (class limitation) then replace the old file with new file
                            songmp3.save(song.toAbsolutePath().toString() + ".new");
                            Path newSong = Paths.get(song.toAbsolutePath().toString() + ".new");
                            Files.move(newSong, song, StandardCopyOption.REPLACE_EXISTING);
                            // Make sure there is only new file
                            if (!Files.exists(newSong) && Files.exists(song)) {
                                pb.step();
                            }
                        } catch (Exception e){
                            Log.error("\nProblem in song:\n\t" + song.toAbsolutePath().toString() + "\n\t" + e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Log.error("\nProblem in album:\n\t" + album.toAbsolutePath().toString() + "\n\t" + e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}