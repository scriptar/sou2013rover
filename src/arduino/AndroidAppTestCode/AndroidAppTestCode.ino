//Quick sketch that shows ultrasonic sensor, bluetooth connectivity.

//NewPing library here:
//http://playground.arduino.cc/Code/NewPing
#include <NewPing.h>

//Pins
#define TRIGGER_PIN  12  //trigger on ultrasonic sensor.
#define ECHO_PIN     11  //echo on ultrasonic sensor.
#define ULTRASONIC_LED_PIN 10 //Display for ultrasonic sensor
#define BLUETOOTH_LED_PIN 9 //Display for receiving bluetooth

#define MAX_DISTANCE 100 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); // NewPing setup of pins and maximum distance.

unsigned int uS = 0;
unsigned int trip = 0;
unsigned int history = 0;   

unsigned int test = 0;
int inputLightToggle = 0;

void setup() {
  Serial.begin(115200); // Open serial monitor at 115200 baud to see ping results.
  pinMode(ULTRASONIC_LED_PIN, OUTPUT);
  pinMode(BLUETOOTH_LED_PIN, OUTPUT);
}

void loop() {

  delay(60);

  //*********** Sonar Calculations
  uS = sonar.ping();
  trip = uS / US_ROUNDTRIP_CM;

  //*********** Sonar History Smoothing
  if (trip == 0 && history == MAX_DISTANCE){
    //no action if at max value 
  } 
  else if(trip == 0 && history < MAX_DISTANCE){  
    history += 1;
  } 
  else if(trip > history && history < MAX_DISTANCE){
    history += 1; 
  }
  else if(trip < history && history > 0){
    history -= 1;
  }

  //*********** Sonar LED Display
  test = ((MAX_DISTANCE-history)*255)/MAX_DISTANCE;
  analogWrite(ULTRASONIC_LED_PIN, test);

  //*********** Bluetooth Output
  Serial.print("Ping: ");
  Serial.print(trip);
  Serial.println("cm");

  //*********** Bluetooth Received Display
  if(Serial.available()){
    Serial.read();
    if (inputLightToggle == 0){
      analogWrite(BLUETOOTH_LED_PIN, 255);
      inputLightToggle = 1;
    } 
    else{
      analogWrite(BLUETOOTH_LED_PIN, 0);
      inputLightToggle = 0;
    } 
  } 
}
