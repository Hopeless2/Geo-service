import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

public class GeoServiceTest {
    private static long suiteStartTime;
    private long testStartTime;

    @BeforeAll
    public static void initSuite() {
        System.out.println("Running GeoServiceTest");
        suiteStartTime = System.nanoTime();
    }

    @AfterAll
    public static void completeSuite() {
        System.out.println("GeoServiceTest complete: " + (System.nanoTime() - suiteStartTime));
    }

    @BeforeEach
    public void initTest() {
        System.out.println("Starting new test");
        testStartTime = System.nanoTime();
    }

    @AfterEach
    public void finalizeTest() {
        System.out.println("Test complete:" + (System.nanoTime() - testStartTime));
    }

    @ParameterizedTest
    @MethodSource("sourceByIp")
    public void testByIp(String ip, Location expected) {
        GeoService geoService = new GeoServiceImpl();

        Location result = geoService.byIp(ip);

        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> sourceByIp() {
        return Stream.of(
                Arguments.of("127.0.0.1", new Location(null, null, null, 0)),
                Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("172.", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.", new Location("New York", Country.USA, null, 0)),
                Arguments.of("", null));
    }
    @ParameterizedTest
    @MethodSource("sourceByCoordinates")
    public void testByCoordinates(double latitude, double longitude, Location expected){
        GeoService geoService = new GeoServiceImpl();

        Location result = geoService.byCoordinates(latitude, longitude);

        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> sourceByCoordinates(){
        return Stream.of(
                Arguments.of(0.0, 0.0, new Location(null, null, null, 0)),
                Arguments.of(55.613753, 37.202703, new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of(40.741193, -74.008487, new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of(55.522432, 37.853203, new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of(38.421788 , -73.921508, new Location("New York", Country.USA, null, 0)),
                Arguments.of(11.111111, 11.111111, null));
    }

}

