# flightsapp

Demo project

## Running the project

1. Build the backend jar file: `gradle bootJar`
2. `docker compose up --build` in the root of the project

To run locally or only part of the project:
- Frontend: change the server proxy target from `http://backend:8080` to `http://localhost:8080` in `vite.config.ts` and `npm run dev`
- Backend: can run locally, it has a different `application.properties` for docker, **needs a running postgres database on localhost:5432**
- ExternalAPI: `python3 api.py`

PS: I usually wouldn't commit docker usernames and passwords to git, but for ease of use, I included them

## Frontend

Frontend is built with React + Ts, using a lot of ![shadcn](https://ui.shadcn.com/) components. Airplane seat image is from ![here](https://www.flyporter.com/en-ca/travel-information/inflight/seat-maps)

Shadcn componenets are under `/src/components/ui`, my components are in `/src/components/custom`, pages ate in `src/pages`.

## Backend

Using mapstruct for mapping and json web tokens for validation.


## External API

I took 'load data from an external API' to mean: query the data from an API in production rather than get the data and save it in a database ahead of time.

Since I couldn't find any free flight apis with unlimited requests, I took some data from [AviationStack API](https://aviationstack.com/documentation) and created my own fake API :3

### Typical response
```json
{"data": [
    {"airline":"Air Baltic","arrival_airport":"Helsinki-vantaa","arrival_time":"2025-03-24T08:00:00+00:00","departure_airport":"Riga International","departure_time":"2025-03-24T07:00:00+00:00","flight_date":"2025-03-24"},...
],
"error": 200,
"pagination": {
        "count":21,
        "limit":50,
        "offset":0,
        "total":21
    }}
```

### Request parameters
- `page_offset` default 0
- `limit` default 50
- `date` - `"mm-dd-yyyy"`
- `time_start` - `"HH:MM`
- `time_end` - `"HH:MM`
- `dept_airport`
- `arr_airport`
- `airline` last 3 match like in SQL `%[string]%`, case insensitive


## Problems I encountered and solved:

#### Database saving duplicates

This was because in `dev` mode, react sends two HTTP requests, which created a race condition in the database. This was partially solved with unique constraints and locking down the database, but similar problems are still present, eg. the phantom booker may not book seats fast enough, so frontend thinks some seats are avaliable when they are not. 

#### Date on the calander shows one day forward

Timezones. Fixed by parsing each part of the date separately.

#### External API bugs

Not really a bug, but since data is pulled from the external API which doesn't have sorting, sorting only works per page.

Sometimes the external API would load in much more data than there was to load, this was a similar issue as with the database storing duplicates - fixed it by loading all data before starting the web server

## Things to improve

- frontend is hosted with vite, which is probably not ideal for production
- the flightplan svg should crop rather than scale down, but I couldn't get it to work
- frontend should do more validation with zod
- profile page is currently for showing that bookings do indeed happen, it's not very pretty
- add logging
- add tests with sufficient coverage
- handle database errors properly (currently it's returning 403 a lot of the time, which is not the correct HTTP Code)
- Logged in validation in the frontend, currently it's only checking whether a jwt is stored, which shows the /seats view rather than /login as it should. The backend obviously checks and sends back 403
