/*
  Blink
  Turns on an LED on for one second, then off for one second, repeatedly.
 
  This example code is in the public domain.
 */
 #include <NewPing.h>
// Pin 13 has an LED connected on most Arduino boards.
// give it a name:
volatile int led = 4;
#define ULTRA_TRIGGER_PIN  5  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ULTRA_ECHO_PIN     2  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define ULTRA_MAX_DISTANCE 10 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.
NewPing sonar(ULTRA_TRIGGER_PIN, ULTRA_ECHO_PIN, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.

// the setup routine runs once when you press reset:
void setup() {                
    Serial.begin(115200);
  // initialize the digital pin as an output.
  pinMode(led, OUTPUT);
  attachInterrupt(0, interrupt, LOW);
}

// the loop routine runs over and over again forever:
void loop() {
  unsigned int uS = sonar.ping();  // Send ping, get ping time in microseconds (uS).
  digitalWrite(led, LOW);
//  digitalRead(2);
  delay(200);
}

void interrupt(){
//  if(digitalRead(2) == LOW) {
  digitalWrite(led, HIGH);}   // turn the LED on (HIGH is the voltage level)
//else {digitalWrite(led, LOW);}

