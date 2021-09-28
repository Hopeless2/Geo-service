import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GeoTest {
    private static long suiteStartTime;
    private long testStartTime;

    @BeforeAll
    public static void initSuite() {
        System.out.println("Running GeoTest");
        suiteStartTime = System.nanoTime();
    }

    @AfterAll
    public static void completeSuite() {
        System.out.println("GeoTest complete: " + (System.nanoTime() - suiteStartTime));
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

    @Test
    public void geoServiceTest_localHost_success(){
        GeoService geoService = Mockito.spy(GeoServiceImpl.class);
        final Location LOCATION = new Location(null, null, null, 0);

        Location expected = geoService.byIp("127.0.0.1");

        Assertions.assertEquals(expected, LOCATION);
    }

    @Test
    public void geoServiceTest_moscowIp_success(){
        GeoService geoService = Mockito.spy(GeoServiceImpl.class);
        final Location LOCATION = new Location("Moscow", Country.RUSSIA, "Lenina", 15);

        Location expected = geoService.byIp("172.0.32.11");

        Assertions.assertEquals(expected, LOCATION);
    }

    @Test
    public void geoServiceTest_newYorkIp_success(){
        GeoService geoService = Mockito.spy(GeoServiceImpl.class);
        final Location LOCATION = new Location("New York", Country.USA, " 10th Avenue", 32);

        Location expected = geoService.byIp("96.44.183.149");

        Assertions.assertEquals(expected, LOCATION);
    }

    @Test
    public void geoServiceTest_russiaIp_success(){
        GeoService geoService = Mockito.spy(GeoServiceImpl.class);
        final Location LOCATION = new Location("Moscow", Country.RUSSIA, null, 0);

        Location expected = geoService.byIp("172.");

        Assertions.assertEquals(expected, LOCATION);
    }

    @Test
    public void geoServiceTest_usaIp_success(){
        GeoService geoService = Mockito.spy(GeoServiceImpl.class);
        final Location LOCATION = new Location("New York", Country.USA, null,  0);

        Location expected = geoService.byIp("96.");

        Assertions.assertEquals(expected, LOCATION);
    }

    @Test
    public void geoServiceTest_nullOutput_success(){
        GeoService geoService = Mockito.spy(GeoServiceImpl.class);
        final Location LOCATION = null;

        Location expected = geoService.byIp("");

        Assertions.assertEquals(expected, LOCATION);
    }

    @Test
    public void localizationServiceTest_inputRussia_success(){
        LocalizationService localizationService = Mockito.spy(LocalizationServiceImpl.class);
        final String MESSAGE = "Добро пожаловать";

        String expected = localizationService.locale(Country.RUSSIA);

        Assertions.assertEquals(expected, MESSAGE);
    }

    @Test
    public void localizationServiceTest_inputUSA_success(){
        LocalizationService localizationService = Mockito.spy(LocalizationServiceImpl.class);
        final String MESSAGE = "Welcome";

        String expected = localizationService.locale(Country.USA);

        Assertions.assertEquals(expected, MESSAGE);
    }

    @Test
    public void messageSenderTest_russianTextWithRussianIP_success() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp("172."))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.");
        String message = messageSender.send(headers);

        String expected = "Добро пожаловать";
        Assertions.assertEquals(expected, message);
    }

    @Test
    public void messageSenderTest_englishTextWithUSAIP_success(){
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp("96."))
                .thenReturn(new Location("New York", Country.USA, null,  0));

        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.");
        String message = messageSender.send(headers);

        String expected = "Welcome";
        Assertions.assertEquals(expected, message);
    }

}

