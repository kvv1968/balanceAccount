package com.balance.account.model;

/**
 * Курс валют по отношению доллару
 */
public enum ExchangeRate {
    EUR(1.15),
    BYN(0.31),
    RUB(0.013);

    private Double coefficient;

    ExchangeRate(double coefficient) {
        this.coefficient = coefficient;
    }

    public Double getCoefficient() {
        return coefficient;
    }
}
