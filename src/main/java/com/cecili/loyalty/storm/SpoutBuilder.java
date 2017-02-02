/**
 * Copyright 2016 NCR Corporation
 */
package com.cecili.loyalty.storm;

import java.util.Map;

import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;

/**
 * @author Cecili Reid (cr250220) on Jan 24, 2017
 *
 */
public class SpoutBuilder extends BaseRichSpout implements IRichSpout {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String SPOUTID = "kafka-spout";

    public KafkaSpout buildKafkaSpout() {
        BrokerHosts hosts = new ZkHosts("localhost:2181");
        SpoutConfig spoutConfig = new SpoutConfig(hosts, "loyalty-chat", "/kafka", "loyalty");
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        return kafkaSpout;
    }

    /* (non-Javadoc)
     * @see org.apache.storm.spout.ISpout#open(java.util.Map, org.apache.storm.task.TopologyContext, org.apache.storm.spout.SpoutOutputCollector)
     */
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.apache.storm.spout.ISpout#close()
     */
    public void close() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.apache.storm.spout.ISpout#activate()
     */
    public void activate() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.apache.storm.spout.ISpout#deactivate()
     */
    public void deactivate() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.apache.storm.spout.ISpout#nextTuple()
     */
    public void nextTuple() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.apache.storm.spout.ISpout#ack(java.lang.Object)
     */
    public void ack(Object msgId) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.apache.storm.spout.ISpout#fail(java.lang.Object)
     */
    public void fail(Object msgId) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.apache.storm.topology.IComponent#declareOutputFields(org.apache.storm.topology.OutputFieldsDeclarer)
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.apache.storm.topology.IComponent#getComponentConfiguration()
     */
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }
}

