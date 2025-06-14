package org.example.models;

import jakarta.persistence.*;
import org.example.models.enums.BikeStatus;

import java.time.LocalDate;

@Entity
@Table(name = "bikes")
public class Bike extends BaseEntity {
    private String model;
    private LocalDate purchaseDate;
    private LocalDate lastMaintenanceDate;
    private BikeStatus currentStatus = BikeStatus.AVAILABLE;
    private TariffZone currentZone;

    @Column(name = "model", nullable = false)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "purchase_date")
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Column(name = "last_maintenance_date")
    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    public BikeStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(BikeStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    @ManyToOne
    @JoinColumn(name = "current_zone_id")
    public TariffZone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(TariffZone currentZone) {
        this.currentZone = currentZone;
    }
}