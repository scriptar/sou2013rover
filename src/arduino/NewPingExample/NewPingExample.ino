#include <Servo.h>

// ---------------------------------------------------------------------------
// Example NewPing library sketch that does a ping about 20 times per second.
// ---------------------------------------------------------------------------

#include <NewPing.h>

#define TRIGGER_PIN  5  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     2  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 10 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); // NewPing setup of pins and maximum distance.
//Servo myservo;
long pos = 0;
int i = 0;
int n = 0;

void setup() {
  Serial.begin(115200); // Open serial monitor at 115200 baud to see ping results.
//  myservo.attach(9);
}

void loop() {
  delay(200);                      // Wait 50ms between pings (about 20 pings/sec). 29ms should be the shortest delay between pings.
  unsigned int uS = sonar.ping();  // Send ping, get ping time in microseconds (uS).
  int pinVal = digitalRead(2);
//  Serial.print("Ping: ");
//  Serial.print(uS / US_ROUNDTRIP_CM); // Convert ping time to distance in cm and print result (0 = outside set distance range)
//  Serial.println("cm");
  Serial.print("Pin 2: ");
  Serial.println(pinVal);
//  pos = uS / US_ROUNDTRIP_CM;
//  if(pos < 50 && pos > 0) {
////    delay(1000);
//    for(n = 0; n < 2; n += 1) {
//    for(i = 0; i < 160; i += 1) {
//      myservo.write(i);
//      delay(2);
//    }
//    for(i = 160; i > 20; i -= 1) {
//      myservo.write(i);
//      delay(2);
//    }
//    }    
//  }
  
}
