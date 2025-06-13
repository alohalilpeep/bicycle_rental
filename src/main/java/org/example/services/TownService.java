package org.example.services;

import org.example.models.legacy.Town;

public interface TownService {

    void addTown(String townName);

    Town findTownByName(String townName);
}
