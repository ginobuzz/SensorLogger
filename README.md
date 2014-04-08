SensorLogger
============
UB CSE664 Final Project Application 

Collects touch and sensor data from user input and sends it to a dedicated server.



#Results

###Metadata
######Filename
```
Metadata.txt
```
######Format
```
timestamp, IMEI, standing=1, case=1, model-id, density-dpi, height-pixels, width-pixels
```

###Touch Events
######Filename
```
Touch.txt
```
######Format
```
timestamp, #touch, x, y, pressure
```

###Accelerometer Events
######Filename
```
Accel.txt
```
######Format
```
timestamp, #touch, x, y, z, accuracy
```

####Gyroscope Events
######Filename
```
Gyros.txt
```
######Format
```
timestamp, #touch, x, y, z, accuracy
```

####Rotation Vector Events
######Filename
```
Rotate.txt
```
######Format
```
timestamp, #touch, x, y, z, accuracy
```



#Version History

####1.1
- Updated data format to include the associated touch event with each sensor event.
- TouchRecorder now writes to files on a background thread.

####1.0
- Initial release.

