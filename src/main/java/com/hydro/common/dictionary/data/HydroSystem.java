package com.hydro.common.dictionary.data;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Hydroponic System object.
 * 
 * @author Sam Butler
 * @since May 26, 2022
 */
@Schema(description = "Hydroponic System object holding information about the system.")
public class HydroSystem {

    @Schema(description = "System identifier.")
    private int id;

    @Schema(description = "Unique Universal identifier for the system.")
    private String uuid;

    @Schema(description = "Systems password for authentication")
    private String password;

    @Schema(description = "System part number.")
    private PartNumber partNumber;

    @Schema(description = "Nickname for the system.")
    private String name;

    @Schema(description = "The user id that has ownership of the system.")
    private int ownerUserId;

    @Schema(description = "When the log was created.")
    private LocalDateTime insertDate;

    public HydroSystem() {}

    public HydroSystem(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PartNumber getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(PartNumber partNumber) {
        this.partNumber = partNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(LocalDateTime insertDate) {
        this.insertDate = insertDate;
    }

}
