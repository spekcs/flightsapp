import os
import ast
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


@app.route("/")
def get_flights():
   if not data["data"]:
      read_data()
   return data;
