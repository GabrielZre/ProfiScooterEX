#define UART_BAUD                   115200
#define MODEM_DTR_PIN               25
#define MODEM_TXD_PIN               26
#define MODEM_RXD_PIN               27
#define MODEM_PWR_PIN               4
#define BAT_ADC                     35
#define BOARD_POWER_ON              12
#define MODEM_RI_PIN                33
#define MODEM_RST_PIN               5
#define SD_MISO                     2
#define SD_MOSI                     15
#define SD_SCLK                     14
#define SD_CS                       13

#define TINY_GSM_MODEM_SIM7600
#define TINY_GSM_RX_BUFFER 1024
#define SerialAT Serial1

#define TINY_GSM_DEBUG Serial
#define bleServerName "BME280_ESP32"

// See the following for generating UUIDs:
// https://www.uuidgenerator.net/
#define SERVICE_UUID "91bad492-b950-4226-aa2b-4ede9fa42f59"

#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>

#include <Wire.h>
#include <TinyGsmClient.h>
#include <TinyGPSPlus.h>
#include <ArduinoHttpClient.h>
#include <SSLClient.h>
#ifdef DUMP_AT_COMMANDS  // if enabled it requires the streamDebugger lib
#include <StreamDebugger.h>
StreamDebugger debugger(SerialAT, Serial);
TinyGsm modem(debugger);
#else
TinyGsm modem(SerialAT);
#endif

const char root_ca[] PROGMEM =
"-----BEGIN CERTIFICATE-----\n"
"MIIFVzCCAz+gAwIBAgINAgPlk28xsBNJiGuiFzANBgkqhkiG9w0BAQwFADBHMQsw\n"
"CQYDVQQGEwJVUzEiMCAGA1UEChMZR29vZ2xlIFRydXN0IFNlcnZpY2VzIExMQzEU\n"
"MBIGA1UEAxMLR1RTIFJvb3QgUjEwHhcNMTYwNjIyMDAwMDAwWhcNMzYwNjIyMDAw\n"
"MDAwWjBHMQswCQYDVQQGEwJVUzEiMCAGA1UEChMZR29vZ2xlIFRydXN0IFNlcnZp\n"
"Y2VzIExMQzEUMBIGA1UEAxMLR1RTIFJvb3QgUjEwggIiMA0GCSqGSIb3DQEBAQUA\n"
"A4ICDwAwggIKAoICAQC2EQKLHuOhd5s73L+UPreVp0A8of2C+X0yBoJx9vaMf/vo\n"
"27xqLpeXo4xL+Sv2sfnOhB2x+cWX3u+58qPpvBKJXqeqUqv4IyfLpLGcY9vXmX7w\n"
"Cl7raKb0xlpHDU0QM+NOsROjyBhsS+z8CZDfnWQpJSMHobTSPS5g4M/SCYe7zUjw\n"
"TcLCeoiKu7rPWRnWr4+wB7CeMfGCwcDfLqZtbBkOtdh+JhpFAz2weaSUKK0Pfybl\n"
"qAj+lug8aJRT7oM6iCsVlgmy4HqMLnXWnOunVmSPlk9orj2XwoSPwLxAwAtcvfaH\n"
"szVsrBhQf4TgTM2S0yDpM7xSma8ytSmzJSq0SPly4cpk9+aCEI3oncKKiPo4Zor8\n"
"Y/kB+Xj9e1x3+naH+uzfsQ55lVe0vSbv1gHR6xYKu44LtcXFilWr06zqkUspzBmk\n"
"MiVOKvFlRNACzqrOSbTqn3yDsEB750Orp2yjj32JgfpMpf/VjsPOS+C12LOORc92\n"
"wO1AK/1TD7Cn1TsNsYqiA94xrcx36m97PtbfkSIS5r762DL8EGMUUXLeXdYWk70p\n"
"aDPvOmbsB4om3xPXV2V4J95eSRQAogB/mqghtqmxlbCluQ0WEdrHbEg8QOB+DVrN\n"
"VjzRlwW5y0vtOUucxD/SVRNuJLDWcfr0wbrM7Rv1/oFB2ACYPTrIrnqYNxgFlQID\n"
"AQABo0IwQDAOBgNVHQ8BAf8EBAMCAYYwDwYDVR0TAQH/BAUwAwEB/zAdBgNVHQ4E\n"
"FgQU5K8rJnEaK0gnhS9SZizv8IkTcT4wDQYJKoZIhvcNAQEMBQADggIBAJ+qQibb\n"
"C5u+/x6Wki4+omVKapi6Ist9wTrYggoGxval3sBOh2Z5ofmmWJyq+bXmYOfg6LEe\n"
"QkEzCzc9zolwFcq1JKjPa7XSQCGYzyI0zzvFIoTgxQ6KfF2I5DUkzps+GlQebtuy\n"
"h6f88/qBVRRiClmpIgUxPoLW7ttXNLwzldMXG+gnoot7TiYaelpkttGsN/H9oPM4\n"
"7HLwEXWdyzRSjeZ2axfG34arJ45JK3VmgRAhpuo+9K4l/3wV3s6MJT/KYnAK9y8J\n"
"ZgfIPxz88NtFMN9iiMG1D53Dn0reWVlHxYciNuaCp+0KueIHoI17eko8cdLiA6Ef\n"
"MgfdG+RCzgwARWGAtQsgWSl4vflVy2PFPEz0tv/bal8xa5meLMFrUKTX5hgUvYU/\n"
"Z6tGn6D/Qqc6f1zLXbBwHSs09dR2CQzreExZBfMzQsNhFRAbd03OIozUhfJFfbdT\n"
"6u9AWpQKXCBfTkBdYiJ23//OYb2MI3jSNwLgjt7RETeJ9r/tSQdirpLsQBqvFAnZ\n"
"0E6yove+7u7Y/9waLd64NnHi/Hm3lCXRSHNboTXns5lndcEZOitHTtNCjv0xyBZm\n"
"2tIMPNuzjsmhDYAPexZ3FL//2wmUspO8IFgV6dtxQ/PeEMMA3KgqlbbC1j+Qa3bb\n"
"bP6MvPJwNQzcmRk13NfIRmPVNnGuV/u3gm3c\n"
"-----END CERTIFICATE-----\n";

