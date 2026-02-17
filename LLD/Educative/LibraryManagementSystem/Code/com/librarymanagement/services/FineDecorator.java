package com.librarymanagement.services;

/**
 * Class diagram: Decorator pattern — abstract decorator.
 * Wraps a FineCalculation and can add extra charges (e.g. late fee tier).
 */
public abstract class FineDecorator implements FineCalculation {
    protected final FineCalculation wrapped;

    protected FineDecorator(FineCalculation wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public double getAmount() {
        return wrapped.getAmount() + getExtraAmount();
    }

    /** Override to add extra amount (e.g. per additional day tier). */
    protected abstract double getExtraAmount();
}
