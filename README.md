       # Outdo
       My first app         

**App description:**
Outdo is an App that allows users to find friends and join groups to hang out or challenge.
Outdo allows users to create their own group and add participants, add challenges to that group and create unique achievements.
In the context of Outdo, “Challenge” is a scope of achievements united by the common theme. “Achievement” contains a description of the achievement (conditions for completion) and a list of users who have or haven’t completed this achievement. Users should confirm completion of the achievement by uploading an image from the gallery or taking a photo. Users participating in the achievement have opportunity to view another users’ attached photo.

By the way, I have attached 2 files that will provide a better understanding of the Outdo structure: “Diagram of data structure.drawio.pdf”.

**Tools and technologies I have used:**
•	Clean Architecture – I was trying to follow Clean Architecture rules to do the Outdo more flexible to changes;
•	MVVM – used in Outdo to separate business logic from presentation and make the code more understandable and readable;
•	Hilt – choosing between Dagger and Hilt I decided to use Hilt because this DI simplifies Dagger-related infrastructure, which was enough for the Outdo;
•	Navigation component and Safe args – used in Outdo to simplify navigation between Fragments and Activities, just like safe args simplify passing data between these components;
•	Room – according to my idea the Outdo should save data in local storage and the best way to do this would be to save data in tables, so that I chose Room to work with SQLite;
•	external storage – I used Backendless as an external database because Backendless stores data in the relational database and connected to SQL;
•	SharedPreferences – used in Outdo to store data that no need to be stored in the database structure and that can be easily accessed by key-value;
•	Coil – used the Coil library to load images in the Outdo because this library well-suited for straightforward image loading tasks that my app required without any difficult functionality;
•	Including Shimmer, Coroutines, Flow, Retrofit, Work manager, Firebase Messaging Service, Permissions and some other.

**Test users:**
1)	alice@gmail.com password: 1234567Aa
2)	john@gmail.com password: 1234567Aa
3)	sam@gmail.com password: 1234567Aa

**Problems:** 
Backendless provides free external storage, which has some limitations, such as 50 requests per minute. So when you first sign in into my app, Outdo may not load all the information in the request queue (see logs).

