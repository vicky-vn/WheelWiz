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

    private static final int Pages_Max = 100; // Limit to avoid excessive crawling
    private static final int MAX_URLS_PER_PAGE = 35; // Limit URLs per page
    private static final String CSV_FILE = "crawled_urls.csv";
    private Set<String> visitedUrls = new HashSet<>();

    public static void main(String[] args) {
        String[] sites = {
                "https://toyota.ca",
                "https://hyundaicanada.com",
                "https://chevrolet.ca",
                "https://mitsubishi-motors.ca"
        };

        WebCrawler crawler = new WebCrawler();
        crawler.startCrawling(sites);
    }

    public void startCrawling(String[] sites) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE))) {
            for (String site : sites) {
                crawl(site, writer);
            }
        } catch (IOException emsg) {
            System.err.println("Error occurred in writing to CSV file: " + emsg.getMessage());
        }
    }

    public void crawl(String url, CSVWriter writer) {
        if (visitedUrls.size() >= Pages_Max) {
            return;
        }

        if (visitedUrls.contains(url)) {
            return;
        }

        visitedUrls.add(url);

        try {
            Document varForDoc = Jsoup.connect(url).get();
            Elements varForLinks = varForDoc.select("a[href]");
            int count = 0;

            for (Element varForLink : varForLinks) {
                if (count >= MAX_URLS_PER_PAGE) {
                    break;
                }

                String linkHref = varForLink.attr("abs:href");
                if (!visitedUrls.contains(linkHref) && linkHref.startsWith("http")) {
                    visitedUrls.add(linkHref); // Add to visited list to avoid duplicate crawling
                    writer.writeNext(new String[] { linkHref });
                    count++;
                }
            }
            writer.writeNext(new String[] { url });
        } catch (IOException emsg) {
            System.err.println("Error occurred while crawling " + url + ": " + emsg.getMessage());
        }
    }
}