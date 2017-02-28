import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by brianzhao on 2/28/17.
 */
public class DownloadMusic {
    public static void main(String[] args) {
        List<String> urlsToGo = getUrlsOnPage(
                "http://anime.thehylia.com/soundtracks/album/psycho-pass-complete-original-soundtrack",
                x -> x.endsWith(".mp3"));
        for (String url : urlsToGo) {
            List<String> urls = getUrlsOnPage(url, x -> x.endsWith(".mp3"));
            for (String mp3Url : urls) {
                try {
                    //http://stackoverflow.com/q/921262
                    String filename = mp3Url.substring(mp3Url.lastIndexOf('/') + 1);
                    System.out.println("Downloading " + filename + " ...");
                    FileUtils.copyURLToFile(new URL(mp3Url), new File("output/" + filename));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<String> getUrlsOnPage(String url, Function<String, Boolean> filterCondition) {
        List<String> toReturn = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.getElementsByAttribute("href");
            for (Element element : links) {
                String potentialUrl = element.attr("href");
                if (filterCondition.apply(potentialUrl)) {
                    toReturn.add(potentialUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