// Definition of current and voltage values and data for measurements
const int voltagePin = 34;
const int currentPin = 35;
int rawVoltageValue = 0;
int rawCurrentValue = 0;
float voltageValueArduino = 0.0;
float currentValueArduino = 0.0;
float voltageValueActual = 0.0;
float currentValueActual = 0.0;
float wattValueActual = 0.0;
float R1 = 27400.0;
float R2 = 1330.0;
float avgCurrentValue = 0.0;
float avgWattValue = 0.0;
int currentSamples = 20;
int sumOfLoops = 0;
float sumOfCurrent = 0.0;
float sumOfWatt = 0.0;
float wattHoursDrained = 0.0;
unsigned long lastTime = 0;
unsigned long timerDelay = 5000;
unsigned long connectionMillis = 0;
bool deviceConnected = false;

// GPRS credentials
const char apn[]  = "plus";     //SET TO YOUR APN
const char gprsUser[] = "Plus";
const char gprsPass[] = "2367";

// Server details
const char FIREBASE_HOST[]  = "profiscooterauth-default-rtdb.firebaseio.com";
const String FIREBASE_AUTH  = "Agh6BsSvczKCNZfhM8hse4X3BSyVLavvqOGFnxYC";
const String USER_UID = "KpWWcqizr8XY1vtumJxhIfjDobm1";
const String FIREBASE_PATH  = "Users/KpWWcqizr8XY1vtumJxhIfjDobm1/location";
const int SSL_PORT          = 443;

