import requests
import secret

def main():
    url = f"https://api.aviationstack.com/v1/flights?access_key={secret.API_KEY}"

    with open("dept_tll.txt", "w") as f:
        dept_url = url + "&dept_iata=TLL"
        data = str(requests.get(dept_url).json())
        f.write(data)

    with open("arr_tll.txt", "w") as f:
        arr_url = url + "&arr_iata=TLL"
        data = str(requests.get(arr_url).json())
        f.write(data)

    with open("dept_hel.txt", "w") as f:
        dept_url = url + "&dept_iata=HEL"
        data = str(requests.get(dept_url).json())
        f.write(data)

    with open("arr_hel.txt", "w") as f:
        arr_url = url + "&arr_iata=HEL"
        data = str(requests.get(arr_url).json())
        f.write(data)

    with open("default.txt", "w") as f:
        data = str(requests.get(url).json())
        f.write(data)

if __name__ == "__main__":
    main()
