SensorLogger
============

## Data Formats
###Metadata
```
timestamp, IMEI, standing=1, case=1, model-id, density-dpi, height-pixels, width-pixels
```

###Touch Events
```
timestamp, #touch, x, y, pressure
```

###Sensor Events
```
timestamp, #touch, x, y, z, accuracy
```

##Version History

####1.1
- Updated data format to include the associated touch event with each sensor event.
- TouchRecorder now writes to files on a background thread.

####1.0
- Initial release.