TinyGPSPlus gps;

TinyGsmClient client(modem, 0);
SSLClient secure_layer(&client);
HttpClient http = HttpClient(secure_layer, FIREBASE_HOST, SSL_PORT);

BLECharacteristic bleBatteryCharacteristics("ca73b3ba-39f6-4ab3-91ae-186dc9577d99", BLECharacteristic::PROPERTY_NOTIFY);
BLEDescriptor bleBatteryDescriptor(BLEUUID((uint16_t)0x2903));

// Multitasking
TaskHandle_t Task1;

// Setup callbacks onConnect and onDisconnect
class MyServerCallbacks: public BLEServerCallbacks {
  void onConnect(BLEServer* pServer) {
    deviceConnected = true;
    Serial.println("Connected...");
  };

  void onDisconnect(BLEServer* pServer) {
    deviceConnected = false;
    Serial.println("Disconnected...");
    pServer->getAdvertising()->start(); // restart advertising after disconnecting
  }
};

void displayInfo();

void setup()
{
    Serial.begin(115200);
    SerialAT.begin(115200, SERIAL_8N1, MODEM_RXD_PIN, MODEM_TXD_PIN);

    // Board Power control pin
    // When using battery power, this pin must be set to HIGH
    pinMode(BOARD_POWER_ON, OUTPUT);
    digitalWrite(BOARD_POWER_ON, HIGH);

    pinMode(MODEM_DTR_PIN, OUTPUT);
    digitalWrite(MODEM_DTR_PIN, LOW);

    pinMode(MODEM_RST_PIN, OUTPUT);
    digitalWrite(MODEM_RST_PIN, HIGH);

    pinMode(MODEM_PWR_PIN, OUTPUT);

    digitalWrite(MODEM_PWR_PIN, LOW);
    delay(100);
    digitalWrite(MODEM_PWR_PIN, HIGH);
    delay(1000);
    digitalWrite(MODEM_PWR_PIN, LOW);

    do {
        Serial.print("Modem starting...");
        int retry = 0;
        while (!modem.testAT(1000)) {
            Serial.println(".");
            if (retry++ > 10) {
                digitalWrite(MODEM_PWR_PIN, LOW);
                delay(100);
                digitalWrite(MODEM_PWR_PIN, HIGH);
                delay(1000);    //Ton = 1000ms ,Min = 500ms, Max 2000ms
                digitalWrite(MODEM_PWR_PIN, LOW);
                retry = 0;
            }
        }
        Serial.println();
        delay(200);
        modem.sendAT("+CGDRT=5,1");
        modem.waitResponse(10000L);
        modem.sendAT("+CGSETV=5,0");
        modem.waitResponse(10000L);
        modem.sendAT("+CVAUXS=1");
        modem.waitResponse(10000L);

        Serial.print("GPS starting...");
        modem.sendAT("+CGNSSPWR=1");

    } while (modem.waitResponse(10000UL, "+CGNSSPWR: READY!") != 1);

    Serial.println("GPS Ready!");
    //Configure the baud rate of UART3 and GPS module
    modem.sendAT("+CGNSSIPR=9600");
    modem.waitResponse(1000L);
    //Configure GNSS support mode : BD + GPS
    modem.sendAT("+CGNSSMODE=3");
    modem.waitResponse(1000L);
    // Configure NMEA sentence type
    modem.sendAT("+CGNSSNMEA=1,1,1,1,1,1,0,0");
    modem.waitResponse(1000L);
    // Set NMEA output rate : 1HZ
    modem.sendAT("+CGPSNMEARATE=1");
    modem.waitResponse(1000L);
    // Send data received from UART3 to NMEA port
    modem.sendAT("+CGNSSTST=1");
    modem.waitResponse(1000L);
    // Select the output port for NMEA sentence
    modem.sendAT("+CGNSSPORTSWITCH=0,1");
    modem.waitResponse(1000L);
    // Unlock your SIM card with a PIN if needed
    if (gprsPass && modem.getSimStatus() != 3) {
        modem.simUnlock(gprsPass);
    }
    Serial.println("Modem Name: ");
    modem.getModemName();
    modem.getModemInfo();
    //Add CA Certificate
    secure_layer.setCACert(root_ca);


    
  // Create the BLE Device
  BLEDevice::init(bleServerName);
  // Create the BLE Server
  BLEServer *pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());
  // Create the BLE Service
  BLEService *bleService = pServer->createService(SERVICE_UUID);
  // Battery
  bleService->addCharacteristic(&bleBatteryCharacteristics);
  bleBatteryDescriptor.setValue("BLE battery");
  bleBatteryCharacteristics.addDescriptor(new BLE2902());
  // Start the service
  bleService->start();
  // Start advertising
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pServer->getAdvertising()->start();
  Serial.println("Waiting a client connection to notify...");
  Serial.println("Starting measuring and ble task...");
  Serial.println("Waiting a client connection to notify...");
    
  xTaskCreatePinnedToCore(
                      Task1code,   /* Task function. */
                      "Task1",     /* name of task. */
                      10000,       /* Stack size of task */
                      NULL,        /* parameter of the task */
                      1,           /* priority of the task */
                      &Task1,      /* Task handle to keep track of created task */
                      1);          /* pin task to core 1 */                  
  delay(500); 
}




