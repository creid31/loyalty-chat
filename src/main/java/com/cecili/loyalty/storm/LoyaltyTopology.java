/**
 * Copyright 2016 NCR Corporation
 */
package com.cecili.loyalty.storm;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.topology.TopologyBuilder;

import com.cecili.loyalty.storm.bolt.CountBolt;

/**
 * @author Cecili Reid (cr250220) on Jan 24, 2017
 *
 */
public class LoyaltyTopology {
    SpoutBuilder spoutBuilder;

    public LoyaltyTopology() {
        spoutBuilder = new SpoutBuilder();
    }

    private void submitTopology() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        KafkaSpout kafkaSpout = spoutBuilder.buildKafkaSpout();
        CountBolt bolt = new CountBolt();

        builder.setSpout(SpoutBuilder.SPOUTID, kafkaSpout);

        // First bolt should receive stream from spout
        builder.setBolt("CountBolt", bolt).shuffleGrouping(SpoutBuilder.SPOUTID);

        Config conf = new Config();
        conf.setNumWorkers(1);
        conf.put(Config.TOPOLOGY_STATS_SAMPLE_RATE, 1.00);

        // Run in localmode without UI
        // LocalCluster cluster = new LocalCluster();
        // cluster.submitTopology("storm-kafka-topology-test", conf, builder.createTopology());
        StormSubmitter.submitTopology("loyalty-chat-topology", conf, builder.createTopology());
    }

    public static void main(String[] args) throws Exception {
        LoyaltyTopology ingestionTopology = new LoyaltyTopology();
        ingestionTopology.submitTopology();
    }

}
