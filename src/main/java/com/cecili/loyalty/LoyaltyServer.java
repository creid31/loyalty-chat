/**
 * Copyright 2016 NCR Corporation
 */
package com.cecili.loyalty;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

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

    /**
     * Map of users to their cart balances; hazelcast instance
     */
    private final Map<String, Double> cartBalances;

    private final Products prodDb = new Products();

    private final Discounts discountDb = new Discounts();
    public LoyaltyServer() {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        cartBalances = instance.getMap("Balances");
    }
    @Override
    public void start() throws Exception {
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
            // Save type of product to be counted down stream by storm
            String typeToCount;

            if (data.length < 2) {
                eb.publish("balance.to.client", "User or product code was not provided. Both are required. Please try again.<br>");
                typeToCount = "USAGE_FAILURE";
            } else {
                String user = data[0].trim();
                String id = data[1].trim();
                Product product = prodDb.getProduct(id);
                if (product != null) {
                    // Add product to balance and print new product's price
                    Double balance = cartBalances.getOrDefault(user, 0.00);
                    balance += product.getPrice();
                    eb.publish("balance.to.client", product.toString());

                    // Add discount to balance and print discount's price
                    Discount discount = discountDb.getDiscount(product.getType());
                    balance -= discount.getDiscount();
                    eb.publish("balance.to.client", discount.toString());

                    // Print the user's current price and update in hazelcast instance
                    eb.publish("balance.to.client", ">>Current balance for user " + user);
                    eb.publish("balance.to.client", "<br> > $" + balance.toString() + "<br>");
                    cartBalances.put(user, balance);
                    typeToCount = product.getType().toString();
                } else {
                    eb.publish("balance.to.client", "Product provided was not found in out system. Please try again.<br>");
                    typeToCount = "UNKNOWN";
                }
            }


            // Publish message using kafka producer
            Properties props = new Properties();

            props.put("metadata.broker.list", "localhost:9092");
            props.put("request.required.acks", "1");
            props.put("bootstrap.servers", "localhost:9092");
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

            KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

            producer.send(new ProducerRecord<String, String>("loyalty-chat", typeToCount));
            producer.close();
        });

    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(LoyaltyServer.class.getName());
      }
}