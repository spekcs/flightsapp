flightsapp

### Problems:

Database saving duplicates

Reason: Frontend sends two requests at once

Date on the calander shows one day forward

Since data is pulled from the external API which doesn't have sorting, sorting only works per page.

If recommendations don't make sense refresh the page, the phantom bookings may not have been all generated

It suddenly gave me a total of 1940 flights instead of 485, this is an externalAPI bug I don't have time to fix

Bug: the pagination menu sometimes disappears in built version when filters are applied

## External API

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

## Things to improve

- 
