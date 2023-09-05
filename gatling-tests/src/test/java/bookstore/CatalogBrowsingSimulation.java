package bookstore;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static utils.SimulationHelper.getConfig;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import utils.SimulationHelper;

import java.time.Duration;

public class CatalogBrowsingSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            SimulationHelper.getHttpProtocolBuilder("app.catalogServiceUrl");

    ChainBuilder browseProducts = exec(http("View Products").get("/api/products")).pause(1);

     ScenarioBuilder scnBrowseProducts =
            scenario("Browse Products").during(Duration.ofSeconds(60 * 3), "Counter").on(browseProducts);

    //ScenarioBuilder scnBrowseProducts = scenario("Browse Products").exec(browseProducts);

    {
        setUp(scnBrowseProducts.injectOpen(rampUsers(getConfig().getInt("users")).during(10)))
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(800),
                        global().successfulRequests().percent().is(100.0));
    }
}
