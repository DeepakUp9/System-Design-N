package com.stockbrokerage.order.enums;

public enum OrderType {

    /**
     * Executes immediately at the best available current market price.
     */
    MARKET,

    /**
     * Executes only if the market price reaches the specified limit price.
     */
    LIMIT,

    /**
     * Converts to a Market Order when the stock price reaches a specified stop price.
     */
    STOP_LOSS,

    /**
     * Converts to a Limit Order when the stock price reaches a specified stop price.
     */
    STOP_LIMIT
}