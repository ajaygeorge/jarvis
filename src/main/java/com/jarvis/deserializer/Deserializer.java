package com.jarvis.deserializer;

import com.integral.is.message.MarketRate;

/**
 * Created with IntelliJ IDEA.
 * User: bhattacharjeer
 * Date: 10/17/13
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Deserializer {

    MarketRate deserialize(String serContent);

}
