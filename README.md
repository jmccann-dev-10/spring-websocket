# Spring-Websocket
One of the groups wanted to implement a chat function in Cohort 9, so I created a sample project as a proof of concept with Spring Websockets

## Technologies
- Docker
- React
- Spring Boot
- Nginx
- [User Api](https://github.com/dev10-program/user-api)

## How To
All four of the main components are in Docker images.  Simply run:
```
docker-compose up -d
```
Then navigate to [localhost](http://localhost)

Once the application is running, you can open up multiple webpages, login as different users, and see the chat working in realtime.  Usernames are persisted through localstorage so if you reload a page after logging in as someone else in a different tab, you'll be the same user in both tabs.  This doesn't keep you from connecting to chat more than once though.

When you're done, run:
```
docker-compose down -v
```
and everything will be taken care of.

## Notable Stuff
- I found a way to make the topic name dynamic in Spring, so the application is designed to allow users to connect and disconnect on a whim to various different rooms.  I suspect the sessions clean themselves up, but I didn't do any load testing or use any messaging queues like RabitMQ, so I'd like to see how this runs in the future.
- The username is pulled directly from the JWT, so you must be logged in through the User-Api to chat.
- Also, I stole some images off the net and turned them into SVGs for the 404 and 500 pages.  They are cute.  I should figure out where I got them from so I can give appropriate credit to the image creators.
- I think my handling of the disconnect process is a little off, I suspect it has to do with the react state, but you currently get a handful of errors when navigating away from a page saying the `websock is already disconnecting`.  It doesn't affect anything, though, so I'll have to work on this when I have more time later.