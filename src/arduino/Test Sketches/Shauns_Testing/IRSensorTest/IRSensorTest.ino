  /*
   TCRT5000 sensor connected to Arduino analog input
   TCRT5000 pins: (see datasheet http://www.vishay.com/docs/83760/tcrt5000.pdf )
     C = Arduino analog input pin A1
     E = GND
     A = 100 ohm resistor, other end of resistor to 5V
     C = GND
  */
  
#define PIN_IR_SENSOR_READ A1

int tcrtState = 0; // variable for reading the TCRT5000 status
 
void setup() {
  pinMode(PIN_IR_SENSOR_READ, INPUT);   // initialize the tcrt5000 pin as an input, and 
  digitalWrite(PIN_IR_SENSOR_READ, HIGH); //turn on the internal pullup resistors
  Serial.begin(115200); 
}
 
void loop(){
  tcrtState = analogRead(PIN_IR_SENSOR_READ); // read the state of the tcrt5000
  Serial.println(tcrtState); //Print the state of the tcrt5000
  delay(1000);
}

