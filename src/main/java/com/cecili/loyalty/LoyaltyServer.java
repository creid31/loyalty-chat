/**
 * Copyright 2016 NCR Corporation
 */
package com.cecili.loyalty;

import java.util.Map;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * Server to
 * 
 * @author Cecili Reid (cr250220) on Jan 18, 2017
 */
public class LoyaltyServer extends AbstractVerticle {

    private final Map<String, Double> cartBalances;

    private final Products prodDb = new Products();

    private final Discounts discountDb = new Discounts();
    public LoyaltyServer() {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        cartBalances = instance.getMap("Balances");
    }
    @Override
    public void start() throws Exception {

        // String name = config().getString("name", "World");
        // vertx.createHttpServer().requestHandler(req -> req.response().end("Hello " + name + "!")).listen(8080);
        Router router = Router.router(vertx);

        // Allow events for the designated addresses in/out of the event bus bridge
        BridgeOptions opts =
                new BridgeOptions().addInboundPermitted(new PermittedOptions().setAddress("product.to.server")).addOutboundPermitted(
                        new PermittedOptions().setAddress("balance.to.client"));

        // Create the event bus bridge and add it to the router.
        SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
        router.route("/eventbus/*").handler(ebHandler);

        // Create a router endpoint for the static content.
        router.route("/assets/*").handler(StaticHandler.create("assets"));

        // Start the web server and tell it to use the router to handle requests.
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

        EventBus eb = vertx.eventBus();

        // Register to listen for messages coming IN to the server
        eb.consumer("product.to.server").handler(message -> {
            String msg = message.body().toString();
            String[] data = msg.split(":");
            String user = data[0].trim();
            String id = data[1].trim();
            Product product = prodDb.getProduct(id);
            Double balance = cartBalances.getOrDefault(user, 0.00);
            balance += product.getPrice();
            eb.publish("balance.to.client", product.toString());
            Discount discount = discountDb.getDiscount(product.getType());
            balance -= discount.getDiscount();
            eb.publish("balance.to.client", "Discount applied: " + discount.toString());
            eb.publish("balance.to.client", "Current balance is $" + balance.toString());
            cartBalances.put(user, balance);
            // // Create a timestamp string
            // String timestamp = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(Date.from(Instant.now()));
            // // Send the message back out to all clients with the timestamp prepended.
            // eb.publish("balance.to.client", timestamp + ": " + message.body());
        });

    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(LoyaltyServer.class.getName());
      }
}