import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WebCrawler {

    private static final int MAX_PAGES = 100; // Limit to avoid excessive crawling
    private static final int MAX_URLS_PER_PAGE = 35; // Limit URLs per page
    private static final String CSV_FILE = "crawled_urls.csv";
    private Set<String> visitedUrls = new HashSet<>();

    public static void main(String[] args) {
        String[] sites = {
                "https://toyota.ca",
                "https://hyundaicanada.com",
                "https://chevrolet.ca",
                "https://ford.ca"
        };

        WebCrawler crawler = new WebCrawler();
        crawler.startCrawling(sites);
    }

    public void startCrawling(String[] sites) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE))) {
            for (String site : sites) {
                crawl(site, writer);
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public void crawl(String url, CSVWriter writer) {
        if (visitedUrls.size() >= MAX_PAGES) {
            return;
        }

        if (visitedUrls.contains(url)) {
            return;
        }

        visitedUrls.add(url);

        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            int count = 0;

            for (Element link : links) {
                if (count >= MAX_URLS_PER_PAGE) {
                    break;
                }

                String linkHref = link.attr("abs:href");
                if (!visitedUrls.contains(linkHref) && linkHref.startsWith("http")) {
                    visitedUrls.add(linkHref); // Add to visited list to avoid duplicate crawling
                    writer.writeNext(new String[] { linkHref });
                    count++;
                }
            }
            writer.writeNext(new String[] { url });
        } catch (IOException e) {
            System.err.println("Error while crawling " + url + ": " + e.getMessage());
        }
    }
}
