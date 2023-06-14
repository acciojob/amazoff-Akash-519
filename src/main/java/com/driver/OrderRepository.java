package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    Map<String,Order> ordersDb = new HashMap<>();
    Map<String, DeliveryPartner> deliveryPartnerDb = new HashMap<>();

    Map<String,String> orderPartnerDb = new HashMap<>();
    Map<String, List<String>> partnerOrdersDb = new HashMap<>();

    public void addOrder(Order order)
    {
        ordersDb.put(order.getId(),order);
    }

    public void addPartner(String partnerId)
    {
        deliveryPartnerDb.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId)
    {
        if(ordersDb.containsKey(orderId) && deliveryPartnerDb.containsKey(partnerId))
         orderPartnerDb.put(orderId, partnerId);

        List<String> currentOrders = new ArrayList<>();
        if(partnerOrdersDb.containsKey(partnerId))
            currentOrders = partnerOrdersDb.get(partnerId);
        currentOrders.add(orderId);
        partnerOrdersDb.put(partnerId,currentOrders);

        //also increase the noOfOrders count in DeliveryPartner class
        DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
    }

    public Order getOrderById(String orderId)
    {
       return ordersDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId)
    {
        return deliveryPartnerDb.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId)
    {
       return partnerOrdersDb.get(partnerId).size();
    }
    public List<String> getOrdersByPartnerId(String partnerId)
    {
        return partnerOrdersDb.get(partnerId);
    }
    public List<String> getAllOrders()
    {
//        List<String> orderIds = new ArrayList<>(ordersDb.keySet());
//        return orderIds;

        List<String> orderIds = new ArrayList<>();
        for(String orderId : ordersDb.keySet())
            orderIds.add(orderId);
        return orderIds;

    }
    public int getCountOfUnassignedOrders()
    {
        return ordersDb.size() - orderPartnerDb.size();
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId)
    {
        int count = 0;
        List<String> orderIds = new ArrayList<>();
        for(String orderId : orderIds)
        {
            if(ordersDb.get(orderId).getDeliveryTime()>time)
                count++;
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId)
    {
        int maxTime =0;
        List<String> orders = new ArrayList<>();
        orders = partnerOrdersDb.get(partnerId);

        for(String orderId : orders)
        {
            int currentTime = ordersDb.get(orderId).getDeliveryTime();
            maxTime = Math.max(maxTime, currentTime);
        }
        return maxTime;
    }
    public void deletePartnerById(String partnerId)
    {
        deliveryPartnerDb.remove(partnerId);

        List<String> orderIds = new ArrayList<>();
        orderIds = partnerOrdersDb.get(partnerId);
        partnerOrdersDb.remove(partnerId);
        for(String orderId : orderIds)
            orderPartnerDb.remove(orderId);
    }
    public void deleteOrderById(String orderId)
    {
        ordersDb.remove(orderId);
        String partnerId = orderPartnerDb.get(orderId);
        orderPartnerDb.remove(orderId);
        partnerOrdersDb.get(partnerId).remove(orderId);
        deliveryPartnerDb.get(partnerId).setNumberOfOrders(partnerOrdersDb.get(partnerId).size());
    }



}
