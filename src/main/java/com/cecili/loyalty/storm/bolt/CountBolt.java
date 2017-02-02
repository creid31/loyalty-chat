/**
 * Copyright 2016 NCR Corporation
 */
package com.cecili.loyalty.storm.bolt;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

/**
 * @author Cecili Reid (cr250220) on Jan 24, 2017
 *
 */
public class CountBolt implements IRichBolt {
    private static final long serialVersionUID = 1L;

    private OutputCollector collector;

    Map<String, Integer> counters;

    /* (non-Javadoc)
     * @see backtype.storm.task.IBolt#prepare(java.util.Map, backtype.storm.task.TopologyContext, backtype.storm.task.OutputCollector)
     */
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.counters = new HashMap<String, Integer>();
    }

    /* (non-Javadoc)
     * @see backtype.storm.task.IBolt#execute(backtype.storm.tuple.Tuple)
     */
    public void execute(Tuple input) {
        String productType = input.getString(0);
        if (!counters.containsKey(productType)) {
            counters.put(productType, 1);
        } else {
            Integer c = counters.get(productType) + 1;
            counters.put(productType, c);
        }

        collector.ack(input);
    }

    /* (non-Javadoc)
     * @see backtype.storm.task.IBolt#cleanup()
     */
    public void cleanup() {
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /* (non-Javadoc)
     * @see backtype.storm.topology.IComponent#declareOutputFields(backtype.storm.topology.OutputFieldsDeclarer)
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    /* (non-Javadoc)
     * @see backtype.storm.topology.IComponent#getComponentConfiguration()
     */
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
