import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        try(Stream<Path> albums = Files.walk(Paths.get("D:\\Java Muzikarchive\\www.javanen-muziekarchief.tv"), 1)) {
            albums.filter(Files::isDirectory).forEach(album -> {
                try(Stream<Path> songs = Files.walk(Paths.get(album.toString()+"\\myjukebox_files"))) {
                    byte[] image = Files.readAllBytes(Paths.get(album.toString()+"\\artiest-groot.jpg"));
                    //System.out.println(image.length);
                    songs.filter(Files::isRegularFile).forEach(song -> {
                        //System.out.println(song.toString());
                        try {
                            Mp3File songmp3 = new Mp3File(song.toString());
                            songmp3.getId3v2Tag().setAlbumImage(image, "image/jpeg");
                            //save the mp3 here
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            });
            //Mp3File mp3 = new Mp3File("\\Myjukebox_Bintang_Jawa_Vol10_2013-06-11\\myjukebox_files\\Lobi_Suriname.mp3");
            //System.out.println(mp3.getId3v2Tag().getArtist());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}