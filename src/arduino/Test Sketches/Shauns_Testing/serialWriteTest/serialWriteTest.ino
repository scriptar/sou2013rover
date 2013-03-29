
#include <NewPing.h>

#define ULTRA_TRIGGER_PIN  5  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ULTRA_ECHO_PIN     2  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define ULTRA_MAX_DISTANCE 200 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(ULTRA_TRIGGER_PIN, ULTRA_ECHO_PIN, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.


void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
}

void loop() {
  // put your main code here, to run repeatedly: 
  unsigned int uS = sonar.ping();  // Send ping, get ping time in microseconds (uS).
//  if(Serial.available())
//  {
//    Serial.println(uS / US_ROUNDTRIP_CM);
    Serial.write(uS / US_ROUNDTRIP_CM); // Convert ping time to distance in cm and print result (0 = outside set distance range)
//  }
  delay(200);
}
