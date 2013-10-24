package com.jarvis.deserializer;

import java.nio.ByteBuffer;
 

/**
 * Created with IntelliJ IDEA.
 * User: bhattacharjeer
 * Date: 10/17/13
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntegralDeserializerImpl implements Deserializer {

    //static MarketRateSerializer v1Serializer = MarketRateSerializerFactory.instance().getSerializerForVersion(1);
    //static MarketRateSerializer v2Serializer = MarketRateSerializerFactory.instance().getSerializerForVersion(2);

    static MarketRateSerializer serializer = new V1MarketRateSerializer();

     

    
    @Override
    public MarketRate deserialize(String serContent) {
        return serializer.deserialize(serContent);
    }
    

    static String testString_v3 = "3|3|G-4d93e864f-141c7504d78-DBNA-34d852|{G-4d93e864f-141c7504d78-DBNA-34d852,G-4d93e864f-141c7504d78-DBNA-34d852,G-4d93e864f-141c7504d78-DBNA-34d852,}|RPSNY|EUR|USD|null|{1.213,1.2129,1.2128,}|{1.213,1.2131,1.2131,}|{1.23545E7,3.70635E7,6.17725E7,}|{1.23545E7,3.70635E7,6.17725E7,}|null|DBNA|{}|{}|false|null|{RatePublishingMulticastStreams~[],PRC_SUPPORT_TYPE~2,RatePublishingJMSTopics~[],}|{RateReceivedByAdapter~1382028430712,RateEffective~1382028430712,}|-1|";
    static String testString_v1 = "1|1|G-4d93e864f-141ccccfa45-DBNA-36c393|{G-4d93e864f-141ccccfa45-DBNA-36c393,}|RPSNY|USD|JPY|null|{98.57,}|{98.57,}|{100000.0,}|{100000.0,}|null|DBNA|{}|{}|false|null|{RatePublishingMulticastStreams~[],PRC_SUPPORT_TYPE~2,RatePublishingJMSTopics~[],}|{RateReceivedByAdapter~1382120487492,RateEffective~1382120487492,}|-1|";

    public static void main(String [] a) {
        //Deserializer deserializer = new IntegralDeserializerImpl();
        //MarketRate rate = deserializer.deserialize(testString_v1);
        //System.out.println("Market Rate " + rate);

        MarketRate rate = serializer.deserialize(testString_v3);

        System.out.println("Rate = > " + rate);
        System.out.println("Base Currency => " + rate.getBaseCcy());
        System.out.println("Bid Limit => " + rate.getBidLimit());
        System.out.println("var currency => " + rate.getVariableCcy());

        int bidTierSize = rate.getBidTierSize();

        System.out.println("bid tier size " + bidTierSize);
        //getBidTier

        for(int i = 0 ; i < bidTierSize ; i++) {
            System.out.println("tier " + i + " is " + rate.getBidTier(i));
        }

    }
}
