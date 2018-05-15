package com.example.simems.gpsdraw;

import android.location.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SvgConverter {
    final private String version = "1.1",
        baseProfile = "full",
        xmlns="http://www.w3.org/2000/svg";
    final public int canvasPadding = 10;

    public String createSvgFromLocationList(List<Location> locations) {
        double[] canvasSize = getCanvasSize(locations);

        return createOpenRootTag(canvasSize[0], canvasSize[1])
                .concat("\n\t")
                .concat(createPathFromLocationList(locations, canvasSize))
                .concat("\n")
                .concat(createCloseRootTag());
    }

    private double[] getCanvasSize(List<Location> locations) {
        if (locations.size() == 0) {
            double[] result = {0, 0};
            return result;
        }

        double firstLon = locations.get(0).getLongitude(),
                firstLat = locations.get(0).getLatitude();
        double maxX = firstLon, minX = firstLon, maxY = firstLat,  minY = firstLat;
        for (Location location : locations) {
            double lat = location.getLatitude(), lon = location.getLongitude();
            if (lat > maxY) maxY = lat;
            else if (lat < minY) minY = lat;
            if (lon > maxX) maxX = lon;
            else if (lon < minX) minX = lon;
        }
        double[] result = new double[2];
        result[0] = ( (maxX - minX) * 200000 ) + canvasPadding;
        result[1] = ( (maxY - minY) * 200000 ) + canvasPadding;
        return result;
    }

    /**
     * creates a path tag from a list of locations
     * @param locations list of locations
     * @param canvasDims array with dimensions of the canvas
     * @return path tag to be inserted in svg
     */
    private String createPathFromLocationList(List<Location> locations, double[] canvasDims) {
        Location start = locations.get(0);

        //start by moving the path to the center of the image
        String tag = String.format("<path d=\"M%f %f ", canvasDims[0] / 2, canvasDims[1] / 2);

        for (Location location : locations) {
            if (location == start) {
                // skip the first point, as this has already been drawn
                continue;
            }
            tag = tag + createPoint(start, location, canvasDims);
        }

        return tag + "\" fill=\"transparent\" stroke=\"black\" />";
    }

    /**
     * creates a new point to add to the path, all points must be located relative to the start
     * of the path.
     * @param start start of the path
     * @param newPoint next location to move to
     * @return
     */
    private String createPoint(Location start, Location newPoint,
                               double[] canvasDims) {
        return String.format("L%f %f ",
                calculatePosition(newPoint.getLongitude(), start.getLongitude(), canvasDims[0]),
                calculatePosition(newPoint.getLatitude(), start.getLatitude(), canvasDims[1]));
    }

    /**
     * calculates position in either x or y axis of a point. position is an offset from the start
     * of the path, which should start at the middle of the canvas. Coordinates are multiplied by
     * 100000 in order to get the fifth decimal place, so that movements in gps-coordinates make a
     * larger difference in the svg path. According to
     * https://gis.stackexchange.com/questions/8650/measuring-accuracy-of-latitude-and-longitude/8674#8674
     * the fifth decimal place in a gps coordinate should represent approximately 1.1 meters
     * @param pos1 position of the start of the path
     * @param pos2 position of the new point
     * @param fullLength full length of the canvas
     * @return position in either x or y axis for the new point
     */
    private double calculatePosition(double pos1, double pos2, double fullLength) {
        return (fullLength / 2) + ((pos1 - pos2) * 100000);
    }

    private String createOpenRootTag(double width, double height) {
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
            pairs = pairs + entry.getKey() + "=\"" + entry.getValue() + "\" ";
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
