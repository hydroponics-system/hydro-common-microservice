package com.hydro.common.dictionary.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hydro.common.dictionary.enums.Environment;

/**
 * Contains the structured alphanumeric part number. This number is generated
 * for each individual hydroponic system so it can be identified.
 * 
 * @author Sam Butler
 * @since May 28, 2022
 */
public class PartNumber {
    private static final String PART_NUMBER_FORMAT = "Required format is \"ppppppEssssss\" where:\n"
                                                     + "- \"pppppp\" is the random six-digit product number\n"
                                                     + "- \"E\" is the alphanumeric single-char environment (D,P,L)\n"
                                                     + "- \"ssssss\" is the six-digit system id (zero-padded)\n";
    private static final Pattern PART_NUMBER_PATTERN = Pattern
            .compile("(?<productnumber>[0-9]{6})(?<environment>[D|P|L])(?<system>[0-9]{6})");

    private String partNumber;
    private int productNumber;
    private Environment environment;
    private int systemId;

    /**
     * Default constructor for deserializing objects correctly.
     */
    public PartNumber() {}

    /**
     * Initialize a Part Number Object with the encoded Part Number String
     * 
     * @param partNumber Part Number
     */
    public PartNumber(String partNumber) {
        if(partNumber == null) {
            throw new IllegalArgumentException("Part Number must not be null. " + PART_NUMBER_FORMAT);
        }
        this.partNumber = partNumber;

        var matcher = PART_NUMBER_PATTERN.matcher(partNumber);
        if(!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Part Number \"%s\" is incorrectly formatted. %s",
                                                             partNumber, PART_NUMBER_FORMAT));
        }

        environment = Environment.get(matcher.group("environment"));
        productNumber = valueOf(matcher, "productnumber");
        systemId = valueOf(matcher, "system");
    }

    public int getProductNumber() {
        return productNumber;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public int getSystemId() {
        return systemId;
    }

    @Override
    public String toString() {
        return partNumber;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) {
            return false;
        }
        return partNumber.equals(other.toString());
    }

    @Override
    public int hashCode() {
        return partNumber.hashCode();
    }

    private static int valueOf(Matcher matcher, String group) {
        return Integer.valueOf(matcher.group(group));
    }
}