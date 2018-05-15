package com.example.simems.gpsdraw;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Simen on 2018-05-15.
 * Super naive tests to discover what why the svg converter was drawing outside of the canvas
 */
@RunWith(MockitoJUnitRunner.class)
public class SvgConverterTest {

    private SvgConverter converter;

    @Before
    public void setUpSvgConverter() {
        converter = new SvgConverter();
    }

    @Test
    public void shouldCreateCorrectSvgFromLocationListShort() throws Exception {
        List<Location> input = new ArrayList<>();
        input.add(createMockLocation(0.00001, 0.00001));
        input.add(createMockLocation(0.00002, 0.00001));

        String actual = converter.createSvgFromLocationList(input);

        String expected =
                "<svg xmlns=\"http://www.w3.org/2000/svg\" baseProfile=\"full\" width=\"12.0\" version=\"1.1\" height=\"10.0\" >" +
                        "\n\t<path d=\"M6.000000 5.000000 L7.000000 5.000000 \" fill=\"transparent\" stroke=\"black\" />" +
                        "\n</svg>";

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCreateCorrectSvgFromLocationListOnlyLon() throws Exception {
        List<Location> input = new ArrayList<>();
        input.add(createMockLocation(0.00001, 0.00001));
        input.add(createMockLocation(0.00002, 0.00001));
        input.add(createMockLocation(0.00003, 0.00001));
        input.add(createMockLocation(0.00004, 0.00001));
        input.add(createMockLocation(0.00005, 0.00001));
        input.add(createMockLocation(0.00006, 0.00001));

        String actual = converter.createSvgFromLocationList(input);

        String expected =
                "<svg xmlns=\"http://www.w3.org/2000/svg\" baseProfile=\"full\" width=\"20.0\" version=\"1.1\" height=\"10.0\" >" +
                        "\n\t<path d=\"M10.000000 5.000000 L11.000000 5.000000 L12.000000 5.000000 L13.000000 5.000000 L14.000000 5.000000 L15.000000 5.000000 \" fill=\"transparent\" stroke=\"black\" />" +
                        "\n</svg>";

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCreateCorrectSvgFromLocationListOnlyLat() throws Exception {
        List<Location> input = new ArrayList<>();
        input.add(createMockLocation(0.00001, 0.00001));
        input.add(createMockLocation(0.00001, 0.00002));
        input.add(createMockLocation(0.00001, 0.00003));
        input.add(createMockLocation(0.00001, 0.00004));
        input.add(createMockLocation(0.00001, 0.00005));
        input.add(createMockLocation(0.00001, 0.00006));

        String actual = converter.createSvgFromLocationList(input);

        String expected =
                "<svg xmlns=\"http://www.w3.org/2000/svg\" baseProfile=\"full\" width=\"10.0\" version=\"1.1\" height=\"20.0\" >" +
                        "\n\t<path d=\"M5.000000 10.000000 L5.000000 11.000000 L5.000000 12.000000 L5.000000 13.000000 L5.000000 14.000000 L5.000000 15.000000 \" fill=\"transparent\" stroke=\"black\" />" +
                        "\n</svg>";

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCreateCorrectSvgFromLocationListOnlyBoth() throws Exception {
        List<Location> input = new ArrayList<>();
        input.add(createMockLocation(0.00001, 0.00001));
        input.add(createMockLocation(0.00002, 0.00002));
        input.add(createMockLocation(0.00003, 0.00003));
        input.add(createMockLocation(0.00004, 0.00004));
        input.add(createMockLocation(0.00005, 0.00005));
        input.add(createMockLocation(0.00006, 0.00006));

        String actual = converter.createSvgFromLocationList(input);

        String expected =
                "<svg xmlns=\"http://www.w3.org/2000/svg\" baseProfile=\"full\" width=\"20.0\" version=\"1.1\" height=\"20.0\" >" +
                        "\n\t<path d=\"M10.000000 10.000000 L11.000000 11.000000 L12.000000 12.000000 L13.000000 13.000000 L14.000000 14.000000 L15.000000 15.000000 \" fill=\"transparent\" stroke=\"black\" />" +
                        "\n</svg>";

        assertEquals(expected, actual);
    }

    private Location createMockLocation(double lon, double lat) {
        Location loc = Mockito.mock(Location.class);
        when(loc.getLongitude()).thenReturn(lon);
        when(loc.getLatitude()).thenReturn(lat);
        return loc;
    }
}