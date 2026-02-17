package com.librarymanagement.services;

/**
 * Class diagram design pattern: Decorator pattern — component interface.
 * Fine amount can be extended by decorators (e.g. per additional day).
 * Reference: Class Diagram — "Fine payment follows Decorator pattern as fine keeps adding upon increased days."
 */
public interface FineCalculation {
    double getAmount();
}
