package bookstore;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static utils.SimulationHelper.getConfig;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;
import utils.SimulationHelper;

public class CatalogBrowsingSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = SimulationHelper.getHttpProtocolBuilder("app.catalogServiceUrl");

    Iterator<Map<String, Object>> randomPageNo = Stream.generate((Supplier<Map<String, Object>>) () -> {
                int randInt = RandomGenerator.getDefault().nextInt(1, 20);
                return Collections.singletonMap("pageNo", randInt);
            })
            .iterator();

    ChainBuilder browseProducts = feed(randomPageNo)
            .exec(http("View Products").get("/api/products?page=#{pageNo}"))
            .pause(1);

    ScenarioBuilder scnBrowseProducts = scenario("Browse Products")
            .during(Duration.ofSeconds(60), "Counter")
            .on(browseProducts);

    // ScenarioBuilder scnBrowseProducts = scenario("Browse Products").exec(browseProducts);

    {
        setUp(scnBrowseProducts.injectOpen(
                        rampUsers(getConfig().getInt("users")).during(10)))
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(800),
                        global().successfulRequests().percent().is(100.0));
    }
}