void Task1code( void * pvParameters ) {
  Serial.print("Measurement and BLE Task is running on core ");
  Serial.println(xPortGetCoreID());

  for(;;) {
    if (deviceConnected) {
      unsigned long currentMillis = millis();

      if (!connectionMillis) {
        connectionMillis = currentMillis;
      }
      unsigned long elapsedTime = currentMillis - connectionMillis;
      sumOfLoops++;

      float avgForCurrent = 0.0;
      float avgForWatt = 0.0;
      for(int i=0; i<currentSamples; i++) {
        rawVoltageValue = analogRead(voltagePin);
        rawCurrentValue = analogRead(currentPin);

        voltageValueArduino = (rawVoltageValue * 3.5) / 4095.0; 
        currentValueArduino = (rawCurrentValue * 3.5) / 4095.0; 

        voltageValueActual = voltageValueArduino / (R2/(R1+R2));
        currentValueActual = 73.3 * (currentValueArduino / 3.28) - 36.65;

        wattValueActual = voltageValueActual * currentValueActual;
        
        avgForCurrent += currentValueActual;
        avgForWatt += wattValueActual;
        while (millis() - currentMillis < 250) {}
      currentMillis = millis();
      }

      avgForCurrent = avgForCurrent / currentSamples;
      sumOfCurrent += avgForCurrent;
      avgCurrentValue = sumOfCurrent / sumOfLoops;

      avgForWatt = avgForWatt / currentSamples;
      sumOfWatt += avgForWatt;
      avgWattValue = sumOfWatt / sumOfLoops;
      wattHoursDrained = (elapsedTime * avgWattValue) / 3600000;

      //Notify battery reading from BLE
      static char dataBat[12]; 
      snprintf(dataBat, sizeof(dataBat), "%.1f,%.1f", voltageValueActual, wattHoursDrained);

      bleBatteryCharacteristics.setValue(dataBat);
      bleBatteryCharacteristics.notify();   

    } else {
      //Reset if device is disconnected
      avgCurrentValue = 0.0;
      avgWattValue = 0.0;
      sumOfLoops = 0.0;
      sumOfCurrent = 0.0;
      connectionMillis = 0;
      sumOfWatt = 0.0;
    }
  }
}




