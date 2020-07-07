# Calorie Tracker Application

[FIT5046](https://www3.monash.edu/pubs/2019handbooks/units/FIT5046.html) Mobile and distributed computing systems assignment

A fitness **android** application that helps to keep track of calories consumed and burned. The user can set a daily calorie goal and be informed if they have met their goal.
The application communicates with the backend database to store and retrieve the data using RESTful web services.

**[Server-side](https://github.com/ianestraikh/CalorieTrackerBackend)**

With Calorie Tracker, the user is able to:   
* Sign up and log in.
* Set up a calorie goal.
* Select food items from the list or enter a new one.
* Enter numbers of steps taken.
* Check the amount of consumed and burned calories.
* See the pie chart and bar chart of the consumed and burned calories for the selected dates.
* See parks around their home on the map.
</br>

The application invokes the following public web APIs:
API | Purpose | 
---------------|---------|
[Google Custom Search](https://developers.google.com/custom-search) | To find an image and description of the new food items entered by the user.
[National Nutrient Database](https://fdc.nal.usda.gov/) | To retrieve the calorie and fat information for the new food items entered by the user.
[OpenCage Geocoder](https://opencagedata.com/) | To geocode the user's home address.
[Foursquare](https://developer.foursquare.com/) | To find parks around the user's home.
[Google Maps](https://developers.google.com/maps/documentation/android-sdk/intro) | To show parks around the user's home.
</br>

The application is integrated with the following android libraries:   
* [Gson](https://github.com/google/gson)
* [Bcrypt Java Library](https://github.com/patrickfav/bcrypt)
* [Glide](https://github.com/bumptech/glide)
* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
</br>

<p align="center">
<img src="images/1_nav.png?raw=true" width="30%">
<img src="images/2_home.png?raw=true" width="30%">
<img src="images/3_diet.png?raw=true" width="30%">
<img src="images/4_steps.png?raw=true" width="30%">
<img src="images/5_tracker.png?raw=true" width="30%">
<img src="images/7_report.png?raw=true" width="30%">
<img src="images/6_map.png?raw=true" width="30%">
</p>
