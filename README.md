# CD-Database Connectivity

## Objectives:
```
 Database Connectivity
```
## CD UI & Database

You have to write a database application to manage and query the titles and tracks of a CD collection.
A database containing two tables, CD and Track can be found in on the SQL Server (OPENBOX\WRR)
in the _WRAP301Music_ Database.

You are to use the Console Menu Application code (yours or the one from the XML-Menu practical)
and write an application that will allow a user to interact with this database.

The expected functionality of this application is described below:

```
 Connect to the database at startup
```

```
 The ability to create a new CD, specifying its details and adding new tracks – when adding CDs
and tracks, please don’t enter junk!
 It should be possible to edit an existing CD’s fields, as well as the tracks and their details on
the CD.
 It should be possible to query the database for:
o A list of all the tracks (and the CD they appear on) by a given artist
o All tracks that have a specific word or phrase in their names
o All CDs that have a specific word or phrase in their title
 Disconnect from the database when shutting down