void loop()
{
    Serial.print(F("Connecting to network"));
    if (!modem.gprsConnect(apn, gprsUser, gprsPass)) {
        Serial.println(" fail");
        delay(1000);
        return;
    }
    
    Serial.println(" OK");
    
    http.connectionKeepAlive(); // Currently, this is needed for HTTPS
    http.connect(FIREBASE_HOST, SSL_PORT);
    
    while (true) {
        if (!http.connected()) {
            Serial.println();
            http.stop();// Shutdown
            Serial.println("HTTP  not connect");
            break;
        }
        else {
            gps_loop();
        }
    }
}

void gps_loop()
{
    String latitude;
    String longitude;
    String datetime = "";
    
    if (gps.date.isValid() && gps.time.isValid()){
      datetime += gps.date.day();
      datetime += gps.date.month();
      datetime += gps.date.year();
      String datetimefull = datetime;
      Serial.println("Datetime: " + datetime);
    }

    Serial.println(F("Sats HDOP  Latitude   Longitude   Fix  Date       Time     Date Alt    Course Speed Card  Distance Course Card  Chars Sentences Checksum"));
    Serial.println(F("           (deg)      (deg)       Age                      Age  (m)    --- from GPS ----  ---- to London  ----  RX    RX        Fail"));
    Serial.println(F("----------------------------------------------------------------------------------------------------------------------------------------"));

    // Show full information
    static const double LONDON_LAT = 51.508131, LONDON_LON = -0.128002;
    
    printInt(gps.satellites.value(), gps.satellites.isValid(), 5);
    printFloat(gps.hdop.hdop(), gps.hdop.isValid(), 6, 1);
    printFloat(gps.location.lat(), gps.location.isValid(), 11, 6);
    printFloat(gps.location.lng(), gps.location.isValid(), 12, 6);
    printInt(gps.location.age(), gps.location.isValid(), 5);
    printDateTime(gps.date, gps.time);
    printFloat(gps.altitude.meters(), gps.altitude.isValid(), 7, 2);
    printFloat(gps.course.deg(), gps.course.isValid(), 7, 2);
    printFloat(gps.speed.kmph(), gps.speed.isValid(), 6, 2);
    printStr(gps.course.isValid() ? TinyGPSPlus::cardinal(gps.course.deg()) : "*** ", 6);

    unsigned long distanceKmToLondon =
        (unsigned long)TinyGPSPlus::distanceBetween(
            gps.location.lat(),
            gps.location.lng(),
            LONDON_LAT,
            LONDON_LON) / 1000;
    printInt(distanceKmToLondon, gps.location.isValid(), 9);

    double courseToLondon =
        TinyGPSPlus::courseTo(
            gps.location.lat(),
            gps.location.lng(),
            LONDON_LAT,
            LONDON_LON);

    printFloat(courseToLondon, gps.location.isValid(), 7, 2);

    const char *cardinalToLondon = TinyGPSPlus::cardinal(courseToLondon);

    printStr(gps.location.isValid() ? cardinalToLondon : "*** ", 6);

    printInt(gps.charsProcessed(), true, 6);
    printInt(gps.sentencesWithFix(), true, 10);
    printInt(gps.failedChecksum(), true, 9);
    Serial.println();

    latitude = String(gps.location.lat(), 6); // Latitude in degrees (double)
    longitude = String(gps.location.lng(), 6); // Longitude in degrees (double)

    String gpsData = "{";
    gpsData += "\"latitude\":" + latitude + ",";
    gpsData += "\"longitude\":" + longitude + ",";
    gpsData += "\"date\":" + datetime + "";
    gpsData += "}";
    smartDelay(1000);
    PostToFirebase("PATCH", FIREBASE_PATH, gpsData, &http);
}

