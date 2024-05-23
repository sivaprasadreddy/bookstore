package bookstore;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static utils.SimulationHelper.getConfig;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import utils.SimulationHelper;

public class OrderFlowSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = SimulationHelper.getHttpProtocolBuilder("app.orderServiceUrl");

    FeederBuilder<String> productFeeder = csv("data/feeders/products.csv").random();
    Iterator<Map<String, Object>> randomString = Stream.generate((Supplier<Map<String, Object>>) () -> {
                String randString = RandomStringUtils.randomAlphanumeric(20);
                return Collections.singletonMap("randString", randString);
            })
            .iterator();
    Iterator<Map<String, Object>> randomQuantity = Stream.generate((Supplier<Map<String, Object>>) () -> {
                int randInt = RandomGenerator.getDefault().nextInt(1, 4);
                return Collections.singletonMap("randomQuantity", randInt);
            })
            .iterator();

    ChainBuilder createOrder = feed(randomQuantity)
            .feed(productFeeder)
            .feed(randomString)
            .exec(http("Create Order")
                    .post("/api/orders")
                    .body(ElFileBody("data/feeders/order.json"))
                    .asJson());

    ChainBuilder browseOrders = exec(http("View Orders").get("/api/orders")).pause(1);

    ChainBuilder createOrderFlow = exec(createOrder).pause(1).exec(browseOrders);

    // ScenarioBuilder scnCreateOrder = scenario("Create Order").exec(createOrderFlow);
    ScenarioBuilder scnCreateOrder =
            scenario("Create Order").during(Duration.ofSeconds(60), "Counter").on(createOrderFlow);

    {
        setUp(scnCreateOrder.injectOpen(rampUsers(getConfig().getInt("users")).during(10)))
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(800),
                        global().successfulRequests().percent().is(100.0));
    }
}
