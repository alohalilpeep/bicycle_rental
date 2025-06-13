package org.example.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "tariff_zones")
public class TariffZone extends BaseEntity {
    private String zoneName;
    private BigDecimal basePrice;
    private BigDecimal pricePerMinute;
    private String polygonCoordinates;

    @Column(name = "zone_name", nullable = false)
    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    @Column(name = "price_per_minute", nullable = false, precision = 10, scale = 2)
    public BigDecimal getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(BigDecimal pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    @Column(name = "polygon_coordinates", nullable = false, columnDefinition = "TEXT")
    public String getPolygonCoordinates() {
        return polygonCoordinates;
    }

    public void setPolygonCoordinates(String polygonCoordinates) {
        this.polygonCoordinates = polygonCoordinates;
    }
}