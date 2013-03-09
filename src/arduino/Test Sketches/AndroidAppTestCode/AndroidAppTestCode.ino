//Quick sketch that shows ultrasonic sensor, bluetooth connectivity.

//NewPing library here:
//http://playground.arduino.cc/Code/NewPing
#include <NewPing.h>

//Ultrasonic Module
#define TRIGGER_PIN  12  //trigger on ultrasonic sensor.
#define ECHO_PIN     11  //echo on ultrasonic sensor.

//Ultrasonic Display
#define ULTRASONIC_LED_PIN 10 //Display for ultrasonic sensor

//Display for receiving bluetooth
#define FORWARD_LED_PIN 7 
#define REVERSE_LED_PIN 6
#define LEFT_LED_PIN 5
#define RIGHT_LED_PIN 4

#define MAX_DISTANCE 100 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); // NewPing setup of pins and maximum distance.

unsigned int uS = 0;
unsigned int trip = 0;
unsigned int history = 0;   

unsigned int test = 0;

void setup() {
  // Open serial monitor at 115200 baud to see ping results.
  Serial.begin(115200); 
  // Setting Pin Modes
  pinMode(ULTRASONIC_LED_PIN, OUTPUT);
  pinMode(FORWARD_LED_PIN, OUTPUT);
  pinMode(REVERSE_LED_PIN, OUTPUT);
  pinMode(LEFT_LED_PIN, OUTPUT);
  pinMode(RIGHT_LED_PIN, OUTPUT);
}

void loop() {

  // Loop Delay
  delay(60);

  // ****************************
  // Sonar Calculations
  // ****************************
  uS = sonar.ping();
  trip = uS / US_ROUNDTRIP_CM;

  //Sonar History Smoothing
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

  // ****************************
  // Sonar Distance Display
  // ****************************
  test = ((MAX_DISTANCE-history)*255)/MAX_DISTANCE;
  analogWrite(ULTRASONIC_LED_PIN, test);

  // ****************************
  // Bluetooth Output
  // ****************************
  Serial.print("Ping: ");
  Serial.print(trip);
  Serial.println("cm");

  // ****************************
  // Bluetooth Input
  // ****************************
  if(Serial.available()){
    byte input = Serial.read(); 
    switch (input){

    case 10:
      digitalWrite(FORWARD_LED_PIN, HIGH);
      delay(200);
      digitalWrite(FORWARD_LED_PIN, LOW);
      break;
    case 20:
      digitalWrite(REVERSE_LED_PIN, HIGH);
      delay(200);
      digitalWrite(REVERSE_LED_PIN, LOW);
      break;
    case 30:
      digitalWrite(LEFT_LED_PIN, HIGH);
      delay(200);
      digitalWrite(LEFT_LED_PIN, LOW);
      break;
    case 40:
      digitalWrite(RIGHT_LED_PIN, HIGH);
      delay(200);
      digitalWrite(RIGHT_LED_PIN, LOW);
      break;
    default:
      digitalWrite(FORWARD_LED_PIN, HIGH);
      digitalWrite(REVERSE_LED_PIN, HIGH);
      digitalWrite(LEFT_LED_PIN, HIGH);
      digitalWrite(RIGHT_LED_PIN, HIGH);
      delay(1000);
      digitalWrite(FORWARD_LED_PIN, LOW);
      digitalWrite(REVERSE_LED_PIN, LOW);
      digitalWrite(LEFT_LED_PIN, LOW);
      digitalWrite(RIGHT_LED_PIN, LOW);
    }
  } 
}





