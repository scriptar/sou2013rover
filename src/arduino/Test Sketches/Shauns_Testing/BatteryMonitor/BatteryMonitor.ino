//Battery Monitor
//written by Shaun Wolff

float bLevel = 0;

void setup()
{
  Serial.begin(9600);
  pinMode(3, OUTPUT);
}

void loop()
{
  bLevel = analogRead(A0);
  bLevel = bLevel * .0049;
  if(bLevel < 2.50) analogWrite(3, 10);
  else {digitalWrite(3, LOW);}
  delay(1000);
  
}

