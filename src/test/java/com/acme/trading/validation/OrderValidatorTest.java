package com.acme.trading.validation;

import com.acme.trading.domain.OrderSide;
import com.acme.trading.domain.OrderType;
import com.acme.trading.dto.OrderRequest;
import com.acme.trading.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderValidatorTest {

    /** Quantity above which orders are rejected, per OrderValidator.MAX_QUANTITY. */
    private static final BigDecimal MAX_QUANTITY = new BigDecimal("1000000");

    private static OrderRequest request(String symbol,
                                        OrderType orderType,
                                        BigDecimal quantity,
                                        BigDecimal limitPrice) {
        return new OrderRequest("ACC-001", symbol, OrderSide.BUY, orderType, quantity, limitPrice, "client-1");
    }

    private static OrderRequest marketOrder(BigDecimal quantity) {
        return request("AAPL", OrderType.MARKET, quantity, null);
    }

    @Nested
    @DisplayName("symbol")
    class Symbol {

        @ParameterizedTest(name = "symbol [{0}] is rejected")
        @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
        void blankSymbolIsRejected(String symbol) {
            OrderRequest request = request(symbol, OrderType.MARKET, BigDecimal.TEN, null);

            ValidationException thrown =
                    assertThrows(ValidationException.class, () -> OrderValidator.validate(request));
            assertEquals("Symbol must not be blank", thrown.getMessage());
        }

        @Test
        void symbolIsNotNormalisedByTheValidator() {
            // Case handling belongs to OrderService, which upper-cases on the way out.
            assertDoesNotThrow(() -> OrderValidator.validate(request("aapl", OrderType.MARKET, BigDecimal.TEN, null)));
        }
    }

    @Nested
    @DisplayName("quantity")
    class Quantity {

        @ParameterizedTest(name = "quantity {0} is rejected as non-positive")
        @ValueSource(strings = {"0", "0.00", "-1", "-0.0001"})
        void nonPositiveQuantityIsRejected(String quantity) {
            OrderRequest request = marketOrder(new BigDecimal(quantity));

            ValidationException thrown =
                    assertThrows(ValidationException.class, () -> OrderValidator.validate(request));
            assertEquals("Quantity must be positive", thrown.getMessage());
        }

        @Test
        void smallestPositiveQuantityIsAccepted() {
            assertDoesNotThrow(() -> OrderValidator.validate(marketOrder(new BigDecimal("0.0001"))));
        }

        @Test
        void quantityExactlyAtTheMaximumIsAccepted() {
            // The check is `> MAX_QUANTITY`, so the boundary itself is allowed.
            assertDoesNotThrow(() -> OrderValidator.validate(marketOrder(MAX_QUANTITY)));
        }

        @Test
        void quantityJustAboveTheMaximumIsRejected() {
            OrderRequest request = marketOrder(MAX_QUANTITY.add(new BigDecimal("0.01")));

            ValidationException thrown =
                    assertThrows(ValidationException.class, () -> OrderValidator.validate(request));
            assertEquals("Quantity exceeds maximum allowed size", thrown.getMessage());
        }

        @Test
        void scaleDoesNotAffectTheMaximumComparison() {
            // compareTo ignores scale, so 1000000.00 must be treated as the boundary, not above it.
            assertDoesNotThrow(() -> OrderValidator.validate(marketOrder(new BigDecimal("1000000.00"))));
        }
    }

    @Nested
    @DisplayName("limit price")
    class LimitPrice {

        @ParameterizedTest(name = "{0} requires a limit price")
        @EnumSource(value = OrderType.class, names = {"LIMIT", "STOP_LIMIT"})
        void priceBearingOrderTypesRequireALimitPrice(OrderType orderType) {
            OrderRequest request = request("AAPL", orderType, BigDecimal.TEN, null);

            ValidationException thrown =
                    assertThrows(ValidationException.class, () -> OrderValidator.validate(request));
            assertEquals("Limit price is required for limit orders", thrown.getMessage());
        }

        @ParameterizedTest(name = "{0} rejects a non-positive limit price")
        @EnumSource(value = OrderType.class, names = {"LIMIT", "STOP_LIMIT"})
        void nonPositiveLimitPriceIsRejected(OrderType orderType) {
            OrderRequest request = request("AAPL", orderType, BigDecimal.TEN, BigDecimal.ZERO);

            assertThrows(ValidationException.class, () -> OrderValidator.validate(request));
        }

        @ParameterizedTest(name = "{0} accepts a positive limit price")
        @EnumSource(value = OrderType.class, names = {"LIMIT", "STOP_LIMIT"})
        void positiveLimitPriceIsAccepted(OrderType orderType) {
            OrderRequest request = request("AAPL", orderType, BigDecimal.TEN, new BigDecimal("101.50"));

            assertDoesNotThrow(() -> OrderValidator.validate(request));
        }

        @ParameterizedTest(name = "{0} does not require a limit price")
        @EnumSource(value = OrderType.class, names = {"MARKET", "STOP"})
        void priceFreeOrderTypesDoNotRequireALimitPrice(OrderType orderType) {
            // Documents current behaviour: STOP carries no stop-price field on OrderRequest,
            // so the validator has nothing to check for it.
            assertDoesNotThrow(() -> OrderValidator.validate(request("AAPL", orderType, BigDecimal.TEN, null)));
        }
    }

    @Test
    void aFullyValidLimitOrderPasses() {
        OrderRequest request = new OrderRequest(
                "ACC-001", "MSFT", OrderSide.SELL, OrderType.LIMIT,
                new BigDecimal("50"), new BigDecimal("420.00"), "client-2");

        assertDoesNotThrow(() -> OrderValidator.validate(request));
    }

    @Test
    void symbolIsCheckedBeforeQuantity() {
        // Both are invalid; the message tells us which rule fired first.
        OrderRequest request = request("", OrderType.MARKET, BigDecimal.ZERO, null);

        ValidationException thrown =
                assertThrows(ValidationException.class, () -> OrderValidator.validate(request));
        assertEquals("Symbol must not be blank", thrown.getMessage());
    }
}
