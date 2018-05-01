package com.example.simems.gpsdraw;

import android.location.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SvgConverter {
    final private String version = "1.1",
        baseProfile = "full",
        xmlns="http://www.w3.org/2000/svg";
    private int width = 300,
        height = 300;

    public String createSvgFromLocationList(List<Location> locations) {
        return createOpenRootTag()
                .concat("\n\t")
                .concat(createPathFromLocationList(locations))
                .concat("\n")
                .concat(createCloseRootTag());
    }

    /**
     * creates a path tag from a list of locations
     * @param locations list of locations
     * @return path tag to be inserted in svg
     */
    private String createPathFromLocationList(List<Location> locations) {
        Location start = locations.get(0);

        String tag = "<path d=\"";

        for (Location location : locations) {
            tag = tag + createPoint(start, location);
        }

        return tag + "\">";
    }

    /**
     * creates a new point to add to the path, all points must be located relative to the start
     * of the path.
     * @param start start of the path
     * @param newPoint next location to move to
     * @return
     */
    private String createPoint(Location start, Location newPoint) {
        return String.format("M%f %f ",
                calculatePosition(start.getLongitude(), newPoint.getLongitude(), width),
                calculatePosition(start.getLatitude(), newPoint.getLatitude(), height));
    }

    /**
     * calculates position in either x or y axis of a point. position is an offset from the start
     * of the path, which should start at the middle of the canvas
     * @param pos1 position of the start of the path
     * @param pos2 position of the new point
     * @param fullLength full length of the canvas
     * @return position in either x or y axis for the new point
     */
    private double calculatePosition(double pos1, double pos2, double fullLength) {
        return (fullLength / 2) + (pos1 - pos2);
    }

    private String createOpenRootTag() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("version", version);
        attributes.put("baseProfile", baseProfile);
        attributes.put("xmlns", xmlns);
        attributes.put("width", String.valueOf(width));
        attributes.put("height", String.valueOf(height));

        return createTagOpen("svg", attributes);
    }

    private String createCloseRootTag() {
        return "</svg>";
    }

    private String createTagOpen(String tagName) {
        return "<" + tagName + ">";
    }

    private String createTagOpen(String tagName, Map<String, String> attributes) {
        return "<" + tagName + " " + createAttributePairs(attributes, false) + ">";
    }

    private String createAttributePairs(Map<String, String> attributes, boolean newLines) {
        String pairs = "";
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            pairs = pairs + entry.getKey() + "=\"" + entry.getValue() + "\"";
            if (newLines) {
                pairs = pairs + "\n";
            }
        }
        return pairs;
    }

    private String createTagClose(String tagName) {
        return "</" + tagName + ">";
    }
}
