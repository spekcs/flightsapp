import os
import ast
from datetime import datetime
from flask import Flask, request

app = Flask(__name__)


data = {
   "count": 0,
   "data": []
}

def read_data():
   for file in os.listdir("."):
      if file.endswith(".txt"):
         with open(file, "r") as f:
            content = f.read()
            new_data = ast.literal_eval(content)
            filtered_data = []
            for d in new_data["data"]:
               filtered_dict = {"flight_date": d["flight_date"],
                                "departure_airport": d["departure"]["airport"],
                                "departure_time": d["departure"]["scheduled"],
                                "arrival_airport": d["arrival"]["airport"],
                                "arrival_time": d["arrival"]["scheduled"],
                                "airline": d["airline"]["name"]}
               print(str(filtered_dict))
               filtered_data.append(filtered_dict)
            data["data"].extend(filtered_data)
            data["count"] += new_data["pagination"]["count"]

def filter_data(offset, limit, date, time_start, time_end, dept_airport, arr_airport, airline):
   filtered_flights = {
      "pagination": {
         "offset": offset,
         "count": 0,
         "limit": limit,
         "total": 0
      },
      "data": [],
      "error": 200
   }

   for flight in data["data"]:
         if date and date != flight["flight_date"]:
            continue
         if dept_airport and str.lower(dept_airport) not in str.lower(flight["departure_airport"]):
            continue

         try: 
            if arr_airport and str.lower(arr_airport) not in str.lower(flight["arrival_airport"]):
               continue
         except:
            print("Invalid data.") #For a few flights, the arrival airport straight up doesn't exist???

         try:
            if airline and str.lower(airline) not in str.lower(flight["airline"]):
               continue
         except:
            print("Invalid data.") # Nor airline apparently.

         if time_start and time_end:
            start = datetime.strptime(time_start, "%H:%M").time()
            end = datetime.strptime(time_end, "%H:%M").time()
            if not (start <= datetime.fromisoformat(flight["departure_time"]).time() <= end):
               continue

         filtered_flights["pagination"]["total"] += 1
         filtered_flights["data"].append(flight)


   total = filtered_flights["pagination"]["total"]
   if offset * limit >= total and total != 0:
      filtered_flights["data"] = []
      filtered_flights["error"] = 400
      return filtered_flights

   count = min(total - (offset * limit), limit)
   filtered_flights["pagination"]["count"] = count

   page_data = []
   for i in range(offset * limit, offset * limit + count): 
      page_data.append(filtered_flights["data"][i])
   
   filtered_flights["data"] = page_data


   return filtered_flights

@app.route("/")
def get_flights():
   if not data["data"]:
      read_data()

   page_offset = request.args.get("page_offset", default=0, type=int)
   limit = request.args.get("limit", default=50, type=int)
   date = request.args.get("date", default=None) # "mm-dd-yyyy"
   time_start = request.args.get("time_start", default=None) # "HH:MM"
   time_end = request.args.get("time_end", default=None)
   dept_airport = request.args.get("dept_airport", default=None)
   arr_airport = request.args.get("arr_airport", default=None)
   airline = request.args.get("airline", default=None)

   filtered_data = filter_data(page_offset, limit, date, time_start, time_end, dept_airport, arr_airport, airline)

   return filtered_data;

if __name__ == "__main__":
   app.run(host="0.0.0.0", port=3000)
