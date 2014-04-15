SensorLogger
============
**University at Buffalo**

**CSE664: Applied Cryptography and Computer Security**

**Final Project Application**

Collects touch and sensor data from user input and sends it to a dedicated server.

[Link to application on Google Play](https://play.google.com/store/apps/details?id=edu.buffalo.cse664.sensorlogger)


##Submitted Data

####Metadata
```
Filename: Metadata.txt
Format: timestamp, IMEI, standing=1, left-handed=1, case=1, model, density, height, width
```
| Name | Type | Description|
|------|------|------------|
|timestamp|long|System time in UTC format|
|IMEI|string|Device IMEI|
|standing|boolean|1 if user is standing; 0 otherwise|
|left-handed|boolean|1 if user is left-handed; 0 otherwise|
|left-handed|boolean|1 if user has a case; 0 otherwise|
|model|string|device model name|
|density|integer|screen dpi; low = 120, medium = 160, high = 240|
|height|integer|Height of screen in pixels|
|width|integer|Width of screen in pixels|



####Touch Events
```
Filename: Touch.txt
Format: timestamp, #touch, x, y, pressure
```

####Accelerometer Events
```
Filename: Accel.txt
Format: timestamp, #touch, x, y, z, accuracy
```

####Gyroscope Events
```
Filename: Gyros.txt
Format: timestamp, #touch, x, y, z, accuracy
```

####Rotation Vector Events
```
Filename: Rotate.txt
Format: timestamp, #touch, x, y, z, accuracy
```



##Version History

####1.4
- Added left-hand data.
- UI changes.

####1.3
- Removed tablet support.
- UI changes.

####1.2
- Added support to record Rotation Vector sensor events.
- Increased number of clicks to 20.
- Modified UI.

####1.1
- Updated data format to include the associated touch event with each sensor event.
- TouchRecorder now writes to files on a background thread.

####1.0
- Initial release.