void displayInfo()
{
    Serial.print(F("Location: "));
    if (gps.location.isValid()) {
        Serial.print(gps.location.lat(), 6);
        Serial.print(F(","));
        Serial.print(gps.location.lng(), 6);
    } else {
        Serial.print(F("INVALID"));
    }

    Serial.print(F("  Date/Time: "));
    if (gps.date.isValid()) {
        Serial.print(gps.date.month());
        Serial.print(F("/"));
        Serial.print(gps.date.day());
        Serial.print(F("/"));
        Serial.print(gps.date.year());
    } else {
        Serial.print(F("INVALID"));
    }

    Serial.print(F(" "));
    if (gps.time.isValid()) {
        if (gps.time.hour() < 10) Serial.print(F("0"));
        Serial.print(gps.time.hour());
        Serial.print(F(":"));
        if (gps.time.minute() < 10) Serial.print(F("0"));
        Serial.print(gps.time.minute());
        Serial.print(F(":"));
        if (gps.time.second() < 10) Serial.print(F("0"));
        Serial.print(gps.time.second());
        Serial.print(F("."));
        if (gps.time.centisecond() < 10) Serial.print(F("0"));
        Serial.print(gps.time.centisecond());
    } else {
        Serial.print(F("INVALID"));
    }

    Serial.println();
}

void PostToFirebase(const char* method, const String & path , const String & data, HttpClient* http) {
  String response;
  int statusCode = 0;
  http->connectionKeepAlive(); // Currently, this is needed for HTTPS
  
  String url;
  if (path[0] != '/') {
    url = "/";
  }
  url += path + ".json";
  url += "?auth=" + FIREBASE_AUTH;
  Serial.print("POST:");
  Serial.println(url);
  Serial.print("Data:");
  Serial.println(data);
  
  String contentType = "application/json";
  http->put(url, contentType, data);
  
  statusCode = http->responseStatusCode();
  Serial.print("Status code: ");
  Serial.println(statusCode);
  response = http->responseBody();
  Serial.print("Response: ");
  Serial.println(response);

  if (http->connected()) {
    Serial.println();
    http->stop();
    Serial.println("HTTP POST disconnected");
  }
}

static void smartDelay(unsigned long ms)
{
    unsigned long start = millis();
    do {
        while (SerialAT.available())
            gps.encode(SerialAT.read());
    } while (millis() - start < ms);
}

static void printFloat(float val, bool valid, int len, int prec)
{
    if (!valid) {
        while (len-- > 1)
            Serial.print('*');
        Serial.print(' ');
    } else {
        Serial.print(val, prec);
        int vi = abs((int)val);
        int flen = prec + (val < 0.0 ? 2 : 1); // . and -
        flen += vi >= 1000 ? 4 : vi >= 100 ? 3 : vi >= 10 ? 2 : 1;
        for (int i = flen; i < len; ++i)
            Serial.print(' ');
    }
    smartDelay(0);
}

static void printInt(unsigned long val, bool valid, int len)
{
    char sz[32] = "*****************";
    if (valid)
        sprintf(sz, "%ld", val);
    sz[len] = 0;
    for (int i = strlen(sz); i < len; ++i)
        sz[i] = ' ';
    if (len > 0)
        sz[len - 1] = ' ';
    Serial.print(sz);
    smartDelay(0);
}

static void printDateTime(TinyGPSDate &d, TinyGPSTime &t)
{
    if (!d.isValid()) {
        Serial.print(F("********** "));
    } else {
        char sz[32];
        sprintf(sz, "%02d/%02d/%02d ", d.month(), d.day(), d.year());
        Serial.print(sz);
    }

    if (!t.isValid()) {
        Serial.print(F("******** "));
    } else {
        char sz[32];
        sprintf(sz, "%02d:%02d:%02d ", t.hour(), t.minute(), t.second());
        Serial.print(sz);
    }

    printInt(d.age(), d.isValid(), 5);
    smartDelay(0);
}

static void printStr(const char *str, int len)
{
    int slen = strlen(str);
    for (int i = 0; i < len; ++i)
        Serial.print(i < slen ? str[i] : ' ');
    smartDelay(0);
}
